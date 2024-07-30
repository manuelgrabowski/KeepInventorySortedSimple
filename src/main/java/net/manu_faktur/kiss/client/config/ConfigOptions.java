package net.manu_faktur.kiss.client.config;

import blue.endless.jankson.Comment;
import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.SortCases;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.kyrptonaught.kyrptconfig.keybinding.CustomKeyBinding;

public class ConfigOptions implements AbstractConfigFile {

    @Comment("Enable 'Sort' button in inventories")
    public boolean displaySort = true;
    @Comment("Enable second 'Sort' button in player inventory")
    public boolean separateBtn = true;
    @Comment("Sorting also sorts player inventory")
    public boolean sortPlayer = false;
    @Comment("Method of sorting: NAME,CATEGORY,MOD,ID")
    public SortCases.SortType sortType = SortCases.SortType.ID;
    @Comment("Display Sort Button Tooltip")
    public boolean displayTooltip = true;

    @Comment("Sort Inventory primary keybinding")
    public final CustomKeyBinding keybindingPrimary = CustomKeyBinding.configDefault(KeepInventorySortedSimple.MOD_ID, "key.mouse.middle");

    @Comment("Sort Inventory secondary keybinding")
    public final CustomKeyBinding keybindingSecondary = CustomKeyBinding.configDefault(KeepInventorySortedSimple.MOD_ID, "key.keyboard.r");

    @Comment("Sort the inventory that the mouse cursor is hovering over")
    public Boolean sortMouseHighlighted = true;

    @Comment("Show extra info about ignore list in tooltip")
    public boolean debugMode = false;

}
