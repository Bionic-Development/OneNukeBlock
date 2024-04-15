package de.takacick.onegirlfriendblock.server.oneblock;

import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import de.takacick.onegirlfriendblock.registry.block.GirlfriendOneBlock;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class OneBlockHandler {

    private static OneBlockHandler INSTANCE;

    public OneBlockHandler() {
        INSTANCE = this;

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient && !player.isCreative()) {
                if (state.isOf(ItemRegistry.GIRLFRIEND_ONE_BLOCK)) {
                    OneBlockServerState.getServerState(world.getServer()).ifPresent(serverState -> {
                        drop(serverState, world, pos);
                    });

                    ItemStack itemStack = player.getMainHandStack();
                    itemStack.postMine(world, state, pos, player);

                    return false;
                }
            }
            return true;
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("onegirlfriendblock")
                    .requires(source -> {
                        return source.hasPermissionLevel(2);
                    }).then(CommandManager.literal("drops").executes(context -> {

                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                        if (playerEntity == null) {
                            return 0;
                        }

                        SimpleInventory simpleInventory = new SimpleInventory(54);
                        AtomicInteger slot = new AtomicInteger();
                        OneBlockServerState.getServerState(context.getSource().getServer()).ifPresent(serverState -> {
                            serverState.getDrops().forEach(itemStack -> {
                                if (slot.get() >= 54) {
                                    return;
                                }

                                simpleInventory.setStack(slot.getAndIncrement(), itemStack);
                            });
                        });

                        playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> DropsScreenHandler.createGeneric9x6(syncId, inventory, simpleInventory), Text.of("OneGirlfriendBlock Drops: ")));
                        return 1;
                    })).then(CommandManager.literal("set")
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

                                playerEntity.getWorld().setBlockState(blockPos, ItemRegistry.GIRLFRIEND_ONE_BLOCK.getDefaultState().with(GirlfriendOneBlock.FACING, playerEntity.getHorizontalFacing()));

                                context.getSource().sendFeedback(() -> Text.literal("Placed OneGirlfriendBlock at " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ()), true);

                                return 1;
                            }))
                    ).then(CommandManager.literal("entity")
                            .then(CommandManager.argument("entity", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.ENTITY_TYPE)).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                    .executes((context) -> {
                                        EntityType<?> entityType;

                                        try {
                                            RegistryEntry.Reference<EntityType<?>> reference = RegistryEntryArgumentType.getSummonableEntityType(context, "entity");
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

                                        OneBlockServerState.getServerState(context.getSource().getServer()).ifPresent(serverState -> {
                                            serverState.setNextEntity(entityType, null);
                                        });

                                        context.getSource().sendFeedback(() -> Text.of("Set next OneGirlfriendBlock entity: " + Text.translatable(entityType.getTranslationKey()).getString()), true);
                                        return 1;
                                    }).then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound()).executes((context) -> {
                                        EntityType<?> entityType;
                                        try {
                                            RegistryEntry.Reference<EntityType<?>> reference = RegistryEntryArgumentType.getSummonableEntityType(context, "entity");
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

                                        OneBlockServerState.getServerState(context.getSource().getServer()).ifPresent(serverState -> {
                                            serverState.setNextEntity(entityType, NbtCompoundArgumentType.getNbtCompound(context, "nbt"));
                                        });

                                        context.getSource().sendFeedback(() -> Text.of("Set next OneGirlfriendBlock entity: " + Text.translatable(entityType.getTranslationKey()).getString()), true);
                                        return 1;
                                    })))
                    ).then(CommandManager.literal("girlfriendtp")
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .executes(context -> {
                                        ServerPlayerEntity targetEntity = EntityArgumentType.getPlayer(context, "player");

                                        if (targetEntity == null) {
                                            context.getSource().sendFeedback(() -> Text.literal("§cUnknown player!"), false);
                                            return 0;
                                        }
                                        OneBlockServerState.getServerState(context.getSource().getServer()).ifPresent(serverState -> {
                                            serverState.setGirlfriendTeleport(targetEntity.getUuid());
                                        });

                                        context.getSource().sendFeedback(() -> Text.literal("Set Girlfriend Teleport target to " + targetEntity.getName().getString()), false);

                                        return 1;
                                    }))
                    ).build();

            dispatcher.getRoot().addChild(rootNode);
        });
    }

    public void drop(OneBlockServerState serverState, World world, BlockPos blockPos) {
        ItemStack itemStack = getRandomDrop(serverState);
        dropStack(world, blockPos, Direction.UP, itemStack);

        EntityType<?> nextEntity = serverState.getNextEntity();
        NbtCompound entityNbt = serverState.getEntityNbt();

        if (nextEntity != null) {
            NbtCompound nbtCompound = entityNbt == null ? new NbtCompound() : entityNbt;
            nbtCompound.putString("id", Registries.ENTITY_TYPE.getId(nextEntity).toString());
            Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entity1 -> {
                entity1.refreshPositionAndAngles(blockPos.getX() + 0.5, blockPos.getY() + 1.1, blockPos.getZ() + 0.5, entity1.getYaw(), entity1.getPitch());
                return entity1;
            });

            if (entity instanceof MobEntity && world instanceof ServerWorld serverWorld) {
                ((MobEntity) entity).initialize(serverWorld, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWN_EGG, null, null);
            }

            serverState.setNextEntity(null, null);

            if (entity != null) {
                world.spawnEntity(entity);
            }
        }
        serverState.setDirty(true);
    }

    public static OneBlockHandler getInstance() {
        return INSTANCE;
    }

    public ItemStack getRandomDrop(OneBlockServerState serverState) {
        ItemStack itemStack = serverState.getDrops().poll();
        serverState.setDirty(true);

        if (itemStack == null || itemStack.isEmpty()) {
            Optional<RegistryEntry.Reference<Item>> item = Registries.ITEM.getRandom(Random.create());
            if (item.isEmpty() || item.get().value().getDefaultStack().isOf(ItemRegistry.GIRLFRIEND_ONE_BLOCK_ITEM)) {
                return getRandomDrop(serverState);
            }
        }

        return itemStack;
    }

    public static void dropStack(World world, BlockPos pos, Direction direction, ItemStack stack) {
        int i = direction.getOffsetX();
        int j = direction.getOffsetY();
        int k = direction.getOffsetZ();
        float f = EntityType.ITEM.getWidth() / 2.0f;
        float g = EntityType.ITEM.getHeight() / 2.0f;
        double d = (double) ((float) pos.getX() + 0.5f) + (i == 0 ? MathHelper.nextDouble(world.random, -0.25, 0.25) : (double) ((float) i * (0.5f + f)));
        double e = (double) ((float) pos.getY() + 0.5f) + (j == 0 ? MathHelper.nextDouble(world.random, -0.25, 0.25) : (double) ((float) j * (0.5f + g))) - (double) g;
        double h = (double) ((float) pos.getZ() + 0.5f) + (k == 0 ? MathHelper.nextDouble(world.random, -0.25, 0.25) : (double) ((float) k * (0.5f + f)));
        double l = i == 0 ? MathHelper.nextDouble(world.random, -0.1, 0.1) : (double) i * 0.1;
        double m = j == 0 ? MathHelper.nextDouble(world.random, 0.0, 0.1) : (double) j * 0.1 + 0.1;
        double n = k == 0 ? MathHelper.nextDouble(world.random, -0.1, 0.1) : (double) k * 0.1;
        dropStack(world, () -> new ItemEntity(world, d, e, h, stack, l, m, n), stack);
    }

    private static void dropStack(World world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack) {
        if (world.isClient || stack.isEmpty() || !world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            return;
        }
        ItemEntity itemEntity = itemEntitySupplier.get();
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }
}
