package net.manu_faktur.kiss.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.manu_faktur.kiss.SortCases;
import net.manu_faktur.kiss.client.config.KissConfig;
import net.manu_faktur.kiss.interfaces.InvSorterPlayer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record SyncInvSortSettingsPacket(int sortType) implements CustomPayload {
    private static final CustomPayload.Id<SyncInvSortSettingsPacket> ID = new CustomPayload.Id<>(Identifier.of("kiss", "sync_settings_packet"));
    private static final PacketCodec<RegistryByteBuf, SyncInvSortSettingsPacket> CODEC = CustomPayload.codecOf(SyncInvSortSettingsPacket::write, SyncInvSortSettingsPacket::new);

    public SyncInvSortSettingsPacket(PacketByteBuf buf) {
        this(buf.readInt());
    }

    @Environment(EnvType.CLIENT)
    public static void registerSyncOnPlayerJoin() {
        ClientPlayNetworking.send(new SyncInvSortSettingsPacket(KissConfig.sortType.ordinal()));
    }

    public static void registerReceiveSyncData() {
        PayloadTypeRegistry.playC2S().register(SyncInvSortSettingsPacket.ID, SyncInvSortSettingsPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SyncInvSortSettingsPacket.ID, ((payload, context) -> {
            ServerPlayerEntity player = context.player();
            player.getServer().execute(() -> ((InvSorterPlayer) player).setSortType(SortCases.SortType.values()[payload.sortType]));
        }));
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(sortType);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
