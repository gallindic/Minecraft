package block;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex {
    private Vector3f positions, normals;
    private Vector2f uvs;

    public Vertex(Vector3f positions, Vector3f normals, Vector2f uvs) {
        this.positions = positions;
        this.normals = normals;
        this.uvs = uvs;
    }

    public Vector3f getPositions() {
        return positions;
    }

    public Vector3f getNormals() {
        return normals;
    }

    public Vector2f getUvs() {
        return uvs;
    }
}
