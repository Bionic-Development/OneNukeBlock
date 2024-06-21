package de.takacick.onescaryblock.utils.datatracker;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.MathHelper;

public class BloodBorderSuitHelper {

    protected int maxTicks = 10;
    protected int tick = 0;
    protected int prevTick = 0;
    private boolean dirty;

    public BloodBorderSuitHelper() {

    }

    public void tick() {
        this.prevTick = this.tick;
        this.tick = MathHelper.clamp(this.tick - 1, 0, this.getMaxTicks());

        if (this.tick != this.prevTick) {
            this.dirty = true;
        }
    }

    public void setTick(int tick) {
        this.tick = tick;
        this.prevTick = this.tick;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public int getMaxTicks() {
        return this.maxTicks;
    }

    public int getTick() {
        return this.tick;
    }

    public float getTick(float tickDelta) {
        return MathHelper.lerp(tickDelta, (float) this.prevTick, this.tick);
    }

    public int getPrevTick() {
        return this.prevTick;
    }

    public float getProgress(float tickDelta) {
        return MathHelper.clamp(getTick(tickDelta) / this.maxTicks * 4f, 0f, 1f);
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("tick", this.tick);
        nbt.putInt("prevTick", this.prevTick);
        nbt.putInt("maxTicks", this.maxTicks);

        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("tick", NbtElement.INT_TYPE)) {
            this.tick = nbt.getInt("tick");
        } else {
            this.tick = 0;
        }

        if (nbt.contains("prevTick", NbtElement.INT_TYPE)) {
            this.prevTick = nbt.getInt("prevTick");
        } else {
            this.prevTick = 0;
        }

        if (nbt.contains("maxTicks", NbtElement.INT_TYPE)) {
            this.maxTicks = nbt.getInt("maxTicks");
        }
    }

    public void sync(BloodBorderSuitHelper animationHelper) {
        if (Math.abs(animationHelper.getTick() - this.tick) > 1) {
            this.tick = animationHelper.getTick();
            this.prevTick = animationHelper.getPrevTick();
        }
        this.maxTicks = animationHelper.getMaxTicks();
    }

    public static BloodBorderSuitHelper fromNbt(NbtCompound nbt) {
        BloodBorderSuitHelper bloodBorderSuitHelper = new BloodBorderSuitHelper();
        bloodBorderSuitHelper.readNbt(nbt);
        return bloodBorderSuitHelper;
    }

    public static BloodBorderSuitHelper copy(BloodBorderSuitHelper animationHelper) {
        BloodBorderSuitHelper bloodBorderSuitHelper = new BloodBorderSuitHelper();
        bloodBorderSuitHelper.tick = animationHelper.getTick();
        bloodBorderSuitHelper.prevTick = animationHelper.getPrevTick();
        bloodBorderSuitHelper.maxTicks = animationHelper.getMaxTicks();

        return bloodBorderSuitHelper;
    }
}
