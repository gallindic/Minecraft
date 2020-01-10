package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    ShaderProgram(String vertexFile, String fragmentFile){
        this.vertexShaderID = readAndCompileShader(vertexFile, GL20.GL_VERTEX_SHADER);
        this.fragmentShaderID = readAndCompileShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        this.programID = createProgram();

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        getAllUniformLocations();
    }

    public void startProgram(){
        GL20.glUseProgram(programID);
    }

    public void stopProgram(){
        GL20.glUseProgram(0);
    }

    public void cleanUp(){
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);

        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);

        GL20.glDeleteProgram(programID);
    }

    public int getProgramID() {
        return programID;
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID,uniformName);
    }

    protected void loadMatrix(int location, Matrix4f matrix){
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    protected void loadVector(int location, Vector3f vector){
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadInt(int location, int value){
        GL20.glUniform1i(location, value);
    }

    private int createProgram(){
        return GL20.glCreateProgram();
    }

    private int readAndCompileShader(String shaderFile, int type){
        StringBuilder shader = new StringBuilder();

        try{
            BufferedReader reader = new BufferedReader(new FileReader(shaderFile));
            String line;
            while((line = reader.readLine())!=null)
                shader.append(line).append("//\n");

            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shader);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }

        return shaderID;
    }
}
