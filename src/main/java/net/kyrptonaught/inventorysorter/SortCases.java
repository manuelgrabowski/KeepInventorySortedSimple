package net.kyrptonaught.inventorysorter;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortCases {
    static String getStringForSort(ItemStack stack, SortType sortType) {
        Item item = stack.getItem();
        String itemName = specialCases(stack);
        switch (sortType) {
            case CATEGORY -> {
                ItemGroup group = getFirstItemGroup(stack);
                return (group != null ? group.getDisplayName().getString() : "zzz") + itemName;
            }
            case MOD -> {
                return Registries.ITEM.getId(item).getNamespace() + itemName;
            }
            case NAME -> {
                return stack.getName() + itemName;
            }
        }


        return itemName;
    }

    private static ItemGroup getFirstItemGroup(ItemStack stack) {
        List<ItemGroup> groups = ItemGroups.getGroups();
        for (ItemGroup group : groups) {
            if (group.contains(new ItemStack(stack.getItem())))
                return group;

        }
        return null;
    }

    private static String specialCases(ItemStack stack) {
        Item item = stack.getItem();
        ComponentMap component = stack.getComponents();
        String sortString = item.toString();

        if (component != null && component.contains(DataComponentTypes.PROFILE))
            sortString = playerHeadCase(stack);
        if (stack.getCount() != stack.getMaxCount())
            sortString = stackSize(stack);
        if (item instanceof EnchantedBookItem)
            sortString = enchantedBookNameCase(stack);
        if (item instanceof ToolItem)
            sortString = toolDuribilityCase(stack);

        if (component != null && item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock){
            ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
            if (container != null){
                DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                container.copyTo(defaultedList);

                List<String> stringList = new ArrayList<>(27);
                for (ItemStack itemStack : defaultedList) {
                    String shulkerboxContentString = itemStack.getItem().toString();
                    // Ignore empty slots for sorting
                    if(!shulkerboxContentString.equals("minecraft:air")) {
                        stringList.add(shulkerboxContentString);
                    }
                }

                // group empty boxes at after all other shulkers
                if(stringList.isEmpty()) {
                    stringList.add("zzzEMPTYBOX");
                }

                sortString = "minecraft:shulker_box" // group all shulkerboxes together
                        + String.join(" ", stringList) // sort them by their content
                        + item; // sort boxes with identical content by their color
            }
        }

        return sortString;
    }

    private static String playerHeadCase(ItemStack stack) {
        ProfileComponent profileComponent = stack.getComponents().get(DataComponentTypes.PROFILE);
        String ownerName = profileComponent.name().isPresent() ? profileComponent.name().get() : stack.getItem().toString();

        // this is duplicated logic, so we should probably refactor
        String count = "";
        if (stack.getCount() != stack.getMaxCount()) {
            count = Integer.toString(stack.getCount());
        }

        return stack.getItem().toString() + " " + ownerName + count;
    }

    private static String stackSize(ItemStack stack) {
        return stack.getItem().toString() + stack.getCount();
    }

    private static String enchantedBookNameCase(ItemStack stack) {
        ItemEnchantmentsComponent enchantmentsComponent = stack.getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS);
        List<String> names = new ArrayList<>();
        StringBuilder enchantNames = new StringBuilder();
        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> enchant : enchantmentsComponent.getEnchantmentEntries()) {
            names.add(Enchantment.getName(enchant.getKey(), enchant.getIntValue()).getString());
        }
        Collections.sort(names);
        for (String enchant : names) {
            enchantNames.append(enchant).append(" ");
        }
        return stack.getItem().toString() + " " + enchantmentsComponent.getSize() + " " + enchantNames;
    }

    private static String toolDuribilityCase(ItemStack stack) {
        return stack.getItem().toString() + stack.getDamage();
    }

    public enum SortType {
        NAME, CATEGORY, MOD, ID;

        public String getTranslationKey() {
            return "key." + InventorySorterMod.MOD_ID + ".sorttype." + this.toString().toLowerCase();
        }
    }
}
