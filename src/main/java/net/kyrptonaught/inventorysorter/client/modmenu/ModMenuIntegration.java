package net.kyrptonaught.inventorysorter.client.modmenu;

import com.google.common.collect.Sets;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.inventorysorter.client.InventorySorterModClient;
import net.kyrptonaught.inventorysorter.client.config.ConfigOptions;
import net.kyrptonaught.inventorysorter.client.config.IgnoreList;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.*;
import net.kyrptonaught.kyrptconfig.config.screen.items.lists.StringList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            ConfigOptions options = InventorySorterModClient.getConfig();
            IgnoreList ignoreList = InventorySorterMod.getBlackList();
            ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("key.inventorysorter.config"));
            configScreen.setSavingEvent(() -> {
                InventorySorterMod.configManager.save();
                if (MinecraftClient.getInstance().player != null)
                    InventorySorterModClient.syncConfig();
            });

            ConfigSection displaySection = new ConfigSection(configScreen, Text.translatable("key.inventorysorter.config.category.display"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.displaysort"), options.displaySort, true).setSaveConsumer(val -> options.displaySort = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.seperatebtn"), options.seperateBtn, true).setSaveConsumer(val -> options.seperateBtn = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.displaytooltip"), options.displayTooltip, true).setSaveConsumer(val -> options.displayTooltip = val));

            ConfigSection logicSection = new ConfigSection(configScreen, Text.translatable("key.inventorysorter.config.category.logic"));
            logicSection.addConfigItem(new EnumItem<>(Text.translatable("key.inventorysorter.config.sorttype"), SortCases.SortType.values(), options.sortType, SortCases.SortType.NAME).setSaveConsumer(val -> options.sortType = val));
            logicSection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.sortplayer"), options.sortPlayer, false).setSaveConsumer(val -> options.sortPlayer = val));

            ConfigSection activationSection = new ConfigSection(configScreen, Text.translatable("key.inventorysorter.config.category.activation"));
            activationSection.addConfigItem(new KeybindItem(Text.translatable("key.inventorysorter.sort"), options.keybinding.rawKey, "key.keyboard.p").setSaveConsumer(key -> options.keybinding.setRaw(key)));
            activationSection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.middleclick"), options.middleClick, true).setSaveConsumer(val -> options.middleClick = val));
            activationSection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.doubleclick"), options.doubleClickSort, true).setSaveConsumer(val -> options.doubleClickSort = val));
            activationSection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.sortmousehighlighted"), options.sortMouseHighlighted, true).setSaveConsumer(val -> options.sortMouseHighlighted = val));

            ConfigSection blackListSection = new ConfigSection(configScreen, Text.translatable("key.inventorysorter.config.category.blacklist"));

            blackListSection.addConfigItem(new BooleanItem(Text.translatable("key.inventorysorter.config.showdebug"), options.debugMode, false).setSaveConsumer(val -> options.debugMode = val).setToolTipWithNewLine("key.inventorysorter.config.debugtooltip"));

            TextItem blackListURL = (TextItem) new TextItem(Text.translatable("key.inventorysorter.config.blacklistURL"), ignoreList.blacklistDownloadURL, IgnoreList.DOWNLOAD_URL).setMaxLength(1024).setSaveConsumer(val -> ignoreList.blacklistDownloadURL = val);
            blackListSection.addConfigItem(blackListURL);

            StringList hideList = (StringList) new StringList(Text.translatable("key.inventorysorter.config.hidesort"), ignoreList.hideSortBtnsList.stream().toList(), new ArrayList<>()).setSaveConsumer(val -> ignoreList.hideSortBtnsList = Sets.newHashSet(val)).setToolTipWithNewLine("key.inventorysorter.config.hidetooltip");
            StringList nosortList = (StringList) new StringList(Text.translatable("key.inventorysorter.config.nosort"), ignoreList.doNotSortList.stream().toList(), new ArrayList<>()).setSaveConsumer(val -> ignoreList.doNotSortList = Sets.newHashSet(val)).setToolTipWithNewLine("key.inventorysorter.config.nosorttooltip");

            blackListSection.addConfigItem(new ButtonItem(Text.translatable("key.inventorysorter.config.downloadListButton")).setClickEvent(() -> {
                blackListURL.save();
                ignoreList.downloadList();
                hideList.setValue(ignoreList.hideSortBtnsList.stream().toList());
                nosortList.setValue(ignoreList.doNotSortList.stream().toList());
            }));

            blackListSection.addConfigItem(hideList);
            blackListSection.addConfigItem(nosortList);
            return configScreen;
        };
    }
}
