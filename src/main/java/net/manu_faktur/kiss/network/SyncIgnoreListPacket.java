package net.manu_faktur.kiss.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.manu_faktur.kiss.KeepInventorySortedSimple;
import net.manu_faktur.kiss.client.config.IgnoreList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record SyncIgnoreListPacket(ByteBuf buffer) implements CustomPayload {
    private static final CustomPayload.Id<SyncIgnoreListPacket> ID = new CustomPayload.Id<>(Identifier.of("kiss", "sync_ignorelist_packet"));
    private static final PacketCodec<RegistryByteBuf, SyncIgnoreListPacket> CODEC = CustomPayload.codecOf(SyncIgnoreListPacket::write, SyncIgnoreListPacket::new);

    public SyncIgnoreListPacket(PacketByteBuf buf) {
        this(buf.readBytes(buf.readableBytes()));
    }

    public static void registerSyncOnPlayerJoin() {
        PayloadTypeRegistry.playS2C().register(SyncIgnoreListPacket.ID, SyncIgnoreListPacket.CODEC);
        ServerPlayConnectionEvents.JOIN.register((serverPlayNetworkHandler, packetSender, minecraftServer) -> packetSender.sendPacket(new SyncIgnoreListPacket(getBuf())));
    }

    public static void sync(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new SyncIgnoreListPacket(getBuf()));
    }

    private static PacketByteBuf getBuf() {
        IgnoreList ignoreList = KeepInventorySortedSimple.getIgnoreList();
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        String[] hideList = new String[ignoreList.hideSortButtonList.size()];
        hideList = ignoreList.hideSortButtonList.toArray(hideList);
        buf.writeInt(hideList.length);
        for (String value : hideList) buf.writeString(value);

        String[] unSortList = new String[ignoreList.doNotSortList.size()];
        unSortList = ignoreList.doNotSortList.toArray(unSortList);
        buf.writeInt(unSortList.length);
        for (String s : unSortList) buf.writeString(s);
        return buf;
    }

    @Environment(EnvType.CLIENT)
    public static void registerReceiveIgnoreList() {
        ClientPlayNetworking.registerGlobalReceiver(SyncIgnoreListPacket.ID, (payload, context) -> {
            PacketByteBuf packet = new PacketByteBuf(payload.buffer);
            int numHides = packet.readInt();
            for (int i = 0; i < numHides; i++)
                KeepInventorySortedSimple.getIgnoreList().hideSortButtonList.add(packet.readString());

            int numNoSort = packet.readInt();
            for (int i = 0; i < numNoSort; i++)
                KeepInventorySortedSimple.getIgnoreList().doNotSortList.add(packet.readString());
        });
    }

    public void write(PacketByteBuf buf) {
        buf.writeBytes(buffer);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
