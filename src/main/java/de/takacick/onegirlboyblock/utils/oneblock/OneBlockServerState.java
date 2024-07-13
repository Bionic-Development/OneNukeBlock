package de.takacick.onegirlboyblock.utils.oneblock;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
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

    private final OneBlock girlOneBlock = new OneBlock();
    private final OneBlock boyOneBlock = new OneBlock();

    public void tick(MinecraftServer minecraftServer) {
        if (this.girlOneBlock.isDirty() || this.boyOneBlock.isDirty()) {
            this.girlOneBlock.setDirty(false);
            this.boyOneBlock.setDirty(false);
            this.markDirty();
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        this.girlOneBlock.writeNbt(nbtCompound, registryLookup);
        nbt.put("girlOneBlock", nbtCompound);

        nbtCompound = new NbtCompound();
        this.boyOneBlock.writeNbt(nbtCompound, registryLookup);
        nbt.put("boyOneBlock", nbtCompound);

        return nbt;
    }

    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains("girlOneBlock", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = nbt.getCompound("girlOneBlock");
            this.girlOneBlock.readNbt(nbtCompound, registryLookup);
        }

        if (nbt.contains("boyOneBlock", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = nbt.getCompound("boyOneBlock");
            this.boyOneBlock.readNbt(nbtCompound, registryLookup);
        }
    }

    public OneBlock getGirlOneBlock() {
        return this.girlOneBlock;
    }

    public OneBlock getBoyOneBlock() {
        return this.boyOneBlock;
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

        return Optional.of(persistentStateManager.getOrCreate(TYPE, OneGirlBoyBlock.MOD_ID));
    }

    public static OneBlockServerState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        OneBlockServerState state = new OneBlockServerState();
        state.readNbt(tag, registryLookup);

        state.markDirty();
        return state;
    }
}