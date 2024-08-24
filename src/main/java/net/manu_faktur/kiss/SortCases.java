package net.manu_faktur.kiss;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortCases {
    static String getStringForSort(ItemStack stack, SortType sortType) {
        return getSortString(stack, sortType) + stackSize(stack);
    }

    private static ItemGroup getFirstItemGroup(ItemStack stack) {
        List<ItemGroup> groups = ItemGroups.getGroups();
        for (ItemGroup group : groups) {
            if (group.contains(new ItemStack(stack.getItem())))
                return group;

        }
        return null;
    }

    private static String getSortBase(ItemStack stack, SortType sortType) {
        Item item = stack.getItem();
        String sortBaseString = item.toString();

        switch (sortType) {
            case CATEGORY -> {
                ItemGroup group = getFirstItemGroup(stack);
                sortBaseString = (group != null ? group.getDisplayName().getString() : "zzz");
            }
            case MOD -> sortBaseString = Registries.ITEM.getId(item).getNamespace();
            case NAME -> sortBaseString = stack.getName().toString();
        }

        return sortBaseString;
    }

    private static String getSortString(ItemStack stack, SortType sortType) {
        String sortString = getSortBase(stack, sortType);
        Item item = stack.getItem();
        ComponentMap component = stack.getComponents();

        if (component != null && component.contains(DataComponentTypes.PROFILE))
            sortString = playerHeadCase(stack, sortType);
        if (item instanceof EnchantedBookItem)
            sortString = enchantedBookNameCase(stack, sortType);
        if (item instanceof ToolItem)
            sortString = toolDurabilityCase(stack, sortType);
        if (component != null && item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock){
            sortString = shulkerBoxCase(stack, sortType);
        }

        return sortString;
    }

    private static String shulkerBoxCase(ItemStack stack, SortType sortType) {
        ContainerComponent container = stack.get(DataComponentTypes.CONTAINER);
        if (container != null) {
            DefaultedList<ItemStack> shulkerContentCopy = DefaultedList.ofSize(27, ItemStack.EMPTY);
            container.copyTo(shulkerContentCopy);

            List<String> shulkerContentSortStrings = new ArrayList<>(27);
            for (ItemStack currentShulkerContentStack : shulkerContentCopy) {
                // Ignore empty slots for sorting
                if(!currentShulkerContentStack.getItem().toString().equals("minecraft:air")) {
                    shulkerContentSortStrings.add(getSortString(currentShulkerContentStack, sortType));
                }
            }

            // group empty shulkerboxes after all other shulkerboxes
            if(shulkerContentSortStrings.isEmpty()) {
                shulkerContentSortStrings.add("zzz_EmptyShulkerBox");
            }

            return "AAA_ShulkerBox" // group all shulkerboxes together at the front
                    + String.join(" ", shulkerContentSortStrings) // sort them by their content
                    + getSortBase(stack, sortType); // sort boxes with identical content by their color
        }

        // Fallback
        return getSortBase(stack, sortType);
    }

    private static String playerHeadCase(ItemStack stack, SortType sortType) {
        ProfileComponent profileComponent = stack.getComponents().get(DataComponentTypes.PROFILE);
        if (profileComponent != null && profileComponent.name().isPresent()) {
            return profileComponent.name().get();
        }

        return getSortBase(stack, sortType);
    }

    private static String stackSize(ItemStack stack) {
        String stackSize = "";

        if (stack.getCount() != stack.getMaxCount())
            stackSize = String.valueOf(stack.getCount());

        return stackSize;
    }

    private static String enchantedBookNameCase(ItemStack stack, SortType sortType) {
        ItemEnchantmentsComponent enchantmentsComponent = stack.getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS);
        List<String> names = new ArrayList<>();
        StringBuilder enchantNames = new StringBuilder();
        if (enchantmentsComponent != null) {
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> enchant : enchantmentsComponent.getEnchantmentEntries()) {
                names.add(Enchantment.getName(enchant.getKey(), enchant.getIntValue()).getString());
            }

            Collections.sort(names);
            for (String enchant : names) {
                enchantNames.append(enchant).append(" ");
            }
            return getSortBase(stack, sortType) + " " + enchantmentsComponent.getSize() + " " + enchantNames;
        }

        // Fallback
        return getSortBase(stack, sortType);
    }

    private static String toolDurabilityCase(ItemStack stack, SortType sortType) {
        return getSortBase(stack, sortType) + stack.getDamage();
    }

    public enum SortType {
        NAME, CATEGORY, MOD, ID;

        public String getTranslationKey() {
            return "net.manu_faktur." + KeepInventorySortedSimple.MOD_ID + ".sorttype." + this.toString().toLowerCase();
        }
    }
}
