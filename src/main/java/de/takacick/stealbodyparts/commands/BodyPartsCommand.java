package de.takacick.stealbodyparts.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.*;
import java.util.stream.Collectors;

public class BodyPartsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("bodyparts").requires((source) -> {
            return source.hasPermissionLevel(2);
        }).then(CommandManager.literal("toggle")
                .then(CommandManager.argument("player", EntityArgumentType.players())
                        .then(CommandManager.argument("part", StringArgumentType.greedyString()).suggests((context, builder) -> {
                            List<String> entries = Arrays.asList(BodyPart.values()).stream().map(BodyPart::getName).collect(Collectors.toCollection(LinkedList::new));
                            return CommandSource.suggestMatching(entries, builder);
                        }).executes(context -> {
                            return executeToggle(context.getSource(), EntityArgumentType.getEntities(context, "player"),
                                    StringArgumentType.getString(context, "part"));
                        }))
                )
        ).then(CommandManager.literal("next")
                .then(CommandManager.argument("player", EntityArgumentType.players())
                        .then(CommandManager.argument("part", StringArgumentType.greedyString()).suggests((context, builder) -> {
                            List<String> entries = Arrays.asList(BodyPart.values()).stream().map(BodyPart::getName).collect(Collectors.toCollection(LinkedList::new));
                            entries.add("none");
                            return CommandSource.suggestMatching(entries, builder);
                        }).executes(context -> {
                            return executeNext(context.getSource(), EntityArgumentType.getEntities(context, "player"),
                                    StringArgumentType.getString(context, "part"));
                        }))
                )
        ).build();

        dispatcher.getRoot().addChild(rootNode);
    }

    private static int executeToggle(ServerCommandSource source, Collection<? extends Entity> collection, String part) {
        List<Entity> targets = new ArrayList<>(collection);
        BodyPart bodyPart = BodyPart.getByName(part);

        if (bodyPart == null) {
            source.sendFeedback(Text.translatable("Unknown body part!"), true);
            return 0;
        }

        targets.removeIf(target -> !(target instanceof PlayerProperties));

        if (targets.isEmpty()) {
            source.sendFeedback(Text.translatable("Unknown players!"), true);
            return 0;
        }

        targets.forEach(target -> {
            if (target instanceof PlayerProperties playerProperties) {
                playerProperties.setBodyPart(bodyPart.getIndex(), !playerProperties.hasBodyPart(bodyPart.getIndex()));
                source.sendFeedback(Text.translatable("Toggled " + target.getName().getString() + "'s " + bodyPart.getName()), true);
            }
        });

        return 0;
    }

    private static int executeNext(ServerCommandSource source, Collection<? extends Entity> collection, String part) {
        List<Entity> targets = new ArrayList<>(collection);
        BodyPart bodyPart = BodyPart.getByName(part);

        if (bodyPart == null && !part.equals("none")) {
            source.sendFeedback(Text.translatable("Unknown body part!"), true);
            return 0;
        }

        targets.removeIf(target -> !(target instanceof PlayerProperties));

        if (targets.isEmpty()) {
            source.sendFeedback(Text.translatable("Unknown players!"), true);
            return 0;
        }

        targets.forEach(target -> {
            if (target instanceof PlayerProperties playerProperties) {
                playerProperties.setNextBodyPart(bodyPart);
                source.sendFeedback(Text.translatable("Set " + target.getName().getString() + "'s next body part to " + (part.equalsIgnoreCase("none") ? "none" :bodyPart.getName())), true);
            }
        });

        return 0;
    }
}

