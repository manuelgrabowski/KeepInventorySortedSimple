package net.manu_faktur.kiss.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.manu_faktur.kiss.client.KeepInventorySortedSimpleClient;
import net.manu_faktur.kiss.client.SortButtonWidget;
import net.manu_faktur.kiss.client.SortableContainerScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CreativeInventoryScreen.class)
public abstract class MixinCreativeInventoryScreen implements SortableContainerScreen {


    @Shadow public abstract boolean isInventoryTabSelected();

    @Inject(method = "init", at = @At("TAIL"))
    private void invsort$init(CallbackInfo callbackinfo) {
        if (KeepInventorySortedSimpleClient.getConfig().displaySort) {
            SortButtonWidget sortbtn = this.getSortButton();
            if (sortbtn != null)
                sortbtn.visible = this.isInventoryTabSelected();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (KeepInventorySortedSimpleClient.getConfig().displaySort) {
            SortButtonWidget sortbtn = this.getSortButton();
            if (sortbtn != null)
                sortbtn.visible = this.isInventoryTabSelected();
        }
    }
}

