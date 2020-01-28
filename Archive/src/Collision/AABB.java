package Collision;

import org.lwjgl.util.vector.Vector3f;

public class AABB {
    private Vector3f worldPosition;
    private float ySize;
    private float xSize;
    private float zSize;

    public AABB(Vector3f worldPosition) {
        this.worldPosition = worldPosition;
    }

    public void setySize(float ySize) {
        this.ySize = ySize;
    }

    public void setxSize(float xSize) {
        this.xSize = xSize;
    }

    public void setzSize(float zSize) {
        this.zSize = zSize;
    }

    public void setWorldPosition(Vector3f worldPosition) {
        this.worldPosition = worldPosition;
    }

    public Vector3f getWorldPosition() {
        return worldPosition;
    }

    public float getySize() {
        return ySize;
    }

    public float getxSize() {
        return xSize;
    }

    public float getzSize() {
        return zSize;
    }
}
