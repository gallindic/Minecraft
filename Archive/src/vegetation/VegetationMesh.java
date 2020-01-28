package vegetation;

import model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class VegetationMesh {
    private TexturedModel texturedModel;
    private Vector3f position;
    private int instanceCount;

    public VegetationMesh(TexturedModel texturedModel, Vector3f position, int instanceCount) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.instanceCount = instanceCount;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getInstanceCount() {
        return instanceCount;
    }
}
