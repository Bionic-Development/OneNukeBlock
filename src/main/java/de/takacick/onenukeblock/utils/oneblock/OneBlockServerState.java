package de.takacick.onenukeblock.utils.oneblock;

import de.takacick.onenukeblock.OneNukeBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OneBlockServerState extends PersistentState {

    private static final Type<OneBlockServerState> TYPE = new Type<>(
            OneBlockServerState::new,
            OneBlockServerState::createFromNbt,
            null
    );

    private final OneBlock oneBlock = new OneBlock();

    public void tick(MinecraftServer minecraftServer) {
        if (this.oneBlock.isDirty()) {
            this.oneBlock.setDirty(false);
            this.markDirty();
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        this.oneBlock.writeNbt(nbtCompound, registryLookup);
        nbt.put("oneBlock", nbtCompound);

        return nbt;
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("oneBlock", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = nbt.getCompound("oneBlock");
            this.oneBlock.readNbt(nbtCompound, registryLookup);
        }
    }

    public OneBlock getOneBlock() {
        return this.oneBlock;
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

        return Optional.of(persistentStateManager.getOrCreate(TYPE, OneNukeBlock.MOD_ID));
    }

    public static OneBlockServerState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        OneBlockServerState state = new OneBlockServerState();
        state.readNbt(tag, registryLookup);

        state.markDirty();
        return state;
    }
}