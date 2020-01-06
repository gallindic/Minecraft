package chunk;


import block.Block;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class Chunk {

    private List<Block> blocks;
    private Vector3f origin;

    public Chunk(List<Block> blocks, Vector3f origin) {
        this.blocks = blocks;
        this.origin = origin;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Vector3f getOrigin() {
        return origin;
    }
}
