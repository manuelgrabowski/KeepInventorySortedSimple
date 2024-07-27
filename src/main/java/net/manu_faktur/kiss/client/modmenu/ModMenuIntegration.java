package net.manu_faktur.kiss.client.modmenu;

import com.google.common.collect.Sets;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.SortCases;
import net.manu_faktur.kiss.client.KeepInventorySortedSimpleClient;
import net.manu_faktur.kiss.client.config.ConfigOptions;
import net.manu_faktur.kiss.client.config.IgnoreList;
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
            ConfigOptions options = KeepInventorySortedSimpleClient.getConfig();
            IgnoreList ignoreList = KeepInventorySortedSimple.getIgnoreList();
            ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("net.manu_faktur.kiss.config"));
            configScreen.setSavingEvent(() -> {
                KeepInventorySortedSimple.configManager.save();
                if (MinecraftClient.getInstance().player != null)
                    KeepInventorySortedSimpleClient.syncConfig();
            });

            ConfigSection displaySection = new ConfigSection(configScreen, Text.translatable("net.manu_faktur.kiss.config.category.display"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("net.manu_faktur.kiss.config.displaysort"), options.displaySort, true).setSaveConsumer(val -> options.displaySort = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("net.manu_faktur.kiss.config.seperatebtn"), options.separateBtn, true).setSaveConsumer(val -> options.separateBtn = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("net.manu_faktur.kiss.config.displaytooltip"), options.displayTooltip, true).setSaveConsumer(val -> options.displayTooltip = val));

            ConfigSection logicSection = new ConfigSection(configScreen, Text.translatable("net.manu_faktur.kiss.config.category.logic"));
            logicSection.addConfigItem(new EnumItem<>(Text.translatable("net.manu_faktur.kiss.config.sorttype"), SortCases.SortType.values(), options.sortType, SortCases.SortType.NAME).setSaveConsumer(val -> options.sortType = val));
            logicSection.addConfigItem(new BooleanItem(Text.translatable("net.manu_faktur.kiss.config.sortplayer"), options.sortPlayer, false).setSaveConsumer(val -> options.sortPlayer = val));

            ConfigSection activationSection = new ConfigSection(configScreen, Text.translatable("net.manu_faktur.kiss.config.category.activation"));
            activationSection.addConfigItem(new KeybindItem(Text.translatable("net.manu_faktur.kiss.sortPrimary"), options.keybindingPrimary.rawKey, "key.mouse.middle").setSaveConsumer(key -> options.keybindingPrimary.setRaw(key)));
            activationSection.addConfigItem(new KeybindItem(Text.translatable("net.manu_faktur.kiss.sortSecondary"), options.keybindingSecondary.rawKey, "key.keyboard.r").setSaveConsumer(key -> options.keybindingSecondary.setRaw(key)));
            activationSection.addConfigItem(new BooleanItem(Text.translatable("net.manu_faktur.kiss.config.sortmousehighlighted"), options.sortMouseHighlighted, true).setSaveConsumer(val -> options.sortMouseHighlighted = val));

            ConfigSection ignoreListSection = new ConfigSection(configScreen, Text.translatable("net.manu_faktur.kiss.config.category.ignorelist"));

            ignoreListSection.addConfigItem(new BooleanItem(Text.translatable("net.manu_faktur.kiss.config.showdebug"), options.debugMode, false).setSaveConsumer(val -> options.debugMode = val).setToolTipWithNewLine("net.manu_faktur.kiss.config.debugtooltip"));

            StringList hideList = (StringList) new StringList(Text.translatable("net.manu_faktur.kiss.config.hidesort"), ignoreList.hideSortButtonList.stream().toList(), new ArrayList<>()).setSaveConsumer(val -> ignoreList.hideSortButtonList = Sets.newHashSet(val)).setToolTipWithNewLine("net.manu_faktur.kiss.config.hidetooltip");
            StringList nosortList = (StringList) new StringList(Text.translatable("net.manu_faktur.kiss.config.nosort"), ignoreList.doNotSortList.stream().toList(), new ArrayList<>()).setSaveConsumer(val -> ignoreList.doNotSortList = Sets.newHashSet(val)).setToolTipWithNewLine("net.manu_faktur.kiss.config.nosorttooltip");

            ignoreListSection.addConfigItem(hideList);
            ignoreListSection.addConfigItem(nosortList);
            return configScreen;
        };
    }
}
