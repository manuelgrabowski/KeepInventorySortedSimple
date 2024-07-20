package net.kyrptonaught.inventorysorter.mixin;

import net.kyrptonaught.inventorysorter.InventoryHelper;
import net.kyrptonaught.inventorysorter.interfaces.InvSorterPlayer;
import net.kyrptonaught.inventorysorter.interfaces.SortableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class MixinContainer implements SortableContainer {
    @Shadow
    @Final
    public DefaultedList<Slot> slots;

    @Shadow
    private ItemStack cursorStack;


    @Override
    public Inventory getInventory() {
        if (!hasSlots()) return null;
        return this.slots.get(0).inventory;
    }

    @Override
    public boolean hasSlots() {
        return !this.slots.isEmpty();
    }
}