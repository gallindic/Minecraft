package Inventory;

public class Item {
    private int blockType;
    private String blockName;

    public Item(int blockType, String blockName) {
        this.blockType = blockType;
        this.blockName = blockName;
    }

    public int getBlockType() {
        return blockType;
    }

    public String getBlockName() {
        return blockName;
    }
}
