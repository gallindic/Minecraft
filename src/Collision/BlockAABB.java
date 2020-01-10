package Collision;

import block.Block;
import org.lwjgl.util.vector.Vector3f;

public class BlockAABB extends AABB{

    private Block block;

    public BlockAABB(Vector3f worldPosition, Block block) {
        super(worldPosition);
        this.block = block;

        setX();
        setY();
        setZ();
    }

    public void setX(){
        super.setxSize(1.0f);
    }

    public void setY(){
        super.setySize(1.0f);
    }

    public void setZ(){
        super.setzSize(1.0f);
    }

    public Block getBlock() {
        return block;
    }
}
