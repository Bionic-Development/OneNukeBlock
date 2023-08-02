package de.takacick.onedeathblock.server.oneblock;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.registry.ItemRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class OneBlockHandler {

    public static OneBlockHandler INSTANCE;

    public static final Queue<ItemStack> QUEUE = new LinkedList<>();

    private ServerWorld world = null;
    private BlockPos blockPos = null;
    private final Block oneBlock = ItemRegistry.DEATH_BLOCK;
    private EntityType<?> nextEntity;
    private NbtCompound entityNbt;

    public OneBlockHandler() {
        INSTANCE = this;
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("onedeathblock")
                    .requires(source -> {
                        return source.hasPermissionLevel(2);
                    }).then(CommandManager.literal("drops").executes(context -> {

                        ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                        if (playerEntity == null) {
                            return 0;
                        }

                        SimpleInventory simpleInventory = new SimpleInventory(54);
                        AtomicInteger slot = new AtomicInteger();

                        QUEUE.forEach(stack -> {
                            if (slot.get() >= 54) {
                                return;
                            }

                            simpleInventory.setStack(slot.getAndIncrement(), stack);
                        });

                        playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> DropsScreenHandler.createGeneric9x6(syncId, inventory, simpleInventory), Text.of("OneDeathBlock Drops: ")));
                        return 1;
                    })).then(CommandManager.literal("set")
                            .requires(source -> source.hasPermissionLevel(2)).then(CommandManager.argument("pos", BlockPosArgumentType.blockPos()).executes(context -> {
                                BlockPos blockPos;

                                try {
                                    blockPos = BlockPosArgumentType.getBlockPos(context, "pos");
                                } catch (Exception exception) {
                                    context.getSource().sendFeedback(Text.literal("§cInvalid blockpos"), false);
                                    return 0;
                                }

                                ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                                if (playerEntity == null) {
                                    context.getSource().sendFeedback(Text.literal("§cUnknown player"), false);
                                    return 0;
                                }

                                this.blockPos = blockPos;
                                this.world = playerEntity.getWorld();

                                context.getSource().sendFeedback(Text.literal("Set OneDeathBlock to " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ()), true);

                                return 1;
                            }))
                    ).then(CommandManager.literal("teleport")
                            .executes(context -> {

                                if (world == null || blockPos == null) {
                                    context.getSource().sendFeedback(Text.literal("§cCouldn't find OneDeathBlock"), false);
                                    return 0;
                                }

                                ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                                if (playerEntity == null) {
                                    context.getSource().sendFeedback(Text.literal("§cUnknown player"), false);
                                    return 0;
                                }

                                playerEntity.teleport(world, blockPos.getX() + 0.5, blockPos.getY() + 1.1, blockPos.getZ() + 0.5, playerEntity.getYaw(), playerEntity.getPitch());
                                context.getSource().sendFeedback(Text.literal("Teleport to OneDeathBlock"), true);

                                return 1;
                            })
                    ).then(CommandManager.literal("entity")
                            .then(CommandManager.argument("entity", EntitySummonArgumentType.entitySummon()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                                    .executes((context) -> {
                                        EntityType<?> entityType;
                                        try {
                                            Identifier identifier = EntitySummonArgumentType.getEntitySummon(context, "entity");
                                            entityType = Registry.ENTITY_TYPE.get(identifier);
                                            if (!identifier.equals(Registry.ENTITY_TYPE.getDefaultId())
                                                    && Registry.ENTITY_TYPE.get(Registry.ENTITY_TYPE.getDefaultId()).equals(entityType)) {

                                                context.getSource().sendFeedback(Text.of("§cUnknown entityType"), false);
                                                return 0;
                                            }
                                        } catch (Exception exception) {
                                            context.getSource().sendFeedback(Text.of("§cUnknown entityType"), false);
                                            return 0;
                                        }

                                        this.nextEntity = entityType;
                                        context.getSource().sendFeedback(Text.of("Set next OneDeathBlock entity: " + Text.translatable(nextEntity.getTranslationKey()).getString()), true);
                                        return 1;
                                    }).then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound()).executes((context) -> {
                                        EntityType<?> entityType;
                                        try {
                                            Identifier identifier = EntitySummonArgumentType.getEntitySummon(context, "entity");
                                            entityType = Registry.ENTITY_TYPE.get(identifier);
                                            if (!identifier.equals(Registry.ENTITY_TYPE.getDefaultId())
                                                    && Registry.ENTITY_TYPE.get(Registry.ENTITY_TYPE.getDefaultId()).equals(entityType)) {

                                                context.getSource().sendFeedback(Text.of("§cUnknown entityType"), false);
                                                return 0;
                                            }
                                        } catch (Exception exception) {
                                            context.getSource().sendFeedback(Text.of("§cUnknown entityType"), false);
                                            return 0;
                                        }
                                        this.nextEntity = entityType;
                                        this.entityNbt = NbtCompoundArgumentType.getNbtCompound(context, "nbt");

                                        context.getSource().sendFeedback(Text.of("Set next OneDeathBlock entity: " + Text.translatable(nextEntity.getTranslationKey()).getString()), true);
                                        return 1;
                                    })))
                    ).then(CommandManager.literal("multiplier")
                            .then(CommandManager.argument("player", EntityArgumentType.players())
                                    .then(CommandManager.argument("multiplier", DoubleArgumentType.doubleArg(1)).executes((context) -> {
                                        double multiplier = DoubleArgumentType.getDouble(context, "multiplier");
                                        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                                        if (playerEntities != null) {
                                            playerEntities.forEach(serverPlayerEntity -> {
                                                context.getSource().sendFeedback(Text.of("Set " + serverPlayerEntity.getName().getString() + "'s deaths multiplier to §a" + multiplier), false);
                                                ((PlayerProperties) serverPlayerEntity).setDeathMultiplier(multiplier);
                                            });
                                        } else {
                                            context.getSource().sendFeedback(Text.of("§cUnknown players!"), false);
                                        }
                                        return 1;
                                    })))
                    ).then(CommandManager.literal("deaths")
                            .then(CommandManager.argument("player", EntityArgumentType.players())
                                    .then(CommandManager.argument("deaths", IntegerArgumentType.integer(0)).executes((context) -> {
                                        int deaths = IntegerArgumentType.getInteger(context, "deaths");
                                        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                                        if (playerEntities != null) {
                                            DecimalFormat format = new DecimalFormat("#,###");

                                            playerEntities.forEach(serverPlayerEntity -> {
                                                context.getSource().sendFeedback(Text.of("Set " + serverPlayerEntity.getName().getString() + "'s deaths to §a" + format.format(deaths)), false);
                                                ((PlayerProperties) serverPlayerEntity).setDeaths(deaths);
                                            });
                                        } else {
                                            context.getSource().sendFeedback(Text.of("§cUnknown players!"), false);
                                        }
                                        return 1;
                                    })))
                    ).then(CommandManager.literal("display")
                            .then(CommandManager.argument("player", EntityArgumentType.players())
                                    .executes((context) -> {
                                        Collection<ServerPlayerEntity> playerEntities = EntityArgumentType.getPlayers(context, "player");

                                        if (playerEntities != null) {
                                            playerEntities.forEach(serverPlayerEntity -> {
                                                context.getSource().sendFeedback(Text.of("Toggled " + serverPlayerEntity.getName().getString() + "'s deaths display"), false);
                                                ((PlayerProperties) serverPlayerEntity).setDeathsDisplay(!((PlayerProperties) serverPlayerEntity).hasDeathsDisplay());
                                            });
                                        } else {
                                            context.getSource().sendFeedback(Text.of("§cUnknown players!"), false);
                                        }
                                        return 1;
                                    }))
                    ).build();
            dispatcher.getRoot().addChild(rootNode);
        });

        ServerTickEvents.START_WORLD_TICK.register((world) -> {
            if (blockPos != null && this.world == world && !world.getBlockState(blockPos).isOf(oneBlock)) {
                world.setBlockState(blockPos, oneBlock.getDefaultState());
            }
        });

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (blockPos == null || this.world != world) {
                return true;
            }

            if (pos.getX() == blockPos.getX() && pos.getY() == blockPos.getY() && pos.getZ() == blockPos.getZ() && !world.isClient) {
                drop();
                return false;
            }
            return true;
        });
    }

    public void drop() {
        if (blockPos != null && world != null) {
            ItemStack itemStack = getRandomDrop();
            Block.dropStack(world, blockPos, Direction.UP, itemStack);

            if (this.nextEntity != null) {
                NbtCompound nbtCompound = this.entityNbt == null ? new NbtCompound() : this.entityNbt;
                nbtCompound.putString("id", Registry.ENTITY_TYPE.getId(this.nextEntity).toString());
                Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entity1 -> {
                    entity1.refreshPositionAndAngles(blockPos.getX() + 0.5, blockPos.getY() + 1.1, blockPos.getZ() + 0.5, entity1.getYaw(), entity1.getPitch());
                    return entity1;
                });

                if (entity instanceof MobEntity) {
                    ((MobEntity) entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWN_EGG, null, null);
                }

                this.nextEntity = null;
                this.entityNbt = null;

                if (entity != null) {
                    world.spawnEntity(entity);
                }
            }
        }
    }

    public ItemStack getRandomDrop() {
        ItemStack itemStack = QUEUE.poll();

        if (itemStack == null || itemStack.isEmpty()) {
            Optional<RegistryEntry<Item>> item = Registry.ITEM.getRandom(Random.create());
            itemStack = item.isEmpty() ? Items.DIRT.getDefaultStack() : item.get().value().getDefaultStack();
        }

        return itemStack;
    }
}
