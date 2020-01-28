package terrain;

import model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class TerrainChunk {
    private TexturedModel texturedModel;
    private Vector3f position;

    public TerrainChunk(TexturedModel texturedModel, Vector3f position) {
        this.texturedModel = texturedModel;
        this.position = position;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }
}
