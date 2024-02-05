package de.takacick.secretcraftbase.registry.entity.custom;

import com.mojang.serialization.DataResult;
import de.takacick.secretcraftbase.server.datatracker.BezierCurve;
import de.takacick.secretcraftbase.server.datatracker.SecretCraftBaseTracker;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractBlockEntity extends Entity {

    protected static final TrackedData<BlockPos> BLOCK_POS = DataTracker.registerData(AbstractBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected static final TrackedData<BlockState> BLOCK_STATE = DataTracker.registerData(AbstractBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE);
    protected static final TrackedData<Float> SIZE = DataTracker.registerData(AbstractBlockEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected static final TrackedData<Float> PROGRESS = DataTracker.registerData(AbstractBlockEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected static final TrackedData<BezierCurve> BEZIER_CURVE = BionicDataTracker.registerData(new Identifier(BionicUtils.MOD_ID, "bezier_curve"), SecretCraftBaseTracker.BEZIER_CURVE);

    protected float size = getStartSize();
    protected float prevSize = getStartSize();
    public float progress = 0f;
    public float prevProgress = 0f;

    public AbstractBlockEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
        this.ignoreCameraFrustum = true;
    }

    abstract float getStartSize();

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(BLOCK_POS, BlockPos.ORIGIN);
        getDataTracker().startTracking(BLOCK_STATE, Blocks.AIR.getDefaultState());
        getDataTracker().startTracking(SIZE, getStartSize());
        getDataTracker().startTracking(PROGRESS, 0f);
        getDataTracker().startTracking(BEZIER_CURVE, new BezierCurve());
    }

    public void initializeTarget(Vec3d end) {
        getDataTracker().set(BEZIER_CURVE, new BezierCurve(getPos(), end, false));
    }

    public float getSize(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevSize, this.size);
    }

    public void setTrackedSize(float size) {
        getDataTracker().set(SIZE, size);
    }

    public float getTrackedSize() {
        return getDataTracker().get(SIZE);
    }

    public void setTrackedProgress(float progress) {
        getDataTracker().set(PROGRESS, progress);
    }

    public float getTrackedProgress() {
        return getDataTracker().get(PROGRESS);
    }

    public void setFromBlockPos(BlockPos fromBlockPos) {
        getDataTracker().set(BLOCK_POS, fromBlockPos);
    }

    public BlockPos getFromBlockPos() {
        return getDataTracker().get(BLOCK_POS);
    }

    public void setBlockState(BlockState blockState) {
        getDataTracker().set(BLOCK_STATE, blockState);
    }

    public BlockState getBlockState() {
        return getDataTracker().get(BLOCK_STATE);
    }

    public BezierCurve getBezierCurve() {
        return getDataTracker().get(BEZIER_CURVE);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("fromBlockPos", NbtHelper.fromBlockPos(getFromBlockPos()));
        nbt.put("blockState", NbtHelper.fromBlockState(getBlockState()));
        nbt.putFloat("prevSize", this.prevSize);
        nbt.putFloat("size", this.size);
        nbt.putFloat("prevProgress", this.prevProgress);
        nbt.putFloat("progress", this.progress);
        nbt.put("bezierCurve", this.getBezierCurve().writeNbt(new NbtCompound()));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("fromBlockPos", NbtElement.COMPOUND_TYPE)) {
            setFromBlockPos(NbtHelper.toBlockPos(nbt.getCompound("fromBlockPos")));
        }
        if (nbt.contains("blockState", NbtElement.COMPOUND_TYPE)) {
            DataResult<BlockState> blockStateResult = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockState"));
            setBlockState(blockStateResult.result().orElse(Blocks.AIR.getDefaultState()));
        }
        if (nbt.contains("prevSize", NbtCompound.FLOAT_TYPE)) {
            this.prevSize = nbt.getFloat("prevSize");
        }
        if (nbt.contains("size", NbtCompound.FLOAT_TYPE)) {
            this.size = nbt.getFloat("size");
            setTrackedProgress(this.size);
        }
        if (nbt.contains("prevProgress", NbtCompound.FLOAT_TYPE)) {
            this.prevProgress = nbt.getFloat("prevProgress");
        }
        if (nbt.contains("progress", NbtCompound.FLOAT_TYPE)) {
            this.progress = nbt.getFloat("progress");
            setTrackedProgress(this.progress);
        }
        if (nbt.contains("bezierCurve", NbtCompound.COMPOUND_TYPE)) {
            this.getBezierCurve().readNbt(nbt.getCompound("bezierCurve"));
        }
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
        if (getBezierCurve().getStart().getY() <= -100) {
            super.updateTrackedPositionAndAngles(x, y, z, yaw, pitch, interpolationSteps);
        }
    }

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance / 4f);
    }
}
