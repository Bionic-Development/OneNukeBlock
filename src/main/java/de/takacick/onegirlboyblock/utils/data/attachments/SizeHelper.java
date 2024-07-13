package de.takacick.onegirlboyblock.utils.data.attachments;

import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.onegirlboyblock.registry.particles.EntityParticleEffect;
import de.takacick.utils.data.attachment.AnimationSyncHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class SizeHelper extends AnimationSyncHelper<LivingEntity> {

    private boolean boyEffect = false;

    public SizeHelper() {
        super(1);
    }

    @Override
    public void tick(LivingEntity livingEntity) {
        this.prevTick = this.tick;
        this.tick--;
        this.running = true;
        if (!livingEntity.getWorld().isClient) {
            ScaleData scaleData = ScaleTypes.BASE.getScaleData(livingEntity);
            if (this.tick > 0) {
                float scale = 1.75f;
                if (scaleData.getTargetScale() != scale && scaleData.getScale() != scale) {
                    scaleData.setTargetScale(scale);
                }
            } else {
                float scale = 1f;
                if (scaleData.getTargetScale() != scale && scaleData.getScale() != scale) {
                    scaleData.setTargetScale(scale);
                }

                if (Math.abs(scaleData.getScale() - scale) <= 0.01) {
                    this.running = false;
                }
            }
        } else {

            ScaleData scaleData = ScaleTypes.BASE.getScaleData(livingEntity);
            World world = livingEntity.getWorld();
            Random random = livingEntity.getRandom();

            float scale = this.tick > 0 ? 1.75f : 1f;

            if (isBoyEffect()) {
                if (this.tick == 0) {
                    livingEntity.getWorld().playSound(livingEntity.getX(), livingEntity.getBodyY(0.5), livingEntity.getZ(),
                            ParticleRegistry.SHRINK_SOUND, livingEntity.getSoundCategory(), 0.75f, 1f, true);
                }

                if (this.tick > 0) {
                    if (scale > scaleData.getScale()) {
                        for (int i = 0; i < 2; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new EntityParticleEffect(ParticleRegistry.GROW, livingEntity.getId()),
                                    true, livingEntity.getX() + vel.getX() * livingEntity.getWidth() * 0.6, livingEntity.getRandomBodyY(), livingEntity.getZ() + vel.getZ() * livingEntity.getWidth() * 0.6,
                                    vel.getX() * 0.3, 1, vel.getZ() * 0.3);
                        }
                    }
                } else {
                    if (scale < scaleData.getScale()) {
                        for (int i = 0; i < 2; ++i) {
                            Vec3d vel = new Vec3d(random.nextGaussian(), random.nextDouble(), random.nextGaussian());

                            world.addParticle(new EntityParticleEffect(ParticleRegistry.SHRINK, livingEntity.getId()),
                                    true, livingEntity.getX() + vel.getX() * livingEntity.getWidth() * 0.6, livingEntity.getRandomBodyY(), livingEntity.getZ() + vel.getZ() * livingEntity.getWidth() * 0.6,
                                    vel.getX() * 0.3, 1, vel.getZ() * 0.3);
                        }
                        this.boyEffect = true;
                    }
                }
            } else {
                if (Math.abs(scaleData.getScale() - scale) >= 0.01) {
                    if (this.tick % 2 == 0) {
                        livingEntity.getWorld().playSound(livingEntity.getX(), livingEntity.getBodyY(0.5), livingEntity.getZ(),
                                SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, livingEntity.getSoundCategory(), 1.5f, 1f, true);
                    }

                    Vec3d vec3d = livingEntity.getRotationVector(0f, this.tick * 20);

                    world.addParticle(new EntityParticleEffect(ParticleRegistry.GIRL_SIZE, livingEntity.getId()),
                            livingEntity.getX() + vec3d.getX() * 1f,
                            livingEntity.getBodyY(0.5) ,
                            livingEntity.getZ() + vec3d.getZ() * 1f,
                            0, 0, 0);

                    world.addParticle(new EntityParticleEffect(ParticleRegistry.GIRL_SIZE, livingEntity.getId()),
                            livingEntity.getX() + vec3d.getX() * -1f,
                            livingEntity.getBodyY(0.5),
                            livingEntity.getZ() + vec3d.getZ() * -1f,
                            0, 0, 0);

                }
            }
        }
    }

    public void setTick(int tick) {
        if (this.tick != tick) {
            this.dirty = true;
        }
        this.tick = tick;
    }

    public void setPrevTick(int prevTick) {
        if (this.prevTick != prevTick) {
            this.dirty = true;
        }
        this.prevTick = prevTick;
    }

    public void setBoyEffect(boolean boyEffect) {
        this.boyEffect = boyEffect;
    }

    public boolean isBoyEffect() {
        return boyEffect;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("boyEffect", this.boyEffect);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        this.boyEffect = nbt.getBoolean("boyEffect");

        super.readNbt(nbt);
    }

    @Override
    public void sync(AnimationSyncHelper<?> animationHelper) {

        if (animationHelper instanceof SizeHelper sizeHelper) {
            setBoyEffect(sizeHelper.isBoyEffect());
        }

        super.sync(animationHelper);
    }

    public static SizeHelper from(NbtCompound nbt) {
        SizeHelper animationHelper = new SizeHelper();
        animationHelper.readNbt(nbt);
        return animationHelper;
    }
}
