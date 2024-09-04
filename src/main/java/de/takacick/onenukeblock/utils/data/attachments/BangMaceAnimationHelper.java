package de.takacick.onenukeblock.utils.data.attachments;

import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.item.BangMace;
import de.takacick.onenukeblock.utils.explosion.BangMaceBoostExplosion;
import de.takacick.utils.common.event.EventHandler;
import de.takacick.utils.data.attachment.AnimationSyncHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BangMaceAnimationHelper extends AnimationSyncHelper<LivingEntity> {

    private final int maxFuse = 20;
    private int fuse = 0;
    private int prevFuse = 0;

    public BangMaceAnimationHelper() {
        super(10);
    }

    @Override
    public void tick(LivingEntity livingEntity) {
        this.prevTick = this.tick;
        this.prevFuse = this.fuse;

        ItemStack activeStack = livingEntity.getActiveItem();

        if (livingEntity.isUsingItem() && activeStack.getItem() instanceof BangMace) {
            this.running = true;
        } else {
            this.running = false;
        }

        if (this.running) {
            this.tick = Math.min(this.tick + 1, this.maxTicks);
            if (this.tick >= this.maxTicks) {

                World world = livingEntity.getWorld();

                if (!world.isClient) {
                    if (getFuse(1f) <= 0) {
                        EventHandler.sendEntityStatus(world, livingEntity, OneNukeBlock.IDENTIFIER, 2, 0);

                        BangMaceBoostExplosion.createExplosion((ServerWorld) world, livingEntity, null, null,
                                livingEntity.getX(), livingEntity.getBodyY(0.1), livingEntity.getZ(),
                                6f, false, true,
                                ParticleTypes.EXPLOSION_EMITTER, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);

                        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                            serverPlayerEntity.currentExplosionImpactPos = serverPlayerEntity.getPos();
                            serverPlayerEntity.setIgnoreFallDamageFromCurrentExplosion(true);
                        }

                        livingEntity.stopUsingItem();
                        if (livingEntity instanceof PlayerEntity player) {
                            player.getItemCooldownManager().set(activeStack.getItem(), 60);
                        }
                    }
                }

                Vec3d rot = livingEntity.getRotationVector();
                Vec3d pos = livingEntity.getPos()
                        .add(0, livingEntity.getHeight() * 0.25, 0)
                        .add(rot.multiply(0.375 * 1.66666666667).multiply(livingEntity.getWidth()));

                if (this.fuse <= 0) {
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1f, 1f, true);
                }

                Vec3d offset = new Vec3d(world.getRandom().nextGaussian(), world.getRandom().nextGaussian(), world.getRandom().nextGaussian()).multiply(0.5);
                world.addParticle(ParticleTypes.SMOKE,
                        pos.getX() + offset.getX() * livingEntity.getScale() * 0.5,
                        pos.getY() + offset.getY() * livingEntity.getScale() * 0.5,
                        pos.getZ() + offset.getZ() * livingEntity.getScale() * 0.5,
                        offset.getX() * 0.01,
                        Math.abs(offset.getY()) * 0.1,
                        offset.getZ() * 0.01);

                this.fuse += 1;
            }

            setDirty(true);
        }

        if (livingEntity.getWorld().isClient && !this.running) {
            this.tick = 0;
            this.prevTick = 0;
            this.fuse = 0;
            this.prevFuse = 0;
            this.running = false;
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("fuse", this.fuse);
        nbt.putInt("prevFuse", this.prevFuse);
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.fuse = nbt.getInt("fuse");
        this.prevFuse = nbt.getInt("prevFuse");
        super.readNbt(nbt);
    }

    public int getMaxFuse() {
        return this.maxFuse;
    }

    public float getFuse(float tickDelta) {
        return this.maxFuse - MathHelper.lerp(tickDelta, this.prevFuse, this.fuse);
    }

    public boolean isFused() {
        return this.tick >= this.maxTicks;
    }

    @Override
    public void sync(AnimationSyncHelper<?> animationHelper) {
        if (animationHelper instanceof BangMaceAnimationHelper helper) {
            if (Math.abs(helper.fuse - this.fuse) > 2) {
                this.fuse = helper.fuse;
                this.prevFuse = helper.prevFuse;
            }
        }
        if (Math.abs(animationHelper.getTick() - this.tick) > 2) {
            this.tick = animationHelper.getTick();
            this.prevTick = animationHelper.getPrevTick();
        }

        super.sync(animationHelper);
    }

    public static BangMaceAnimationHelper from(NbtCompound nbt) {
        BangMaceAnimationHelper animationHelper = new BangMaceAnimationHelper();
        animationHelper.readNbt(nbt);
        return animationHelper;
    }
}
