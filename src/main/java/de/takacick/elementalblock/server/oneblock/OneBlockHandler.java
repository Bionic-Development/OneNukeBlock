package de.takacick.elementalblock.server.oneblock;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.elementalblock.registry.block.ElementalBlock;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class OneBlockHandler {

    public static OneBlockHandler INSTANCE;

    private List<ElementalBlock> blocks = Arrays.asList(ItemRegistry.EARTH_ELEMENTAL_BLOCK, ItemRegistry.AIR_ELEMENTAL_BLOCK, ItemRegistry.WATER_ELEMENTAL_BLOCK, ItemRegistry.FIRE_ELEMENTAL_BLOCK);
    public static final Queue<ItemStack> QUEUE = new LinkedList<>();

    private final ServerBossBar bossBar = new ServerBossBar(Text.of("Progress: "), BossBar.Color.WHITE, BossBar.Style.PROGRESS);
    private ElementalBlock oneBlock = ItemRegistry.EARTH_ELEMENTAL_BLOCK;
    private int blocksBroken = 0;
    private int requiredBlocks = oneBlock.getRequiredBlocks();


    private ServerWorld world = null;
    private BlockPos blockPos = null;
    private EntityType<?> nextEntity;
    private NbtCompound entityNbt;

    public OneBlockHandler() {
        INSTANCE = this;

        ServerTickEvents.START_SERVER_TICK.register((server) -> {
            updateBossBar();
            server.getPlayerManager().getPlayerList().forEach(bossBar::addPlayer);

            if (this.world == null) {
                this.bossBar.setVisible(false);
            }
        });

        ServerTickEvents.START_WORLD_TICK.register((world) -> {
            if (blockPos != null && this.world == world) {
                if (!world.getBlockState(blockPos).isOf(oneBlock)) {
                    world.setBlockState(blockPos, oneBlock.getDefaultState());
                }

                this.requiredBlocks = oneBlock.getRequiredBlocks();
                checkUpdateBlock();
            }
        });

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (blockPos == null || this.world != world) {
                return true;
            }

            if (!world.isClient) {
                if (pos.getX() == blockPos.getX() && pos.getY() == blockPos.getY() && pos.getZ() == blockPos.getZ()) {

                    if (state.isOf(ItemRegistry.WATER_ELEMENTAL_BLOCK)) {
                        world.syncWorldEvent(821482, pos, 2);
                    } else if (state.isOf(ItemRegistry.AIR_ELEMENTAL_BLOCK)) {
                        world.syncWorldEvent(821482, pos, 3);
                    }

                    drop();

                    this.blocksBroken++;
                    checkUpdateBlock();
                    updateBossBar();

                    return false;
                }
            }
            return true;
        });


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LiteralCommandNode<ServerCommandSource> rootNode = CommandManager.literal("elementalblock")
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

                        playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> DropsScreenHandler.createGeneric9x6(syncId, inventory, simpleInventory), Text.of("OneElementalBlock Drops: ")));
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

                                this.blockPos = blockPos;
                                this.world = playerEntity.getServerWorld();
                                this.bossBar.setVisible(true);

                                context.getSource().sendFeedback(() -> Text.literal("Set OneElementalBlock to " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ()), true);

                                return 1;
                            }))
                    ).then(CommandManager.literal("teleport")
                            .executes(context -> {

                                if (world == null || blockPos == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("§cCouldn't find OneElementalBlock"), false);
                                    return 0;
                                }

                                ServerPlayerEntity playerEntity = context.getSource().getPlayer();

                                if (playerEntity == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("§cUnknown player"), false);
                                    return 0;
                                }

                                playerEntity.teleport(world, blockPos.getX() + 0.5, blockPos.getY() + 1.1, blockPos.getZ() + 0.5, playerEntity.getYaw(), playerEntity.getPitch());
                                context.getSource().sendFeedback(() -> Text.literal("Teleport to OneElementalBlock"), true);

                                return 1;
                            })
                    ).then(CommandManager.literal("togglebar")
                            .executes(context -> {

                                if (world == null || blockPos == null) {
                                    context.getSource().sendFeedback(() -> Text.literal("§cCouldn't find OneElementalBlock"), false);
                                    return 0;
                                }

                                this.bossBar.setVisible(this.bossBar.isVisible());
                                context.getSource().sendFeedback(() -> Text.literal("Toggled OneElementalBlock Progressbar"), false);

                                return 1;
                            })
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

                                        this.nextEntity = entityType;
                                        context.getSource().sendFeedback(() -> Text.of("Set next OneElementalBlock entity: " + Text.translatable(nextEntity.getTranslationKey()).getString()), true);
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
                                        this.nextEntity = entityType;
                                        this.entityNbt = NbtCompoundArgumentType.getNbtCompound(context, "nbt");

                                        context.getSource().sendFeedback(() -> Text.of("Set next OneElementalBlock entity: " + Text.translatable(nextEntity.getTranslationKey()).getString()), true);
                                        return 1;
                                    })))
                    ).build();

            LiteralCommandNode<ServerCommandSource> node = CommandManager.literal("progress")
                    .requires(source -> {
                        return source.hasPermissionLevel(2);
                    }).then(CommandManager.literal("maxBlocks")
                            .then(CommandManager.argument("element", StringArgumentType.word()).suggests(getOneBlockSuggestions())
                                    .then(CommandManager.argument("blocks", IntegerArgumentType.integer(1))
                                            .executes(context -> {
                                                int blocks = IntegerArgumentType.getInteger(context, "blocks");
                                                String element = StringArgumentType.getString(context, "element");
                                                ElementalBlock elementalBlock = getElement(element);

                                                if (elementalBlock == null) {
                                                    context.getSource().sendFeedback(() -> Text.of("§cUnknown Element!"), false);
                                                    return 0;
                                                }
                                                elementalBlock.setRequiredBlocks(blocks);
                                                this.requiredBlocks = elementalBlock.getRequiredBlocks();

                                                context.getSource().sendFeedback(() -> Text.literal("Set " + elementalBlock.getElement() + " required Blocks to " + blocks + " blocks "), true);
                                                checkUpdateBlock();
                                                updateBossBar();

                                                return 1;
                                            })))
                    ).then(CommandManager.literal("blocks")
                            .then(CommandManager.argument("blocks", IntegerArgumentType.integer(0))
                                    .executes(context -> {
                                        int blocks = IntegerArgumentType.getInteger(context, "blocks");

                                        if (world == null) {
                                            context.getSource().sendFeedback(() -> Text.literal("§cCouldn't find OneElementalBlock"), false);
                                            return 0;
                                        }
                                        this.blocksBroken = blocks;
                                        context.getSource().sendFeedback(() -> Text.literal("Set " + " progress to " + blocks + " blocks "), true);
                                        checkUpdateBlock();
                                        updateBossBar();
                                        return 1;
                                    }))
                    ).build();

            rootNode.addChild(node);

            dispatcher.getRoot().addChild(rootNode);
        });
    }

    public void drop() {
        if (blockPos != null && world != null) {
            ItemStack itemStack = getRandomDrop();
            dropStack(world, blockPos, Direction.UP, itemStack);

            if (this.nextEntity != null) {
                NbtCompound nbtCompound = this.entityNbt == null ? new NbtCompound() : this.entityNbt;
                nbtCompound.putString("id", Registries.ENTITY_TYPE.getId(this.nextEntity).toString());
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
            Optional<RegistryEntry.Reference<Item>> item = Registries.ITEM.getRandom(Random.create());
            itemStack = item.isEmpty() ? Items.DIRT.getDefaultStack() : item.get().value().getDefaultStack();
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

    private ElementalBlock getNextBlock() {
        if (oneBlock == null) {
            return ItemRegistry.EARTH_ELEMENTAL_BLOCK;
        } else if (oneBlock.equals(ItemRegistry.EARTH_ELEMENTAL_BLOCK)) {
            return ItemRegistry.AIR_ELEMENTAL_BLOCK;
        } else if (oneBlock.equals(ItemRegistry.AIR_ELEMENTAL_BLOCK)) {
            return ItemRegistry.WATER_ELEMENTAL_BLOCK;
        } else if (oneBlock.equals(ItemRegistry.WATER_ELEMENTAL_BLOCK)) {
            return ItemRegistry.FIRE_ELEMENTAL_BLOCK;
        }

        return ItemRegistry.EARTH_ELEMENTAL_BLOCK;
    }

    private int getElementId() {
        if (oneBlock == null || oneBlock.equals(ItemRegistry.EARTH_ELEMENTAL_BLOCK)) {
            return 0;
        } else if (oneBlock.equals(ItemRegistry.AIR_ELEMENTAL_BLOCK)) {
            return 1;
        } else if (oneBlock.equals(ItemRegistry.WATER_ELEMENTAL_BLOCK)) {
            return 2;
        }

        return 3;
    }

    private void updateBossBar() {
        this.bossBar.setName(this.oneBlock.getBossBarTitle());
        this.bossBar.setColor(this.oneBlock.getBossBarColor());
        this.bossBar.setPercent(MathHelper.clamp((float) this.blocksBroken / this.requiredBlocks, 0f, 1f));
    }

    private void checkUpdateBlock() {
        if (this.world != null && this.blockPos != null) {
            if (this.blocksBroken >= this.requiredBlocks) {
                world.syncWorldEvent(821482, blockPos, 4 + getElementId());
                ElementalBlock nextBlock = getNextBlock();
                this.blocksBroken = 0;
                this.requiredBlocks = nextBlock.getRequiredBlocks();
                this.oneBlock = nextBlock;

                world.setBlockState(blockPos, oneBlock.getDefaultState());
            }
        }
    }

    private <S> SuggestionProvider<S> getOneBlockSuggestions() {
        return (context, builder) -> {
            List<String> entries = new LinkedList<>(blocks.stream().map(ElementalBlock::getElement).toList());

            return CommandSource.suggestMatching(entries, builder);
        };
    }

    private ElementalBlock getElement(String element) {
        return blocks.stream().filter(elementalBlock -> elementalBlock.getElement().equalsIgnoreCase(element)).findFirst().orElse(null);
    }
}
