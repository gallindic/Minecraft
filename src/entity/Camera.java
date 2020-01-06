package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private float distanceFromPlayer = 6;
    private boolean personState1st;
    public static Vector3f camPos;

    private Vector3f position;
    private int pitch = 20;
    private int yaw = 0;

    private Player player;

    public Camera(Vector3f position, Player player){
        this.position = position;
        camPos = position;
        this.player = player;

        initCamera();
    }

    private void initCamera(){
        Mouse.setGrabbed(true);
        personState1st = true;
    }

    public void move(){
        keyPress();
        cameraRotation();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance,verticalDistance);
        this.yaw = (int) (180 - (player.getRotY()));

    }

    private void cameraRotation(){
        float dx = Mouse.getDX();
        float mouseSensitivity = 0.25f;
        updateYaw(dx * mouseSensitivity);

        if(personState1st) {
            float dy = -Mouse.getDY();
            updatePitch(dy * mouseSensitivity);
        }

    }

    private void keyPress(){
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            Mouse.setGrabbed(false);

        //3rd Person
        if(Keyboard.isKeyDown(Keyboard.KEY_5) && personState1st) {
            personState1st = false;
        }
        //1st Person
        else if(Keyboard.isKeyDown(Keyboard.KEY_4) && !personState1st){
            personState1st = true;
        }
    }

    private void updateYaw(float amount){
        player.increaseRotation(0, amount, 0);
    }

    private void updatePitch(float amount){
        this.pitch += amount;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        camPos = position;
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

    private void calculateCameraPosition(float horizonDistance, float verticalDistance){
        float theta = player.getRotY();
        float offsetX = (float) (horizonDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizonDistance * Math.cos(Math.toRadians(theta)));

        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer* Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer* Math.sin(Math.toRadians(pitch)));
    }
}
