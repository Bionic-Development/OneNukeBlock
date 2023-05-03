package de.takacick.everythinghearts.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ProgressBarCommand {
    public static final ServerBossBar progressBar = new ServerBossBar(Text.of("§c❤ §a§lWorld Conversion"), BossBar.Color.RED, BossBar.Style.PROGRESS);
    public static int maxTicks = 100;
    public static int ticks = 0;
    public static int tickStep = 1;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            progressBar.setPercent((float) ticks / (float) maxTicks);

            server.getPlayerManager().getPlayerList().forEach(progressBar::addPlayer);
        });

        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("heartbar")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.literal("toggle").executes(context -> {
                            progressBar.setVisible(!progressBar.isVisible());
                            context.getSource().sendFeedback(Text.literal("Toggled Heart Bar"), false);

                            return 1;
                        })
                ).then(CommandManager.literal("ticks").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0, maxTicks)).executes(context -> {
                            ticks = IntegerArgumentType.getInteger(context, "ticks");
                            progressBar.setPercent((float) ticks / (float) maxTicks);

                            context.getSource().sendFeedback(Text.literal("Set Heart Bar ticks to " + ticks), false);

                            return 1;
                        }))
                ).then(CommandManager.literal("maxTicks").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0)).executes(context -> {
                            maxTicks = IntegerArgumentType.getInteger(context, "ticks");
                            progressBar.setPercent((float) ticks / (float) maxTicks);

                            context.getSource().sendFeedback(Text.literal("Set Heart Bar max ticks to " + maxTicks), false);

                            return 1;
                        }))
                ).then(CommandManager.literal("step").then(CommandManager.argument("ticks", IntegerArgumentType.integer(0, maxTicks)).executes(context -> {
                            tickStep = IntegerArgumentType.getInteger(context, "ticks");
                            progressBar.setPercent((float) ticks / (float) maxTicks);

                            context.getSource().sendFeedback(Text.literal("Set Heart Bar tick steps to " + tickStep), false);

                            return 1;
                        })
                )).build();
        dispatcher.getRoot().addChild(rootNode);
    }
}
