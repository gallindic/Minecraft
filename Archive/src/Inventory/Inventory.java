package Inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private static List<Item> inventoryItems = new ArrayList<>();

    public static void addItem(Item item){
        inventoryItems.add(item);
    }

    public static List<Item> getInventoryItems() {
        return inventoryItems;
    }
}
