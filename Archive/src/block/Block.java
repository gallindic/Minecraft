package block;

import model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Block {
    private Vector3f origin;
    private int type;

    //private TexturedModel texturedModel;

    public static int GRASS = 0;
    public static int DIRT = 1;
    public static int STONE = 2;
    public static int TREEBARK = 3;
    public static int TREELEAF = 4;
    public static int SAND = 5;

    public Block(Vector3f origin, int type) {
        this.origin = origin;
        this.type = type;
        //this.texturedModel = texturedModel;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public int getType() {
        return type;
    }
}
