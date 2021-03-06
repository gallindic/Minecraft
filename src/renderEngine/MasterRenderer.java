package renderEngine;

import entity.Camera;
import entity.DirectionalLight;
import entity.Entity;
import model.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import shaders.Shader;
import shaders.TerrainShader;
import shaders.VegetationShader;
import shadows.ShadowMapMasterRenderer;
import terrain.TerrainChunk;
import vegetation.VegetationMesh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;
    private Shader shader = new Shader();
    private VegetationShader vegetationShader = new VegetationShader();
    private TerrainShader terrainShader = new TerrainShader();

    private TerrainRenderer terrainRenderer;
    private VegetationRenderer vegetationRenderer;
    private EntityRenderer entityRenderer;
    private ShadowMapMasterRenderer shadowMapMasterRenderer;

    private Map<TexturedModel,List<Entity>> entities = new HashMap<>();
    private Map<TexturedModel, List<TerrainChunk>> terrainChunks = new HashMap<>();


    private Map<TexturedModel, Integer> vegetations = new HashMap<>();

    public MasterRenderer(Camera camera){
        createProjectionMatrix();

        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        vegetationRenderer = new VegetationRenderer(vegetationShader, projectionMatrix);
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        shadowMapMasterRenderer = new ShadowMapMasterRenderer(camera);
    }

    public void prepareScreen(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.53f, 0.81f, 0.92f, 1.0f);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE1, getShadowMapTexture());
    }

    public void render(Camera camera, DirectionalLight sun){
        prepareScreen();

        terrainRenderer.render(terrainChunks, camera, sun, shadowMapMasterRenderer.getToShadowMapSpaceMatrix());
        vegetationRenderer.render(vegetations, camera);
        entityRenderer.render(entities, camera);

        renderShadowMap(sun);

        entities.clear();
        terrainChunks.clear();
        vegetations.clear();
    }

    public void processEntity(Entity entity){
        List<Entity> batch = entities.get(entity.getTexturedModel());
        if(batch != null)
            batch.add(entity);
        else{
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entity.getTexturedModel(), newBatch);
        }
    }

    public void processTerrain(TerrainChunk terrainChunk){
        List<TerrainChunk> batch = terrainChunks.get(terrainChunk.getTexturedModel());
        if(batch != null)
            batch.add(terrainChunk);
        else{
            List<TerrainChunk> newBatch = new ArrayList<>();
            newBatch.add(terrainChunk);
            terrainChunks.put(terrainChunk.getTexturedModel(), newBatch);
        }
    }

    public void processVegetation(VegetationMesh vegetationMesh){
        vegetations.put(vegetationMesh.getTexturedModel(), vegetationMesh.getInstanceCount());

    }

    public void cleanUp(){
        terrainRenderer.cleanUp();
        vegetationRenderer.cleanUp();
        entityRenderer.cleanUp();
        shadowMapMasterRenderer.cleanUp();
    }

    public void renderShadowMap(DirectionalLight sun){
        shadowMapMasterRenderer.render(terrainChunks, sun);

        terrainChunks.clear();
    }

    public int getShadowMapTexture(){
        return shadowMapMasterRenderer.getShadowMap();
    }

    private void createProjectionMatrix(){
        projectionMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
