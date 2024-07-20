package net.manu_faktur.kiss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.manu_faktur.kiss.client.config.IgnoreList;
import net.manu_faktur.kiss.interfaces.InvSorterPlayer;
import net.manu_faktur.kiss.network.InventorySortPacket;
import net.manu_faktur.kiss.network.SyncIgnoreListPacket;
import net.manu_faktur.kiss.network.SyncInvSortSettingsPacket;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KeepInventorySortedSimple implements ModInitializer {
    public static final ConfigManager.MultiConfigManager configManager = new ConfigManager.MultiConfigManager(KeepInventorySortedSimple.MOD_ID);
    public static final String MOD_ID = "kiss";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        configManager.registerFile("ignorelist.json5", new IgnoreList());
        configManager.load();
        CommandRegistrationCallback.EVENT.register(SortCommand::register);
        InventorySortPacket.registerReceivePacket();
        SyncInvSortSettingsPacket.registerReceiveSyncData();
        SyncIgnoreListPacket.registerSyncOnPlayerJoin();

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (oldPlayer instanceof InvSorterPlayer) {
                ((InvSorterPlayer) newPlayer).setSortType(((InvSorterPlayer) oldPlayer).getSortType());
            }
        });
    }

    public static IgnoreList getIgnoreList() {
        return (IgnoreList) configManager.getConfig("ignorelist.json5");
    }
}
