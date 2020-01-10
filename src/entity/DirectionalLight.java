package entity;

import org.lwjgl.util.vector.Vector3f;

public class DirectionalLight {

    private Vector3f color;
    private Vector3f direction;

    public DirectionalLight(Vector3f color, Vector3f direction) {
        this.color = color;
        this.direction = direction;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }
}
