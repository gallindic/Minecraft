package renderEngine;

import model.Model;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
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

    public Model loadToVAO(float[] vertices, float[] textureCoords){
        int vaoID = createAndBindVertexArray();

        storeDataInAttributesList(0, 3, vertices);
        storeDataInAttributesList(1, 2, textureCoords);

        unbindVertexArray();

        return new Model(vaoID, vertices.length);
    }

    public Model loadToVAO(float[] vertices, int[] indices, float[] textureCoords){
        int vaoID = createAndBindVertexArray();

        bindIndicesBuffer(indices);
        storeDataInAttributesList(0, 3, vertices);
        storeDataInAttributesList(1, 2, textureCoords);

        unbindVertexArray();

        return new Model(vaoID, indices.length);
    }

    public Model loadToVAO(float[] vertices, int[] indices, float[] textureCoords, float[] positions){
        int vaoID = createAndBindVertexArray();

        bindIndicesBuffer(indices);
        storeDataInAttributesList(0, 3, vertices);
        storeDataInAttributesList(1, 2, textureCoords);
        storeDataInAttributesList(2, 3, positions);
        GL33.glVertexAttribDivisor(2, 1);

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
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -4);
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
