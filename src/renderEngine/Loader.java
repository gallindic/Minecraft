package renderEngine;

import model.Model;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();
    private int vaoAtributesCount = 0;

    public Model loadToVAO(float[] vertices, int[] indices, float[] textureCoords){
        int vaoID = createAndBindVertexArray();

        bindIndicesBuffer(indices);
        storeDataInAttributesList(vaoAtributesCount++, 3, vertices);
        storeDataInAttributesList(vaoAtributesCount++, 2, textureCoords);

        unbindVertexArray();

        return new Model(vaoID, indices.length);
    }

    public void unbindVertexArray(){
        GL30.glBindVertexArray(0);
    }

    public int loadTexture(String fileName) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG",
                    new FileInputStream("assets/" + fileName + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ".png , didn't work");
            System.exit(-1);
        }

        textures.add(texture.getTextureID());

        return texture.getTextureID();
    }

    public void cleanUp(){
        for(int vbo : vbos){
            GL15.glDeleteBuffers(vbo);
        }

        for(int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }

        for(int texture : textures){
            GL11.glDeleteTextures(texture);
        }
    }

    public void enableVaoAttributes(){
        for (int i = 0; i < vaoAtributesCount; i++)
            GL20.glEnableVertexAttribArray(i);
    }

    public void disableVaoAttributes(){
        for (int i = 0; i < vaoAtributesCount; i++)
            GL20.glDisableVertexAttribArray(i);
    }

    private int createAndBindVertexArray(){
        int vao = GL30.glGenVertexArrays();
        vaos.add(vao);

        GL30.glBindVertexArray(vao);

        return vao;
    }

    private void storeDataInAttributesList(int attributeIndex, int dataSize, float[] data){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer bufferData = bufferData(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bufferData, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeIndex, dataSize, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer bufferData = intBufferData(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, bufferData, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer intBufferData(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private FloatBuffer bufferData(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }
}
