package net.manu_faktur.kiss.interfaces;

import net.minecraft.inventory.Inventory;

public interface SortableContainer {
    Inventory getInventory();

    boolean hasSlots();

}
