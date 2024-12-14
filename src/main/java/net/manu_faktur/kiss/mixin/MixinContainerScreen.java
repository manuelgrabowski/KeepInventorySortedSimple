package net.manu_faktur.kiss.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.manu_faktur.kiss.InventoryHelper;
import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.client.KeepInventorySortedSimpleClient;
import net.manu_faktur.kiss.client.SortButtonWidget;
import net.manu_faktur.kiss.client.SortableContainerScreen;
import net.manu_faktur.kiss.client.config.KissConfig;
import net.manu_faktur.kiss.network.InventorySortPacket;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class MixinContainerScreen extends Screen implements SortableContainerScreen {
    @Shadow
    protected int backgroundWidth;
    @Shadow
    protected int backgroundHeight;

    @Shadow
    @Final
    protected ScreenHandler handler;

    @Shadow
    protected int x;
    @Shadow
    protected int y;

    @Shadow
    protected Slot focusedSlot;
    private SortButtonWidget invsort$SortBtn;

    protected MixinContainerScreen(Text text_1) {
        super(text_1);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void invsort$init(CallbackInfo callbackinfo) {
        if (client == null || client.player == null)
            return;

        boolean isPlayerInventory = !InventoryHelper.canSortInventory(client.player);

        if ((KissConfig.displaySortButtonInventories && !isPlayerInventory) || (KissConfig.displaySortButtonPlayerInventory && isPlayerInventory)) {
            this.addDrawableChild(invsort$SortBtn = new SortButtonWidget(this.x + this.backgroundWidth - 20, this.y + (isPlayerInventory ? (backgroundHeight - 95) : 6), isPlayerInventory));
        }

        if (!isPlayerInventory && KissConfig.displaySortButtonPlayerInventory) {
            this.addDrawableChild(new SortButtonWidget(this.x + this.backgroundWidth - 20, this.y + ((SortableContainerScreen) (this)).getMiddleHeight(), true));

        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void invsort$mouseClicked(double x, double y, int button, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (KeepInventorySortedSimpleClient.isKeyBindingPressed(button, InputUtil.Type.MOUSE)) {
            performEventBasedSorting(callbackInfoReturnable);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void invsort$keyPressed(int keycode, int scancode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (KeepInventorySortedSimpleClient.isKeyBindingPressed(keycode, InputUtil.Type.KEYSYM)) {
            performEventBasedSorting(callbackInfoReturnable);
        }
    }

    @Unique
    private void performEventBasedSorting(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (client == null || client.player == null)
            return;

        // Don't interfere with Creative Mode middle mouse click default actions
        if(client.player.isInCreativeMode()) {
            // allow picking stack
            if (focusedSlot != null && focusedSlot.hasStack())
                return;
            // allow dragging an already picked stack
            if (!client.player.currentScreenHandler.getCursorStack().isEmpty())
                return;
        }

        boolean playerOnlyInv = !InventoryHelper.canSortInventory(client.player);
        if (!playerOnlyInv && KissConfig.sortMouseHoveredInventory) {
            if (focusedSlot != null) {
                playerOnlyInv = focusedSlot.inventory instanceof PlayerInventory;
            }
        }

        InventorySortPacket.sendSortPacket(playerOnlyInv);
        callbackInfoReturnable.setReturnValue(true);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void invsort$render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (invsort$SortBtn != null)
            invsort$SortBtn.setX(this.x + this.backgroundWidth - 20);
    }

    @Override
    public SortButtonWidget getSortButton() {
        return invsort$SortBtn;
    }

    @Override
    public int getMiddleHeight() {
        if (this.handler.slots.isEmpty()) return 0;
        return this.handler.getSlot(this.handler.slots.size() - 36).y - 12;
    }
}
