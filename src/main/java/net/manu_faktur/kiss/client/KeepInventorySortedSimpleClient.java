package net.manu_faktur.kiss.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.network.SyncInvSortSettingsPacket;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeepInventorySortedSimpleClient implements ClientModInitializer {
    public static KeyBinding primaryKeyBinding;
    public static KeyBinding secondaryKeyBinding;

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> syncConfig());

        primaryKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "net.manu_faktur.kiss.sortPrimary",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "net.manu_faktur.kiss.name"
        ));

        secondaryKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "net.manu_faktur.kiss.sortSecondary",
                InputUtil.Type.MOUSE,
                GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
                "net.manu_faktur.kiss.name"
        ));
    }

    public static void syncConfig() {
        SyncInvSortSettingsPacket.registerSyncOnPlayerJoin();
    }

    public static boolean isKeyBindingPressed(int pressedKeyCode, InputUtil.Type type) {
        KeepInventorySortedSimple.LOGGER.info("Pressed " + pressedKeyCode);
        return primaryKeyBinding.matchesKey(pressedKeyCode, type.ordinal())  || secondaryKeyBinding.matchesKey(pressedKeyCode, type.ordinal()) ||
                primaryKeyBinding.matchesMouse(pressedKeyCode)  || secondaryKeyBinding.matchesMouse(pressedKeyCode);
    }
}
