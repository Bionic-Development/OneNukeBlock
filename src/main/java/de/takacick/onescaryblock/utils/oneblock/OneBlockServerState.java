package de.takacick.onescaryblock.utils.oneblock;

import de.takacick.onescaryblock.OneScaryBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class OneBlockServerState extends PersistentState {

    private static final Type<OneBlockServerState> TYPE = new Type<>(
            OneBlockServerState::new,
            OneBlockServerState::createFromNbt,
            null
    );

    private Queue<ItemStack> drops = new LinkedList<>();
    private EntityType<?> nextEntity;
    private NbtCompound entityNbt;
    private boolean scaryOneBlockRitual;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (!this.drops.isEmpty()) {
            NbtList nbtList = new NbtList();
            this.drops.forEach(itemStack -> {
                nbtList.add(itemStack.writeNbt(new NbtCompound()));
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

        if (this.scaryOneBlockRitual) {
            nbt.putBoolean("scaryOneBlockRitual", this.scaryOneBlockRitual);
        }

        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("drops", NbtElement.LIST_TYPE)) {
            nbt.getList("drops", NbtElement.COMPOUND_TYPE).forEach(nbtElement -> {
                if (nbtElement instanceof NbtCompound nbtCompound) {
                    this.drops.add(ItemStack.fromNbt(nbtCompound));
                }
            });
        }

        if (nbt.contains("entity", NbtElement.COMPOUND_TYPE)) {
            NbtCompound entity = nbt.getCompound("entity");

            Registries.ENTITY_TYPE.getOrEmpty(new Identifier(entity.getString("type"))).ifPresent(entityType -> {
                this.nextEntity = entityType;

                if (entity.contains("data", NbtElement.COMPOUND_TYPE)) {
                    this.entityNbt = entity.getCompound("data");
                }
            });
        }

        this.scaryOneBlockRitual = nbt.getBoolean("scaryOneBlockRitual");
    }

    public void setScaryOneBlockRitual(boolean scaryOneBlockRitual) {
        this.scaryOneBlockRitual = scaryOneBlockRitual;
        this.markDirty();
    }

    public boolean isScaryOneBlockRitual() {
        return this.scaryOneBlockRitual;
    }

    public void setDrops(Queue<ItemStack> drops) {
        this.drops = drops;
        this.markDirty();
    }

    public Queue<ItemStack> getDrops() {
        return this.drops;
    }

    public void setNextEntity(EntityType<?> nextEntity, NbtCompound entityNbt) {
        this.nextEntity = nextEntity;
        this.entityNbt = entityNbt;
        this.markDirty();
    }

    public EntityType<?> getNextEntity() {
        return this.nextEntity;
    }

    public NbtCompound getEntityNbt() {
        return this.entityNbt;
    }

    public static Optional<OneBlockServerState> getServerState(@Nullable MinecraftServer server) {
        if (server == null) {
            return Optional.empty();
        }
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        if (serverWorld == null) {
            return Optional.empty();
        }
        PersistentStateManager persistentStateManager = serverWorld.getPersistentStateManager();

        return Optional.of(persistentStateManager.getOrCreate(TYPE, OneScaryBlock.MOD_ID));
    }

    public static OneBlockServerState createFromNbt(NbtCompound tag) {
        OneBlockServerState state = new OneBlockServerState();
        state.readNbt(tag);

        state.markDirty();
        return state;
    }
}