package de.takacick.onenukeblock.utils.oneblock;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.ItemRegistry;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Supplier;

public class OneBlock {

    private Queue<ItemStack> drops = new LinkedList<>();
    private EntityType<?> nextEntity;
    private NbtCompound entityNbt;
    private boolean dirty;

    public OneBlock() {

    }

    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (!this.drops.isEmpty()) {
            NbtList nbtList = new NbtList();

            RegistryOps<NbtElement> registryOps = registryLookup.getOps(NbtOps.INSTANCE);
            this.drops.forEach(itemStack -> {
                ItemStack.CODEC.encodeStart(registryOps, itemStack).ifSuccess(nbtList::add);
            });

            nbt.put("drops", nbtList);
        }

        if (this.nextEntity != null) {
            NbtCompound entity = new NbtCompound();
            entity.putString("type", EntityType.getId(this.nextEntity).toString());
            if (this.entityNbt != null) {
                entity.put("data", this.entityNbt);
            }

            nbt.put("entity", entity);
        }

        return nbt;
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("drops", NbtElement.LIST_TYPE)) {
            RegistryOps<NbtElement> registryOps = registryLookup.getOps(NbtOps.INSTANCE);
            nbt.getList("drops", NbtElement.COMPOUND_TYPE).forEach(nbtElement -> {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    ItemStack.CODEC.parse(registryOps, nbtCompound).ifSuccess(itemStack -> {
                        this.drops.add(itemStack);
                    });
                }
            });
        }

        if (nbt.contains("entity", NbtElement.COMPOUND_TYPE)) {
            NbtCompound entity = nbt.getCompound("entity");

            Registries.ENTITY_TYPE.getOrEmpty(Identifier.of(entity.getString("type"))).ifPresent(entityType -> {
                this.nextEntity = entityType;

                if (entity.contains("data", NbtElement.COMPOUND_TYPE)) {
                    this.entityNbt = entity.getCompound("data");
                }
            });
        }
    }


    public void setDrops(Queue<ItemStack> drops) {
        this.drops = drops;
        this.setDirty(true);
    }

    public Queue<ItemStack> getDrops() {
        return this.drops;
    }

    public void setNextEntity(EntityType<?> nextEntity, NbtCompound entityNbt) {
        this.nextEntity = nextEntity;
        this.entityNbt = entityNbt;
        this.setDirty(true);
    }

    public EntityType<?> getNextEntity() {
        return this.nextEntity;
    }

    public NbtCompound getEntityNbt() {
        return this.entityNbt;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }


    public void drop(World world, BlockPos blockPos) {
        ItemStack itemStack = getRandomDrop();
        dropStack(world, blockPos, Direction.UP, itemStack);

        EntityType<?> nextEntity = getNextEntity();
        NbtCompound entityNbt = getEntityNbt();

        if (nextEntity != null) {
            NbtCompound nbtCompound = entityNbt == null ? new NbtCompound() : entityNbt;
            nbtCompound.putString("id", Registries.ENTITY_TYPE.getId(nextEntity).toString());
            Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entity1 -> {
                entity1.refreshPositionAndAngles(blockPos.getX() + 0.5, blockPos.getY() + 1.1, blockPos.getZ() + 0.5, entity1.getYaw(), entity1.getPitch());
                return entity1;
            });

            if (entity instanceof MobEntity && world instanceof ServerWorld serverWorld) {
                ((MobEntity) entity).initialize(serverWorld, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWN_EGG, null);
            }

            setNextEntity(null, null);

            if (entity != null) {
                world.spawnEntity(entity);
            }
        }

        setDirty(true);
        EventHandler.sendWorldStatus(world, blockPos.toCenterPos(), OneNukeBlock.IDENTIFIER, 3, 0);
    }

    public ItemStack getRandomDrop() {
        ItemStack itemStack = getDrops().poll();
        setDirty(true);

        if (itemStack == null || itemStack.isEmpty()) {
            Optional<RegistryEntry.Reference<Item>> reference = Registries.ITEM.getRandom(Random.create());
            Item item = reference.isEmpty() ? Items.AIR : reference.get().value();

            if (reference.isEmpty() || item.equals(ItemRegistry.ONE_NUKE_BLOCK_ITEM)) {
                return getRandomDrop();
            }

            itemStack = item.getDefaultStack();
        }

        return itemStack;
    }

    public static void dropStack(World world, BlockPos pos, Direction direction, ItemStack stack) {
        if (stack == null) {
            return;
        }

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