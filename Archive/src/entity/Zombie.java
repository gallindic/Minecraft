package entity;

import Collision.CollisionDetector;
import Collision.PlayerAABB;
import Collision.zombieAABB;
import model.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

public class Zombie extends Entity {

    private static final float RUN_SPEED = 15;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;

    private static float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0.01f;
    private float upwardsSpeed = 0;


    public static zombieAABB zombieAABB;
    private Player player;

    public Zombie(Player player, TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(texturedModel, position, rotX, rotY, rotZ, scale);

        zombieAABB = new zombieAABB(super.getPosition(), this);
        this.player = player;
    }

    public void setOrigin(Vector3f origin){
        super.setPosition(origin);
        TERRAIN_HEIGHT = origin.getY();
    }


    public void move(){
        float dx = Player.currentPlayerPosition.x - super.getPosition().x;
        float dz = Player.currentPlayerPosition.z- super.getPosition().z;
        zombieAABB.setWorldPosition(super.getPosition());

        float angle = (float) Math.toDegrees(Math.atan2(dx, dz));

        this.setRotY(angle);

        super.increasePosition(dx * currentSpeed, 0, dz * currentSpeed);
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed*DisplayManager.getFrameTimeSeconds(),0);
        if(super.getPosition().y < TERRAIN_HEIGHT){
            upwardsSpeed = 0;
            super.getPosition().y = TERRAIN_HEIGHT;
        }
    }
    public void setTerrainHeight(float height){
        TERRAIN_HEIGHT = height;
    }
}
