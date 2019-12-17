package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private int pitch;
    private int yaw;
    private int roll;

    public Camera(){}

    public void move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W))
            position.z -= 0.1f;
        if(Keyboard.isKeyDown(Keyboard.KEY_S))
            position.z += 0.1f;
        if(Keyboard.isKeyDown(Keyboard.KEY_A))
            position.x -= 0.1f;
        if(Keyboard.isKeyDown(Keyboard.KEY_D))
            position.x += 0.1f;
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            position.y += 0.1f;
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            position.y -= 0.1f;
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getPitch() {
        return pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public int getRoll() {
        return roll;
    }
}
