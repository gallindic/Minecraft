package entity;

import Collision.CollisionDetector;
import Collision.PlayerAABB;
import model.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class Player extends Entity {

    private static final float RUN_SPEED = 15;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 15;

    private static float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private boolean isInAir = false;

    public static Vector3f currentPlayerPosition;

    public static PlayerAABB playerAABB;

    public Player(TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(texturedModel, position, rotX, rotY, rotZ, scale);

        currentPlayerPosition = super.getPosition();
        playerAABB = new PlayerAABB(super.getPosition(), this);
    }

    public void setPlayerOrigin(Vector3f origin){
        super.setPosition(origin);
        playerAABB.setWorldPosition(origin);
        setTerrainHeight(origin.y);
    }

    public void setTerrainHeight(float height){
        TERRAIN_HEIGHT = height;
    }

    public void move(){
        checkInputs();
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        currentPlayerPosition = super.getPosition();
        playerAABB.setWorldPosition(super.getPosition());

        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed*DisplayManager.getFrameTimeSeconds(),0);
        if(super.getPosition().y < TERRAIN_HEIGHT){
            upwardsSpeed = 0;
            isInAir = false;
            super.getPosition().y = TERRAIN_HEIGHT;
        }
    }


    public void jump(){
        if(!isInAir){
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void  checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = RUN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        }else{
            this.currentSpeed = 0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }
}
