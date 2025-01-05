package net.manu_faktur.kiss;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.manu_faktur.kiss.client.config.KissConfig;
import net.manu_faktur.kiss.interfaces.InvSorterPlayer;
import net.manu_faktur.kiss.network.InventorySortPacket;
import net.manu_faktur.kiss.network.SyncInvSortSettingsPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepInventorySortedSimple implements ModInitializer {
    public static final String MOD_ID = "kiss";
    //public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MidnightConfig.init(MOD_ID, KissConfig.class);
        CommandRegistrationCallback.EVENT.register(SortCommand::register);
        InventorySortPacket.registerReceivePacket();
        SyncInvSortSettingsPacket.registerReceiveSyncData();

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            if (oldPlayer instanceof InvSorterPlayer) {
                ((InvSorterPlayer) newPlayer).setSortType(((InvSorterPlayer) oldPlayer).getSortType());
            }
        });
    }
}
