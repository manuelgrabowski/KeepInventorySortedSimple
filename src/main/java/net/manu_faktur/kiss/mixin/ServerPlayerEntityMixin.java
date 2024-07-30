package net.manu_faktur.kiss.mixin;

import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.SortCases;
import net.manu_faktur.kiss.interfaces.InvSorterPlayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements InvSorterPlayer {
    private static final String saveKEY = KeepInventorySortedSimple.MOD_ID + "invsorter";
    private static final String sortTypeKey = "sorttype";
    private SortCases.SortType sortType = SortCases.SortType.ID;

    @Override
    public SortCases.SortType getSortType() {
        return sortType;
    }

    @Override
    public void setSortType(SortCases.SortType sortType) {
        this.sortType = sortType;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeSortType(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound invSortNbt = new NbtCompound();
        invSortNbt.putInt(sortTypeKey, sortType.ordinal());
        nbt.put(saveKEY, invSortNbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readSortType(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(saveKEY)) {
            NbtCompound invSortNbt = nbt.getCompound(saveKEY);
            if (invSortNbt.contains(sortTypeKey)) sortType = SortCases.SortType.values()[invSortNbt.getInt(sortTypeKey)];
        }
    }
}
