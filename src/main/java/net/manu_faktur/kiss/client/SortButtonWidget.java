package net.manu_faktur.kiss.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.manu_faktur.kiss.InventoryHelper;
import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.SortCases;
import net.manu_faktur.kiss.network.InventorySortPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.registry.Registries;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SortButtonWidget extends TexturedButtonWidget {
    private static final ButtonTextures TEXTURES = new ButtonTextures(
            Identifier.of(KeepInventorySortedSimple.MOD_ID, "textures/gui/button_unfocused.png"),
            Identifier.of(KeepInventorySortedSimple.MOD_ID, "textures/gui/button_focused.png"));
    private final boolean playerInv;

    public SortButtonWidget(int int_1, int int_2, boolean playerInv) {
        super(int_1, int_2, 10, 9, TEXTURES, null, Text.literal(""));
        this.playerInv = playerInv;
    }

    @Override
    public void onPress() {
        if (GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) == 1) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null && InventoryHelper.canSortInventory(player)) {
                String screenID;
                Identifier screenIdentifier = Registries.SCREEN_HANDLER.getId(MinecraftClient.getInstance().player.currentScreenHandler.getType());
                if(screenIdentifier != null) {
                    screenID = screenIdentifier.toString();
                } else {
                    screenID = "Unable to determine Screen ID";
                }

                MutableText MODID = Text.literal("[" + KeepInventorySortedSimple.MOD_ID + "]: ").formatted(Formatting.BLUE);
                MutableText autoDNS = (Text.translatable("net.manu_faktur.kiss.sortbtn.clickhere")).formatted(Formatting.UNDERLINE, Formatting.WHITE)
                        .styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kiss ignorelist doNotSort " + screenID)));
                MutableText autoDND = (Text.translatable("net.manu_faktur.kiss.sortbtn.clickhere")).formatted(Formatting.UNDERLINE, Formatting.WHITE)
                        .styled((style) -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kiss ignorelist doNotDisplay " + screenID)));
                MinecraftClient.getInstance().player.sendMessage(MODID.copyContentOnly().append(autoDNS).append(Text.translatable("net.manu_faktur.kiss.sortbtn.dnsadd").formatted(Formatting.WHITE)), false);
                MinecraftClient.getInstance().player.sendMessage(MODID.copyContentOnly().append(autoDND).append(Text.translatable("net.manu_faktur.kiss.sortbtn.dndadd").formatted(Formatting.WHITE)), false);
            } else
                MinecraftClient.getInstance().player.sendMessage(Text.literal("[" + KeepInventorySortedSimple.MOD_ID + "]: ").append(Text.translatable("net.manu_faktur.kiss.sortbtn.error")), false);
        } else
            InventorySortPacket.sendSortPacket(playerInv);
    }

    @Override
    public void renderWidget(DrawContext context, int int_1, int int_2, float float_1) {
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.enableDepthTest();
        context.getMatrices().push();
        context.getMatrices().scale(.5f, .5f, 1);
        context.getMatrices().translate(getX(), getY(), 0);

        context.drawTexture(TEXTURES.get(true, isSelected() || isHovered()), getX(), getY(), 0, 0, 20, 18, 20, 18);
        this.renderTooltip(context, int_1, int_2);
        context.getMatrices().pop();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int current = KeepInventorySortedSimpleClient.getConfig().sortType.ordinal();
        if (verticalAmount > 0) {
            current++;
            if (current >= SortCases.SortType.values().length)
                current = 0;
        } else {
            current--;
            if (current < 0)
                current = SortCases.SortType.values().length - 1;
        }
        KeepInventorySortedSimpleClient.getConfig().sortType = SortCases.SortType.values()[current];
        KeepInventorySortedSimple.configManager.save();
        KeepInventorySortedSimpleClient.syncConfig();
        return true;
    }


    public void renderTooltip(DrawContext context, int mouseX, int mouseY) {
        if (KeepInventorySortedSimpleClient.getConfig().displayTooltip && this.isHovered()) {
            List<Text> lines = new ArrayList<>();
            lines.add(Text.translatable("net.manu_faktur.kiss.sortbtn.sort").append(Text.translatable(KeepInventorySortedSimpleClient.getConfig().sortType.getTranslationKey())));
            if (KeepInventorySortedSimpleClient.getConfig().debugMode) {
                lines.add(Text.translatable("net.manu_faktur.kiss.sortbtn.debug"));
                lines.add(Text.translatable("net.manu_faktur.kiss.sortbtn.debug2"));
            }
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, lines, getX(), getY());
        }
    }
}
