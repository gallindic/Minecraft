package renderEngine;

import entity.Camera;
import entity.Entity;
import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.Shader;
import utils.Maths;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

    private Shader shader;

    public EntityRenderer(Shader shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.startProgram();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stopProgram();
    }

    public void cleanUp(){
        this.shader.cleanUp();
    }

    public void render(Map<TexturedModel, List<Entity>> entities, Camera camera) {
        this.shader.startProgram();
        shader.loadViewMatrix(camera);

        for (TexturedModel texturedModel : entities.keySet()){
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);

            for (Entity entity : batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            unbindTexturedModel();
        }

        this.shader.stopProgram();
    }

    private void prepareTexturedModel(TexturedModel model) {
        Model rawModel = model.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());

        shader.loadTransformationMatrix(transformationMatrix);
    }
}
