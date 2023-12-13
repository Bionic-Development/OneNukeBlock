package de.takacick.upgradebody.registry.entity.custom;

import de.takacick.upgradebody.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class BlockBreakEntity extends Entity {

    protected float breakProgress;
    protected float prevBreakProgress = -1;
    private int lastMined = 2;

    public BlockBreakEntity(EntityType<? extends BlockBreakEntity> entityType, World world) {
        super(entityType, world);
    }

    public BlockBreakEntity(World world, double x, double y, double z) {
        this(EntityRegistry.BLOCK_BREAK, world);
        this.setPosition(x, y, z);
    }

    public static BlockBreakEntity create(EntityType<BlockBreakEntity> entityType, World world) {
        return new BlockBreakEntity(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        return true;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void tick() {

        if (getWorld().getBlockState(getBlockPos()).isAir()) {
            this.discard();
            return;
        }


        if (!getWorld().isClient) {
            float i = this.breakProgress;
            if (i != this.prevBreakProgress) {
                lastMined = 100;
                getWorld().setBlockBreakingInfo(getId(), getBlockPos(), (int) (i * 10));
                this.prevBreakProgress = i;
            }

            if (lastMined <= 0) {
                this.breakProgress -= 0.1f;
            } else {
                lastMined--;
            }

            if (this.breakProgress >= getMaxBreakProgress()) {
                this.discard();
            } else if (this.breakProgress <= 0) {
                this.discard();
            }
        }
    }

    @Override
    public void onRemoved() {

        if (getWorld().isClient) {
            getWorld().setBlockBreakingInfo(this.getId(), getBlockPos(), -1);
        }

        super.onRemoved();
    }

    public void setBreakProgress(float breakProgress) {
        this.breakProgress = breakProgress;
    }

    public float getBreakProgress() {
        return breakProgress;
    }

    public float getMaxBreakProgress() {
        return 1;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

}

