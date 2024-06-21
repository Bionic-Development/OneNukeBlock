package de.takacick.onescaryblock.utils.datatracker;

import de.takacick.onescaryblock.OneScaryBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class HerobrineLightningDamageHelper {
    public static final RegistryKey<DamageType> HEROBRINE_LIGHTNING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(OneScaryBlock.MOD_ID, "herobrine_lightning"));

    protected final int maxTicks = 20;
    protected int tick = 0;
    protected int prevTick = 0;
    private boolean dirty;

    public HerobrineLightningDamageHelper() {

    }

    public void tick(LivingEntity livingEntity) {
        this.prevTick = this.tick;
        this.tick = MathHelper.clamp(this.tick - 1, 0, this.getMaxTicks());

        World world = livingEntity.getWorld();
        if (this.tick > 0) {
            if (!world.isClient) {
                livingEntity.damage(world.getDamageSources().create(HEROBRINE_LIGHTNING, livingEntity), 0.15f);
            }
        }

        if (this.tick != this.prevTick) {
            this.dirty = true;
        }
    }

    public boolean isRunning() {
        return this.tick > 0;
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
    }

    public void sync(HerobrineLightningDamageHelper animationHelper) {
        if (Math.abs(animationHelper.getTick() - this.tick) > 1) {
            this.tick = animationHelper.getTick();
            this.prevTick = animationHelper.getPrevTick();
        }
    }

    public static HerobrineLightningDamageHelper fromNbt(NbtCompound nbt) {
        HerobrineLightningDamageHelper bloodBorderSuitHelper = new HerobrineLightningDamageHelper();
        bloodBorderSuitHelper.readNbt(nbt);
        return bloodBorderSuitHelper;
    }

    public static HerobrineLightningDamageHelper copy(HerobrineLightningDamageHelper animationHelper) {
        HerobrineLightningDamageHelper bloodBorderSuitHelper = new HerobrineLightningDamageHelper();
        bloodBorderSuitHelper.tick = animationHelper.getTick();
        bloodBorderSuitHelper.prevTick = animationHelper.getPrevTick();

        return bloodBorderSuitHelper;
    }
}
