package renderEngine;

import entity.Camera;
import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.VegetationShader;

import java.util.Map;

public class VegetationRenderer {

    private VegetationShader shader;

    public VegetationRenderer(VegetationShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.startProgram();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stopProgram();
    }

    public void render(Map<TexturedModel, Integer> vegetations, Camera camera){
        shader.startProgram();
        shader.loadViewMatrix(camera);

        for (TexturedModel texturedModel: vegetations.keySet()){
            prepareTexturedModel(texturedModel);
            int instances = vegetations.get(texturedModel);

            GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, texturedModel.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0, instances);

            unbindTexturedModel();
        }

        shader.stopProgram();
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
}
