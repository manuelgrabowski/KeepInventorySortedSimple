package net.manu_faktur.kiss.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.client.config.ConfigOptions;
import net.manu_faktur.kiss.network.SyncIgnoreListPacket;
import net.manu_faktur.kiss.network.SyncInvSortSettingsPacket;
import net.kyrptonaught.kyrptconfig.keybinding.DisplayOnlyKeyBind;
import net.minecraft.client.util.InputUtil;

public class KeepInventorySortedSimpleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeepInventorySortedSimple.configManager.registerFile("config.json5", new ConfigOptions());
        KeepInventorySortedSimple.configManager.load();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> syncConfig());
        SyncIgnoreListPacket.registerReceiveIgnoreList();

        KeyBindingHelper.registerKeyBinding(new DisplayOnlyKeyBind(
                "net.manu_faktur.kiss.sortPrimary",
                "net.manu_faktur.kiss.name",
                getConfig().keybindingPrimary,
                setKey -> KeepInventorySortedSimple.configManager.save()
        ));

        KeyBindingHelper.registerKeyBinding(new DisplayOnlyKeyBind(
                "net.manu_faktur.kiss.sortSecondary",
                "net.manu_faktur.kiss.name",
                getConfig().keybindingSecondary,
                setKey -> KeepInventorySortedSimple.configManager.save()
        ));
    }

    public static void syncConfig() {
        SyncInvSortSettingsPacket.registerSyncOnPlayerJoin();
    }

    public static ConfigOptions getConfig() {
        return (ConfigOptions) KeepInventorySortedSimple.configManager.getConfig("config.json5");
    }

    public static boolean isKeyBindingPressed(int pressedKeyCode, InputUtil.Type type) {
        return getConfig().keybindingPrimary.matches(pressedKeyCode, type) || getConfig().keybindingSecondary.matches(pressedKeyCode, type);
    }
}
