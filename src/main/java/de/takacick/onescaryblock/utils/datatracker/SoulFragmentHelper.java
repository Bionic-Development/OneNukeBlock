package de.takacick.onescaryblock.utils.datatracker;

import de.takacick.onescaryblock.registry.ParticleRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SoulFragmentHelper {

    protected final int maxTicks = 200;
    protected int tick = 0;
    protected int prevTick = 0;
    private boolean dirty;

    public SoulFragmentHelper() {

    }

    public void tick(PlayerEntity playerEntity) {
        this.prevTick = this.tick;
        this.tick = MathHelper.clamp(this.tick - 1, 0, this.getMaxTicks());

        World world = playerEntity.getWorld();

        if (this.tick <= 0 && this.prevTick > 0) {
            if (!(playerEntity.isSpectator() || playerEntity.isCreative())) {
                playerEntity.getAbilities().allowFlying = false;
                playerEntity.getAbilities().flying = false;
                playerEntity.sendAbilitiesUpdate();
            }
        } else if(isRunning()){
            if (!playerEntity.getAbilities().allowFlying) {
                playerEntity.getAbilities().allowFlying = true;
                playerEntity.getAbilities().flying = true;
                playerEntity.sendAbilitiesUpdate();
            }

            if (playerEntity.getWorld().isClient) {
                Random random = world.getRandom();
                for (int i = 0; i < 1; ++i) {
                    Vec3d vel = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().multiply(0.1 + world.getRandom().nextDouble() * 0.5);

                    world.addParticle(ParticleRegistry.SOUL, true,
                            playerEntity.getX() + vel.getX(), playerEntity.getY() + vel.getY(), playerEntity.getZ() + vel.getZ(),
                            vel.getX() * 0.01, vel.getY()  * 0.01, vel.getZ() * 0.01);


                    if (random.nextDouble() <= 0.3) {
                        world.playSound(playerEntity.getX() + vel.getX(), playerEntity.getY() + vel.getY(), playerEntity.getZ() + vel.getZ(),
                                SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 4.0f,
                                (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
                    }
                }
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

    public void sync(SoulFragmentHelper animationHelper) {
        if (Math.abs(animationHelper.getTick() - this.tick) > 1) {
            this.tick = animationHelper.getTick();
            this.prevTick = animationHelper.getPrevTick();
        }
    }

    public static SoulFragmentHelper fromNbt(NbtCompound nbt) {
        SoulFragmentHelper bloodBorderSuitHelper = new SoulFragmentHelper();
        bloodBorderSuitHelper.readNbt(nbt);
        return bloodBorderSuitHelper;
    }

    public static SoulFragmentHelper copy(SoulFragmentHelper animationHelper) {
        SoulFragmentHelper bloodBorderSuitHelper = new SoulFragmentHelper();
        bloodBorderSuitHelper.tick = animationHelper.getTick();
        bloodBorderSuitHelper.prevTick = animationHelper.getPrevTick();

        return bloodBorderSuitHelper;
    }
}
