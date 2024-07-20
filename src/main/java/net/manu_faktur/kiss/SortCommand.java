package net.manu_faktur.kiss;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.manu_faktur.kiss.interfaces.InvSorterPlayer;
import net.manu_faktur.kiss.network.SyncIgnoreListPacket;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.function.BiConsumer;


public class SortCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralArgumentBuilder<ServerCommandSource> invsortCommand = CommandManager.literal("kiss").requires((source) -> source.hasPermissionLevel(0));

        invsortCommand.then(CommandManager.literal("sort")
                .requires((source) -> source.hasPermissionLevel(0))
                .executes((commandContext) -> {
                    HitResult hit = commandContext.getSource().getPlayer().raycast(6, 1, false);
                    if (hit instanceof BlockHitResult) {
                        Text feedBack = InventoryHelper.sortBlock(commandContext.getSource().getPlayer().getWorld(), ((BlockHitResult) hit).getBlockPos(), commandContext.getSource().getPlayer(), ((InvSorterPlayer) commandContext.getSource().getPlayer()).getSortType());
                        commandContext.getSource().sendFeedback(() -> feedBack, false);
                    }
                    return 1;
                }));
        invsortCommand.then(CommandManager.literal("sortme")
                .requires((source) -> source.hasPermissionLevel(0))
                .executes((commandContext) -> {
                    InventoryHelper.sortInv(commandContext.getSource().getPlayer(), true, ((InvSorterPlayer) commandContext.getSource().getPlayer()).getSortType());

                    Text feedBack = Text.translatable("net.manu_faktur.kiss.sorting.sorted");
                    commandContext.getSource().sendFeedback(() -> feedBack, false);
                    return 1;
                }));

        invsortCommand.then(CommandManager.literal("ignorelist")
                .requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("doNotSort")
                        .then(CommandManager.argument("screenid", StringArgumentType.greedyString())
                                .executes(context -> executeIgnoreList(context, true))))
                .then(CommandManager.literal("doNotDisplay")
                        .then(CommandManager.argument("screenid", StringArgumentType.greedyString())
                                .executes(context -> executeIgnoreList(context, false)))));

        for (SortCases.SortType sortType : SortCases.SortType.values()) {
            invsortCommand.then(CommandManager.literal("sortType")
                    .then(CommandManager.literal(sortType.name())
                            .executes(context -> {
                                ((InvSorterPlayer) context.getSource().getPlayer()).setSortType(sortType);
                                Text feedBack = Text.translatable("net.manu_faktur.kiss.cmd.updatesortingtype");
                                context.getSource().sendFeedback(() -> feedBack, false);
                                return 1;
                            })));
        }

        dispatcher.register(invsortCommand);
    }

    public static int executeIgnoreList(CommandContext<ServerCommandSource> commandContext, boolean isDNS) {
        String id = StringArgumentType.getString(commandContext, "screenid");
        if (Registries.SCREEN_HANDLER.containsId(Identifier.of(id))) {
            if (isDNS) KeepInventorySortedSimple.getIgnoreList().doNotSortList.add(id);
            else KeepInventorySortedSimple.getIgnoreList().hideSortButtonList.add(id);
            KeepInventorySortedSimple.configManager.save();
            commandContext.getSource().getServer().getPlayerManager().getPlayerList().forEach(SyncIgnoreListPacket::sync);
            commandContext.getSource().sendFeedback(() -> Text.translatable("net.manu_faktur.kiss.cmd.addignorelist").append(id), false);
            return 0;
        } else
            commandContext.getSource().sendFeedback(() -> Text.translatable("net.manu_faktur.kiss.cmd.invalidscreen"), false);
        return 1;
    }

    private static void registerBooleanCommand(LiteralArgumentBuilder<ServerCommandSource> invsortCommand, String command, Text response, BiConsumer<PlayerEntity, Boolean> storage) {
        invsortCommand.then(CommandManager.literal(command)
                .then(CommandManager.literal("false")
                        .executes(context -> {
                            storage.accept(context.getSource().getPlayer(), false);
                            Text feedBack = response.copy().append("False");
                            context.getSource().sendFeedback(() -> feedBack, false);
                            return 1;
                        })));
        invsortCommand.then(CommandManager.literal(command)
                .then(CommandManager.literal("true")
                        .executes(context -> {
                            storage.accept(context.getSource().getPlayer(), true);
                            Text feedBack = response.copy().append("True");
                            context.getSource().sendFeedback(() -> feedBack, false);
                            return 1;
                        })));
    }
}
