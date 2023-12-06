package de.takacick.emeraldmoney.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.emeraldmoney.access.PlayerProperties;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class EmeraldMoneyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("emeraldmoney").requires((source) -> {
                    return source.hasPermissionLevel(2);
                })
                .then(CommandManager.literal("multiplier")
                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg(1)).executes((context) -> {
                                    double multiplier = DoubleArgumentType.getDouble(context, "multiplier");
                                    Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                                    if (playerEntities != null) {
                                        playerEntities.forEach(serverPlayerEntity -> {
                                            context.getSource().sendFeedback(() -> Text.of("Set " + serverPlayerEntity.getName().getString() + "'s Emerald multiplier to §a" + multiplier), false);
                                            ((PlayerProperties) serverPlayerEntity).setEmeraldMultiplier(multiplier);
                                        });
                                    } else {
                                        context.getSource().sendFeedback(() -> Text.of("§cUnknown players!"), false);
                                    }
                                    return 1;
                                })))
                ).then(CommandManager.literal("emeralds")
                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                .then(CommandManager.argument("emeralds", IntegerArgumentType.integer(0)).executes((context) -> {
                                    int emeralds = IntegerArgumentType.getInteger(context, "emeralds");
                                    Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                                    if (playerEntities != null) {
                                        playerEntities.forEach(serverPlayerEntity -> {
                                            context.getSource().sendFeedback(() -> Text.of("Set " + serverPlayerEntity.getName().getString() + "'s Emeralds to §a" + emeralds), false);
                                            ((PlayerProperties) serverPlayerEntity).setEmeralds(emeralds);
                                        });
                                    } else {
                                        context.getSource().sendFeedback(() -> Text.of("§cUnknown players!"), false);
                                    }
                                    return 1;
                                })))
                ).then(CommandManager.literal("wallet")
                        .then(CommandManager.argument("player", EntityArgumentType.players())
                                .executes((context) -> {
                                    Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                                    if (playerEntities != null) {
                                        playerEntities.forEach(serverPlayerEntity -> {
                                            context.getSource().sendFeedback(() -> Text.of("Toggled " + serverPlayerEntity.getName().getString() + "'s Emerald Wallet"), false);
                                            ((PlayerProperties) serverPlayerEntity).setEmeraldWallet(!((PlayerProperties) serverPlayerEntity).hasEmeraldWallet());
                                        });
                                    } else {
                                        context.getSource().sendFeedback(() -> Text.of("§cUnknown players!"), false);
                                    }
                                    return 1;
                                }))
                ).build();

        dispatcher.getRoot().addChild(rootNode);
    }
}
