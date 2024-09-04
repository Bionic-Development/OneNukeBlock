package de.takacick.onenukeblock.utils.nuketimer;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class NukeTimerCommand {

    public static void register(LiteralCommandNode<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("nuketimer")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.literal("duration")
                        .then(CommandManager.argument("seconds", IntegerArgumentType.integer(0)).executes(context -> {
                            int seconds = IntegerArgumentType.getInteger(context, "seconds");
                            NukeTimerServerState.getServerState(context.getSource().getServer()).ifPresent(oneBlockServerState -> {
                                oneBlockServerState.setMaxTicks(seconds * 20);
                                oneBlockServerState.updateProgressBar();

                                context.getSource().sendFeedback(() -> Text.literal("Set Nuke Explosion Timer duration to " + seconds + " seconds"), false);
                            });

                            return 1;
                        }))
                ).then(CommandManager.literal("progress")
                        .then(CommandManager.argument("seconds", IntegerArgumentType.integer(0)).executes(context -> {
                            int seconds = IntegerArgumentType.getInteger(context, "seconds");
                            NukeTimerServerState.getServerState(context.getSource().getServer()).ifPresent(oneBlockServerState -> {
                                oneBlockServerState.setTicks(seconds * 20);
                                oneBlockServerState.updateProgressBar();

                                context.getSource().sendFeedback(() -> Text.literal("Set Nuke Explosion Timer progress to " + seconds + " seconds"), false);
                            });

                            return 1;
                        }))
                ).then(CommandManager.literal("stop").executes(context -> {
                            NukeTimerServerState.getServerState(context.getSource().getServer()).ifPresent(oneBlockServerState -> {
                                oneBlockServerState.setRunning(false);
                                oneBlockServerState.setVisible(false);
                                oneBlockServerState.setTicks(0);
                                oneBlockServerState.updateProgressBar();

                                context.getSource().sendFeedback(() -> Text.literal("Stopped Nuke Explosion Timer"), false);
                            });
                            return 1;
                        })
                ).then(CommandManager.literal("toggle").executes(context -> {
                            NukeTimerServerState.getServerState(context.getSource().getServer()).ifPresent(oneBlockServerState -> {
                                oneBlockServerState.setVisible(!oneBlockServerState.isVisible());
                                oneBlockServerState.updateProgressBar();

                                context.getSource().sendFeedback(() -> Text.literal("Toggled Nuke Explosion Timer"), false);
                            });

                            return 1;
                        })
                ).then(CommandManager.literal("start").executes(context -> {
                            NukeTimerServerState.getServerState(context.getSource().getServer()).ifPresent(oneBlockServerState -> {
                                oneBlockServerState.startTimer(context.getSource().getServer(), true);
                            });

                            return 1;
                        })
                ).build();
        dispatcher.addChild(rootNode);
    }
}
