package shaders;

import entity.Camera;
import org.lwjgl.util.vector.Matrix4f;
import utils.Maths;

public class VegetationShader extends ShaderProgram {

    private static final String vertexFile = "src/shaders/shaderFiles/vegetationVertexShader.txt";
    private static final String fragmentFile = "src/shaders/shaderFiles/vegetationFragmentShader.txt";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public VegetationShader() {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}


