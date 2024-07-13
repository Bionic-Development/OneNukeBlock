package de.takacick.onegirlboyblock.utils.oneblock.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.onegirlboyblock.utils.oneblock.OneBlock;
import de.takacick.onegirlboyblock.utils.oneblock.screen.DropsScreenHandler;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class OneBlockCommand {

    public static void register(String alias, Block block, int color, CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, Function<MinecraftServer, Optional<OneBlock>> oneBlockGetter) {
        final MutableText name = Text.literal(alias).withColor(color);
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal(alias.toLowerCase())
                .requires(source -> {
                    return source.hasPermissionLevel(2);
                }).then(CommandManager.literal("drops").executes(context -> {

                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                            if (playerEntity == null) {
                                return 0;
                            }

                            SimpleInventory simpleInventory = new SimpleInventory(54);
                            AtomicInteger slot = new AtomicInteger();

                            oneBlockGetter.apply(context.getSource().getServer()).ifPresent(oneBlock -> {
                                oneBlock.getDrops().forEach(itemStack -> {
                                    if (slot.get() >= 54) {
                                        return;
                                    }

                                    simpleInventory.setStack(slot.getAndIncrement(), itemStack);
                                });
                                playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> DropsScreenHandler.createGeneric9x6(syncId, inventory, simpleInventory, oneBlock), Text.literal("").append(name).append(" Drops:")));
                            });

                            return 1;
                        })
                ).then(CommandManager.literal("set")
                        .requires(source -> source.hasPermissionLevel(2)).then(CommandManager.argument("pos", BlockPosArgumentType.blockPos()).executes(context -> {
                            BlockPos blockPos;

                            try {
                                blockPos = BlockPosArgumentType.getBlockPos(context, "pos");
                            } catch (Exception exception) {
                                context.getSource().sendFeedback(() -> Text.literal("§cInvalid blockpos"), false);
                                return 0;
                            }

                            ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                            if (playerEntity == null) {
                                context.getSource().sendFeedback(() -> Text.literal("§cUnknown player"), false);
                                return 0;
                            }

                            playerEntity.getWorld().setBlockState(blockPos, block.getDefaultState());

                            context.getSource().sendFeedback(() -> Text.literal("Placed ").append(name).append("at " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ()), true);
                            return 1;
                        }))
                ).then(CommandManager.literal("entity")
                        .then(CommandManager.argument("entity", RegistryEntryReferenceArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                .executes((context) -> {
                                    EntityType<?> entityType;

                                    try {
                                        RegistryEntry.Reference<EntityType<?>> reference = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity");
                                        if (!reference.hasKeyAndValue()) {

                                            context.getSource().sendFeedback(() -> Text.of("§cUnknown entityType"), false);
                                            return 0;
                                        } else {
                                            entityType = reference.value();
                                        }
                                    } catch (Exception exception) {
                                        context.getSource().sendFeedback(() -> Text.of("§cUnknown entityType"), false);
                                        return 0;
                                    }

                                    oneBlockGetter.apply(context.getSource().getServer()).ifPresent(oneBlock -> {
                                        oneBlock.setNextEntity(entityType, null);
                                    });

                                    context.getSource().sendFeedback(() -> Text.literal("Set next ").append(name).append(" entity: " + Text.translatable(entityType.getTranslationKey()).getString()), true);
                                    return 1;
                                }).then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound()).executes((context) -> {
                                    EntityType<?> entityType;
                                    try {
                                        RegistryEntry.Reference<EntityType<?>> reference = RegistryEntryReferenceArgumentType.getSummonableEntityType(context, "entity");
                                        if (!reference.hasKeyAndValue()) {

                                            context.getSource().sendFeedback(() -> Text.of("§cUnknown entityType"), false);
                                            return 0;
                                        } else {
                                            entityType = reference.value();
                                        }
                                    } catch (Exception exception) {
                                        context.getSource().sendFeedback(() -> Text.of("§cUnknown entityType"), false);
                                        return 0;
                                    }

                                    oneBlockGetter.apply(context.getSource().getServer()).ifPresent(oneBlock -> {
                                        oneBlock.setNextEntity(entityType, NbtCompoundArgumentType.getNbtCompound(context, "nbt"));
                                    });

                                    context.getSource().sendFeedback(() -> Text.literal("Set next ").append(name).append(" entity: " + Text.translatable(entityType.getTranslationKey()).getString()), true);
                                    return 1;
                                })))
                ).build();

        dispatcher.getRoot().addChild(rootNode);
    }
}
