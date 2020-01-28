package Collision;

import block.Block;

import java.util.List;

public class CollisionDetector {

    public static void checkCollisions(PlayerAABB playerAABB, zombieAABB zombieAABB, List<BlockAABB> blockAABBList) {
        for (int i = 0; i < blockAABBList.size(); i++) {
            if (blockAABBList.get(i).getWorldPosition().x <= zombieAABB.getWorldPosition().x + 5 &&
                    blockAABBList.get(i).getWorldPosition().x >= zombieAABB.getWorldPosition().x - 5 &&
                    blockAABBList.get(i).getWorldPosition().z <= zombieAABB.getWorldPosition().z + 5 &&
                    blockAABBList.get(i).getWorldPosition().z >= zombieAABB.getWorldPosition().z - 5) {

                if (Math.floor(zombieAABB.getWorldPosition().x - zombieAABB.getxSize() / 2) >= Math.floor(blockAABBList.get(i).getWorldPosition().x - blockAABBList.get(i).getxSize() / 2) &&
                        Math.floor(zombieAABB.getWorldPosition().x + zombieAABB.getxSize() / 2) <= Math.floor(blockAABBList.get(i).getWorldPosition().x + blockAABBList.get(i).getxSize() / 2) &&
                        Math.floor(zombieAABB.getWorldPosition().z - zombieAABB.getzSize() / 2) >= Math.floor(blockAABBList.get(i).getWorldPosition().z - blockAABBList.get(i).getzSize() / 2) &&
                        Math.floor(zombieAABB.getWorldPosition().z + zombieAABB.getzSize() / 2) <= Math.floor(blockAABBList.get(i).getWorldPosition().z + blockAABBList.get(i).getzSize() / 2)) {

                    zombieAABB.getZombie().setTerrainHeight(blockAABBList.get(i).getWorldPosition().y + 1.5f);
                }
            }


            if (blockAABBList.get(i).getWorldPosition().x <= playerAABB.getWorldPosition().x + 5 &&
                    blockAABBList.get(i).getWorldPosition().x >= playerAABB.getWorldPosition().x - 5 &&
                    blockAABBList.get(i).getWorldPosition().z <= playerAABB.getWorldPosition().z + 5 &&
                    blockAABBList.get(i).getWorldPosition().z >= playerAABB.getWorldPosition().z - 5) {

                if (Math.floor(playerAABB.getWorldPosition().x - playerAABB.getxSize() / 2) >= Math.floor(blockAABBList.get(i).getWorldPosition().x - blockAABBList.get(i).getxSize() / 2) &&
                        Math.floor(playerAABB.getWorldPosition().x + playerAABB.getxSize() / 2) <= Math.floor(blockAABBList.get(i).getWorldPosition().x + blockAABBList.get(i).getxSize() / 2) &&
                        Math.floor(playerAABB.getWorldPosition().z - playerAABB.getzSize() / 2) >= Math.floor(blockAABBList.get(i).getWorldPosition().z - blockAABBList.get(i).getzSize() / 2) &&
                        Math.floor(playerAABB.getWorldPosition().z + playerAABB.getzSize() / 2) <= Math.floor(blockAABBList.get(i).getWorldPosition().z + blockAABBList.get(i).getzSize() / 2)) {

                    if(blockAABBList.get(i).getBlock().getType() != Block.TREEBARK) {
                        playerAABB.getPlayer().setTerrainHeight(blockAABBList.get(i).getWorldPosition().y + 1.5f);
                    }
                }
            }
        }
    }
}
