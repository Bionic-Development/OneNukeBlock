package de.takacick.illegalwars.registry.block.entity;

import com.mojang.logging.LogUtils;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.block.entity.spawner.TrialSpawnerStateHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrialSpawnerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.Spawner;
import net.minecraft.block.enums.TrialSpawnerState;
import net.minecraft.block.spawner.EntityDetector;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.slf4j.Logger;

public class CyberWardenSecurityTrialSpawnerBlockEntity
        extends BlockEntity
        implements Spawner,
        TrialSpawnerLogic.TrialSpawner {

    private static final Logger LOGGER = LogUtils.getLogger();
    private TrialSpawnerLogic spawner;

    public CyberWardenSecurityTrialSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.CYBER_WARDEN_SECURITY_TRIAL_SPAWNER, pos, state);
        EntityDetector entityDetector = EntityDetector.SURVIVAL_PLAYER;

        this.spawner = new TrialSpawnerLogic(TrialSpawnerStateHandler.TRIAL_SPAWNER_CONFIG, new TrialSpawnerData(), this, entityDetector);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.spawner.codec().parse(NbtOps.INSTANCE, nbt).resultOrPartial(LOGGER::error).ifPresent(trialSpawnerLogic -> {
            this.spawner = trialSpawnerLogic;
        });
        if (this.world != null) {
            this.updateListeners();
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.spawner.codec().encodeStart(NbtOps.INSTANCE, this.spawner).get().ifLeft(nbtElement -> nbt.copyFrom((NbtCompound) nbtElement)).ifRight(partialResult -> LOGGER.warn("Failed to encode TrialSpawner {}", (Object) partialResult.message()));
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.spawner.getData().getSpawnDataNbt(this.getCachedState().get(TrialSpawnerBlock.TRIAL_SPAWNER_STATE));
    }

    @Override
    public boolean copyItemDataRequiresOperator() {
        return true;
    }

    @Override
    public void setEntityType(EntityType<?> type, Random random) {
        this.spawner.getData().setEntityType(this.spawner, random, type);
        this.markDirty();
    }

    public TrialSpawnerLogic getSpawner() {
        return this.spawner;
    }

    @Override
    public TrialSpawnerState getSpawnerState() {
        if (!this.getCachedState().contains(Properties.TRIAL_SPAWNER_STATE)) {
            return TrialSpawnerState.INACTIVE;
        }
        return this.getCachedState().get(Properties.TRIAL_SPAWNER_STATE);
    }

    @Override
    public void setSpawnerState(World world, TrialSpawnerState spawnerState) {
        this.markDirty();
        world.setBlockState(this.pos, this.getCachedState().with(Properties.TRIAL_SPAWNER_STATE, spawnerState));
    }

    @Override
    public void updateListeners() {
        this.markDirty();
        if (this.world != null) {
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
        }
    }
}

