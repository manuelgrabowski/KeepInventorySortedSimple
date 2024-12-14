package net.manu_faktur.kiss;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.manu_faktur.kiss.interfaces.InvSorterPlayer;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

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
}
