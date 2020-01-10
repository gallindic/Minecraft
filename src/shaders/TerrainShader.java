package shaders;

import entity.Camera;
import entity.DirectionalLight;
import entity.Light;
import org.lwjgl.util.vector.Matrix4f;
import utils.Maths;

public class TerrainShader extends ShaderProgram {

    private static final String vertexFile = "src/shaders/shaderFiles/terrainVertexShader.txt";
    private static final String fragmentFile = "src/shaders/shaderFiles/terrainFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    private int location_lightPosition;
    private int location_lightColor;
    private int location_toShadowMapSpace;
    private int location_shadowMap;

    public TerrainShader() {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightDirection");
        location_lightColor = super.getUniformLocation("lightColor");
        location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
        location_shadowMap = super.getUniformLocation("shadowMap");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadLight(DirectionalLight light){
        super.loadVector(location_lightPosition, light.getDirection());
        super.loadVector(location_lightColor, light.getColor());
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadToShadowSpaceMatrix(Matrix4f matrix){
        super.loadMatrix(location_toShadowMapSpace, matrix);
    }

    public void loadShadowMap(int i){
        super.loadInt(location_shadowMap, i);
    }
}


