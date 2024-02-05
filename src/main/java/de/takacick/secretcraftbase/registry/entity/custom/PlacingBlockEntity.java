package de.takacick.secretcraftbase.registry.entity.custom;

import de.takacick.secretcraftbase.server.datatracker.BezierCurve;
import de.takacick.secretcraftbase.server.utils.WorldChunkUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlacingBlockEntity extends AbstractBlockEntity {

    private boolean placed = false;
    private NbtCompound nbtCompound;

    public PlacingBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    float getStartSize() {
        return 0.25f;
    }

    @Override
    public void initializeTarget(Vec3d end) {
        getDataTracker().set(BEZIER_CURVE, new BezierCurve(getPos(), end, true));
    }

    @Override
    public void tick() {
        this.prevSize = this.size;
        this.prevProgress = this.progress;

        this.progress = MathHelper.clamp(this.progress + 0.05f, 0f, 1.9f);

        if (Math.abs(getTrackedProgress() - this.progress) > 0.1f) {
            this.progress = getTrackedProgress();
        }

        BezierCurve bezierCurve = getBezierCurve();
        Vec3d prevPos = bezierCurve.getPos(this.prevProgress);
        Vec3d pos = bezierCurve.getPos(this.progress);

        this.prevX = prevPos.getX();
        this.prevY = prevPos.getY();
        this.prevZ = prevPos.getZ();
        this.setPos(pos.getX(), pos.getY(), pos.getZ());

        if (this.progress >= 0.75f) {
            this.size = MathHelper.clamp(this.size + 0.05f, 0.25f, 1f);

            if (Math.abs(getTrackedSize() - this.size) > 0.1f) {
                this.size = getTrackedSize();
            }

            if (!getWorld().isClient) {
                if (this.progress >= 1.8f && this.size >= 1f) {
                    BlockPos targetPos = BlockPos.ofFloored(bezierCurve.getPos(1f));
                    if (getWorld().getBlockState(targetPos).equals(getBlockState())) {
                        this.discard();
                    }

                    WorldChunkUtils.setBlockState(getWorld(), targetPos, getBlockState(), false);

                    BlockEntity blockEntity;
                    if (this.nbtCompound != null && (blockEntity = getWorld().getBlockEntity(targetPos)) != null) {
                        blockEntity.createNbt();
                        blockEntity.readNbt(this.nbtCompound);
                    }
                }
            } else if (this.progress >= 1f && this.size >= 1f) {
                if (random.nextDouble() <= 0.2 && !this.placed) {
                    this.placed = true;
                    BlockSoundGroup soundGroup = getBlockState().getSoundGroup();
                    getWorld().playSound(getX(), getY(), getZ(), soundGroup.getPlaceSound(),
                            SoundCategory.BLOCKS, (soundGroup.getVolume() + 1.0f) / 2.0f, soundGroup.getPitch() * 0.8f, false);
                }
            }
        }

        if (!getWorld().isClient) {
            setTrackedSize(this.size);
            setTrackedProgress(this.progress);
        }

        super.tick();
    }

    public void setNbtCompound(NbtCompound nbtCompound) {
        this.nbtCompound = nbtCompound;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if(this.nbtCompound != null) {
            nbt.put("blockNbt", this.nbtCompound);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("blockNbt", NbtCompound.COMPOUND_TYPE)) {
            this.nbtCompound = nbt.getCompound("blockNbt");
        }
    }
}
