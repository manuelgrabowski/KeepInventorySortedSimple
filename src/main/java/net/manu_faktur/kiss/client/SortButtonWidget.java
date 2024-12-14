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
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.RenderLayer;
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
        InventorySortPacket.sendSortPacket(playerInv);
    }

    @Override
    public void renderWidget(DrawContext context, int int_1, int int_2, float float_1) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION);
        RenderSystem.enableDepthTest();
        context.getMatrices().push();
        context.getMatrices().scale(.5f, .5f, 1);
        context.getMatrices().translate(getX(), getY(), 0);

        context.drawTexture(RenderLayer::getGuiTextured, TEXTURES.get(true, isHovered()), getX(), getY(), 0, 0, 20, 18, 20, 18);
        this.renderTooltip(context, int_1, int_2);
        context.getMatrices().pop();
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
