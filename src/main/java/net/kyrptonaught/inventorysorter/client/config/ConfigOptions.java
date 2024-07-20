package net.kyrptonaught.inventorysorter.client.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.kyrptonaught.kyrptconfig.keybinding.CustomKeyBinding;

public class ConfigOptions implements AbstractConfigFile {

    @Comment("Enable 'Sort' button in inventorys")
    public boolean displaySort = true;
    @Comment("Enable second 'Sort' button in player inv")
    public boolean seperateBtn = true;
    @Comment("Sorting inv also sorts player inv")
    public boolean sortPlayer = false;
    @Comment("Method of sorting, NAME,CATEGORY,MOD")
    public SortCases.SortType sortType = SortCases.SortType.NAME;
    @Comment("Display Sort Button Tooltip")
    public boolean displayTooltip = true;

    @Comment("Sort Inventory keybinding")
    public final CustomKeyBinding keybinding = CustomKeyBinding.configDefault(InventorySorterMod.MOD_ID, "key.mouse.middle");

    @Comment("Should sort half of open inv highlighted by mouse")
    public Boolean sortMouseHighlighted = true;

    @Comment("Show extra info about ignore list in tooltip")
    public boolean debugMode = false;

}
