package entity;

import Collision.CollisionDetector;
import engineTester.Minecraft;
import org.lwjgl.opengl.Display;
import terrain.Terrain;

import java.util.List;

public class EntityController {

    private Player player;
    private Zombie zombie;
    private Camera camera;

    public EntityController(Player player, Zombie zombie, Camera camera) {
        this.player = player;
        this.zombie = zombie;
        this.camera = camera;
    }

    public void gameLogic(){
        camera.move();
        player.move();
        zombie.move();

        if(Math.floor(Zombie.zombieAABB.getWorldPosition().x - Zombie.zombieAABB.getxSize() / 2) >= Math.floor(Player.playerAABB.getWorldPosition().x - Player.playerAABB.getxSize() / 2) &&
                Math.floor(Zombie.zombieAABB.getWorldPosition().x + Zombie.zombieAABB.getxSize() / 2) <= Math.floor(Player.playerAABB.getWorldPosition().x + Player.playerAABB.getxSize() / 2) &&
                Math.floor(Zombie.zombieAABB.getWorldPosition().z - Zombie.zombieAABB.getzSize() / 2) >= Math.floor(Player.playerAABB.getWorldPosition().z - Player.playerAABB.getzSize() / 2) &&
                Math.floor(Zombie.zombieAABB.getWorldPosition().z + Zombie.zombieAABB.getzSize() / 2) <= Math.floor(Player.playerAABB.getWorldPosition().z + Player.playerAABB.getzSize() / 2)) {
            end();
        }
    }

    private void end(){
        Minecraft.endGame = true;
    }

    public void collisonChecker(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Display.isCloseRequested() && !Minecraft.endGame){
                    CollisionDetector.checkCollisions(Player.playerAABB, Zombie.zombieAABB, Terrain.blockAABBList);
                }
            }
        }).start();
    }
}
