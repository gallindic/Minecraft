package Collision;

import entity.Player;
import entity.Zombie;
import org.lwjgl.util.vector.Vector3f;

public class zombieAABB extends AABB {

    private Zombie zombie;

    public zombieAABB(Vector3f worldPosition, Zombie zombie) {
        super(worldPosition);
        this.zombie = zombie;

        setX();
        setY();
        setZ();
    }

    public Zombie getZombie() {
        return zombie;
    }

    public void setWorldPosition(Vector3f position){
        super.setWorldPosition(position);
    }


    public void setX() {
        super.setxSize(1.0f);
    }

    public void setY() {
        super.setySize(2.0f);
    }

    public void setZ(){
        super.setzSize(0.5f);
    }
}
