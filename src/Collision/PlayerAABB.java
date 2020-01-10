package Collision;

import entity.Player;
import org.lwjgl.util.vector.Vector3f;

public class PlayerAABB extends AABB {

    private Player player;

    public PlayerAABB(Vector3f worldPosition, Player player) {
        super(worldPosition);
        this.player = player;

        setX();
        setY();
        setZ();
    }

    public Player getPlayer() {
        return player;
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
