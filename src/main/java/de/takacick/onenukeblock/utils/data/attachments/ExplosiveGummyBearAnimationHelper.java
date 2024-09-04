package de.takacick.onenukeblock.utils.data.attachments;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.utils.common.event.EventHandler;
import de.takacick.utils.data.attachment.AnimationSyncHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExplosiveGummyBearAnimationHelper extends AnimationSyncHelper<LivingEntity> {

    public ExplosiveGummyBearAnimationHelper() {
        super(49);
    }

    @Override
    public void tick(LivingEntity livingEntity) {
        this.prevTick = this.tick;
        this.tick += 1;

        World world = livingEntity.getWorld();
        if (world.isClient) {
            if (this.tick >= 20) {
                if (this.tick == 20) {
                    world.playSound(livingEntity.getX(), livingEntity.getBodyY(0.5), livingEntity.getZ(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.PLAYERS, 1f, 1f, true);
                }

                Vec3d offset = new Vec3d(world.getRandom().nextGaussian(), world.getRandom().nextDouble(), world.getRandom().nextGaussian()).multiply(0.5);
                world.addParticle(ParticleTypes.SMOKE,
                        livingEntity.getX() + offset.getX(), livingEntity.getBodyY(0.5) + offset.getY(), livingEntity.getZ() + offset.getZ(),
                        offset.getX() * 0.1, offset.getY() * 0.1, offset.getZ() * 0.1);
            }
        }

        if (this.tick >= this.maxTicks) {
            if (!world.isClient) {
                livingEntity.addVelocity(0, 0.15, 0);
                EventHandler.sendEntityStatus(world, livingEntity, OneNukeBlock.IDENTIFIER, 1, 0);

                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(serverPlayerEntity));
                }
            }
            this.tick = 0;
            this.prevTick = 0;
            this.running = false;
        } else {
            this.running = true;
        }
    }

    @Override
    public float getProgress(float tickDelta) {
        return Math.min(super.getProgress(tickDelta), 1f);
    }

    public static ExplosiveGummyBearAnimationHelper from(NbtCompound nbt) {
        ExplosiveGummyBearAnimationHelper animationHelper = new ExplosiveGummyBearAnimationHelper();
        animationHelper.readNbt(nbt);
        return animationHelper;
    }
}
