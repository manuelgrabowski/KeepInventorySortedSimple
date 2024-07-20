package net.manu_faktur.kiss.client.config;

import blue.endless.jankson.Comment;
import com.google.common.collect.Sets;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.HashSet;

public class IgnoreList implements AbstractConfigFile {

    @Comment("The opened inventory's sort button will not be visible, only the player's when this inventory is opened")
    public HashSet<String> doNotSortList = new HashSet<>();

    @Comment("Neither the opened inventory, nor your player inventory will be sortable when this inventory is opened")
    public HashSet<String> hideSortBtnsList = new HashSet<>();

    public final transient HashSet<Identifier> defaultHideSortBtnsList = Sets.newHashSet(
            Identifier.of("guild:quest_screen"),
            Identifier.of("ae2:crystal_growth"),
            Identifier.of("ae2:advanced_inscriber"),
            Identifier.of("ae2:item_terminal"),
            Identifier.of("ae2:drive"),
            Identifier.of("ae2:patternterm"),
            Identifier.of("ae2:craftingterm"),
            Identifier.of("dankstorage:portable_dank_1"),
            Identifier.of("dankstorage:portable_dank_2"),
            Identifier.of("dankstorage:portable_dank_3"),
            Identifier.of("dankstorage:portable_dank_4"),
            Identifier.of("dankstorage:portable_dank_5"),
            Identifier.of("dankstorage:portable_dank_6"),
            Identifier.of("dankstorage:portable_dank_7")
    );

    public final transient HashSet<Identifier> defaultDoNotSortList = Sets.newHashSet(
            Registries.SCREEN_HANDLER.getId(ScreenHandlerType.CRAFTING),
            Identifier.of("adorn:trading_station"),
            Identifier.of("guild:quest_screen"),
            Identifier.of("conjuring:soulfire_forge"),
            Identifier.of("spectrum:pedestal"),
            Identifier.of("bankstorage:bank_1"),
            Identifier.of("bankstorage:bank_2"),
            Identifier.of("bankstorage:bank_3"),
            Identifier.of("bankstorage:bank_4"),
            Identifier.of("bankstorage:bank_5"),
            Identifier.of("bankstorage:bank_6"),
            Identifier.of("bankstorage:bank_7")
    );

    public boolean isDoNotSort(Identifier screenHandlerTypeID) {
        return isDoNotDisplay(screenHandlerTypeID) || doNotSortList.contains(screenHandlerTypeID.toString()) || defaultDoNotSortList.contains(screenHandlerTypeID);
    }

    public boolean isDoNotDisplay(Identifier screenHandlerTypeID) {
        return defaultHideSortBtnsList.contains(screenHandlerTypeID) || hideSortBtnsList.contains(screenHandlerTypeID.toString());
    }
}
