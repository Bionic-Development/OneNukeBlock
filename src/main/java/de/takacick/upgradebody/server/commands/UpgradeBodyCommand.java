package de.takacick.upgradebody.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UpgradeBodyCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("upgradebody").requires((source) -> {
            return source.hasPermissionLevel(2);
        }).then(CommandManager.literal("toggle")
                .then(CommandManager.argument("player", EntityArgumentType.players())
                        .executes(context -> {
                            Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                            if (playerEntities != null) {
                                playerEntities.forEach(serverPlayerEntity -> {
                                    if (serverPlayerEntity instanceof PlayerProperties playerProperties) {
                                        playerProperties.setUpgrading(!playerProperties.isUpgrading());
                                    }

                                    context.getSource().sendFeedback(() -> Text.of("Toggled " + serverPlayerEntity.getName().getString() + "'s body upgrades"), false);
                                });
                            } else {
                                context.getSource().sendFeedback(() -> Text.of("§cUnknown players!"), false);
                            }
                            return 1;
                        }))
        ).then(CommandManager.literal("upgrade")
                .then(CommandManager.argument("player", EntityArgumentType.players())
                        .then(CommandManager.argument("upgrade", StringArgumentType.greedyString())
                                .suggests(getUpgradeSuggestions())
                                .executes((context) -> {
                                    String upgrade = StringArgumentType.getString(context, "upgrade");
                                    Optional<BodyPart> optional = BodyParts.getBodyPart(upgrade);

                                    if (optional.isEmpty()) {
                                        context.getSource().sendFeedback(() -> Text.of("§cUnknown upgrade!"), false);
                                        return 0;
                                    }
                                    Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                                    optional.ifPresent(bodyPart -> {
                                        if (playerEntities != null) {
                                            playerEntities.forEach(serverPlayerEntity -> {
                                                if (serverPlayerEntity instanceof PlayerProperties playerProperties) {
                                                    playerProperties.setBodyPart(bodyPart, !playerProperties.hasBodyPart(bodyPart));
                                                }

                                                context.getSource().sendFeedback(() -> Text.of("Toggled " + serverPlayerEntity.getName().getString() + "'s " + bodyPart.getName() + " upgrade"), false);
                                            });
                                        } else {
                                            context.getSource().sendFeedback(() -> Text.of("§cUnknown players!"), false);
                                        }
                                    });

                                    return 1;
                                })))
        ).build();

        dispatcher.getRoot().addChild(rootNode);
    }

    private static <S> SuggestionProvider<S> getUpgradeSuggestions() {
        return (context, builder) -> {
            List<String> entries = new LinkedList<>(BodyParts.BODY_PARTS.stream().map(BodyPart::getName).toList());

            return CommandSource.suggestMatching(entries, builder);
        };
    }
}
