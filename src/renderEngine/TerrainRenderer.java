package renderEngine;

import chunk.ChunkMesh;
import entity.Camera;
import entity.DirectionalLight;
import entity.Entity;
import entity.Light;
import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Text;
import shaders.Shader;
import shaders.TerrainShader;
import terrain.TerrainChunk;
import utils.Maths;

import java.util.List;
import java.util.Map;

public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.startProgram();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stopProgram();
    }

    public void render(Map<TexturedModel, List<TerrainChunk>> terrainBlocks, Camera camera, DirectionalLight light){
        this.shader.startProgram();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);


        for (TexturedModel model : terrainBlocks.keySet()){
            prepareTexturedModel(model);
            List<TerrainChunk> batch = terrainBlocks.get(model);
            for (TerrainChunk terrainChunk : batch){
                prepareInstance(terrainChunk);
                GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getModel().getVertexCount() / 3);
            }
            unbindTexturedModel();
        }
        this.shader.stopProgram();
    }

    public void cleanUp(){
        this.shader.cleanUp();
    }

    private void prepareTexturedModel(TexturedModel model) {
        Model rawModel = model.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(TerrainChunk terrainChunk) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(terrainChunk.getPosition(),
                0, 0, 0, 1);

        shader.loadTransformationMatrix(transformationMatrix);
    }
}
