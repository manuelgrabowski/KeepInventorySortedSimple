package net.manu_faktur.kiss.client.config;

import net.manu_faktur.kiss.SortCases;
import eu.midnightdust.lib.config.MidnightConfig;

public class KissConfig extends MidnightConfig {
    public static final String KISS = "KISS";

    @Entry(category = KISS) public static boolean sortMouseHoveredInventory = true;
    @Entry(category = KISS) public static boolean alsoSortPlayerInventory = false;
    @Entry(category = KISS) public static SortCases.SortType sortType = SortCases.SortType.ID;
    @Comment(category = KISS) public static Comment spacer1;
    @Comment(category = KISS, centered = true) public static Comment deprecatedTitle;
    @Comment(category = KISS) public static Comment deprecatedNote;
    @Entry(category = KISS) public static boolean displaySortButtonInventories = false;
    @Entry(category = KISS) public static boolean displaySortButtonPlayerInventory = false;
    @Entry(category = KISS) public static boolean displayTooltip = false;


}
