package shadows;

import java.util.List;
import java.util.Map;

import model.Model;
import model.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.ShadowShader;
import terrain.TerrainChunk;
import utils.Maths;

public class ShadowMapTerrainRenderer {

    private Matrix4f projectionViewMatrix;
    private ShadowShader shader;

    protected ShadowMapTerrainRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
        this.shader = shader;
        this.projectionViewMatrix = projectionViewMatrix;
    }

    protected void render(Map<TexturedModel, List<TerrainChunk>> terrainChunks) {
        for (TexturedModel model : terrainChunks.keySet()) {
            Model rawModel = model.getModel();
            bindModel(rawModel);

            List<TerrainChunk> batch = terrainChunks.get(model);
            for (TerrainChunk terrainChunk : batch){
                prepareInstance(terrainChunk);
                GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getModel().getVertexCount() / 3);
            }
        }
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }


    private void bindModel(Model rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
    }

    private void prepareInstance(TerrainChunk terrainChunk) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(terrainChunk.getPosition(),
                0, 0, 0, 1);

        Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, transformationMatrix, null);
        shader.loadMvpMatrix(mvpMatrix);
    }

}
