package de.takacick.secretgirlbase.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.secretgirlbase.access.PlayerProperties;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SecretGirlBaseCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("secretgirlbase")
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.literal("phone")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> {
                                            ServerPlayerEntity playerEntity = EntityArgumentType.getPlayer(context, "player");
                                            ServerPlayerEntity targetEntity = EntityArgumentType.getPlayer(context, "target");

                                            if (playerEntity == null) {
                                                context.getSource().sendFeedback(() -> Text.literal("§cUnknown player!"), false);
                                                return 0;
                                            }
                                            if (targetEntity == null) {
                                                context.getSource().sendFeedback(() -> Text.literal("§cUnknown target!"), false);
                                                return 0;
                                            }

                                            ((PlayerProperties) playerEntity).setPhonePlayer(targetEntity);
                                            context.getSource().sendFeedback(() -> Text.literal("Set " + playerEntity.getName().getString() + "'s phone target to " + targetEntity.getName().getString()), false);

                                            return 1;
                                        })))
                ).then(CommandManager.literal("smokedrop")
                        .executes(context -> {
                            PlayerEntity playerEntity = context.getSource().getPlayer();
                            if (!(playerEntity instanceof PlayerProperties playerProperties)) {
                                return 0;
                            }

                            playerProperties.setSmokeDrop(playerEntity.getMainHandStack());
                            context.getSource().sendFeedback(() -> Text.literal("Set Smoke Drop to ").append(playerEntity.getMainHandStack().toHoverableText()), false);
                            return 1;
                        })
                ).build();
        dispatcher.getRoot().addChild(rootNode);
    }
}
