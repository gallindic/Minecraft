package vegetation;

import org.lwjgl.util.vector.Vector3f;

public abstract class Vegetation {
    public static float[] vertices = {
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f, 0.5f, 0.0f,
        -0.5f, 0.5f, 0.0f,

        0.0f, 0.5f, -0.5f,
        0.0f, -0.5f, -0.5f,
        0.0f, -0.5f, 0.5f,
        0.0f, 0.5f, 0.5f,
    };

    public static int[] indices = {
        0, 1, 3,
        3, 1, 2,

        4, 5, 7,
        7, 5, 6,
    };

    public static float[] uvs = {
        11.f / 16.f, 1.f / 16f,
        12.f / 16.f, 1.f / 16f,
        12.f / 16.f, 0.f,
        11.f / 16.f, 0.f,

        11.f / 16.f, 0.f,
        11.f / 16.f, 1.f / 16f,
        12.f / 16.f, 1.f / 16f,
        12.f / 16.f, 0.f,
    };

    public abstract Vector3f getOrigin();
}
