package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.UpgradeBodyClient;
import de.takacick.upgradebody.access.PlayerProperties;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private Vec3d pos;

    @Shadow
    private EntityDimensions dimensions;

    @Shadow
    public abstract float getStandingEyeHeight();

    @Shadow
    public boolean velocityDirty;

    @Shadow
    public boolean velocityModified;

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract Box getBoundingBox();

    @Shadow
    public int age;

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract void setPosition(double x, double y, double z);

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Inject(method = "getStandingEyeHeight", at = @At("HEAD"), cancellable = true)
    public void getStandingEyeHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()) {
            info.setReturnValue(Math.max(this.dimensions.height - 0.27f, 0.1f));
        }
    }

    @Inject(method = "getEyeY", at = @At("HEAD"), cancellable = true)
    public void getEyeY(CallbackInfoReturnable<Double> info) {
        if (this instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()) {
            info.setReturnValue(this.pos.getY() + getStandingEyeHeight());
        }
    }

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"), cancellable = true)
    public void adjustMovementForCollisions(Vec3d movement, CallbackInfoReturnable<Vec3d> info) {
        if (this instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()
                && playerProperties.getBodyPartManager().isHeadOnly()
                && playerProperties.isUsingHeadbutt()
                && (Object) this instanceof PlayerEntity playerEntity) {
            Vec3d vec3d = info.getReturnValue();

            if (!vec3d.equals(movement)) {

                boolean bl = !MathHelper.approximatelyEquals(movement.x, vec3d.x);
                boolean bl2 = !MathHelper.approximatelyEquals(movement.z, vec3d.z);

                if (vec3d.y <= movement.y && (bl || bl2)) {
                    Box box = this.getBoundingBox();
                    Iterable<Pair<BlockPos, VoxelShape>> posIterable = () -> new BlockCollisionSpliterator<>(getWorld(), playerEntity, box.stretch(movement), false, (pos, voxelShape) -> new Pair<>(new BlockPos(pos), voxelShape));
                    BlockPos closest = null;
                    Vec3d closestPos = null;
                    double distance = 10000;

                    for (Pair<BlockPos, VoxelShape> pair : posIterable) {
                        Optional<Vec3d> pos = pair.getRight().getClosestPointTo(getPos().add(0, 0.5, 0));
                        if (pos.isPresent()) {
                            if (pos.get().distanceTo(getPos()) < distance) {
                                closestPos = pos.get();
                                closest = pair.getLeft();
                            }
                        }
                    }

                    if (closest != null) {
                        info.setReturnValue(movement);
                        this.setPosition(this.getX() - movement.x, this.getY() - movement.y, this.getZ() - movement.z);

                        Vec3d velocity = getPos().add(0, 0.5, 0).subtract(closestPos).normalize().multiply(0.25, 0.1, 0.25).add(0, 0.2, 0);

                        setVelocity(velocity);
                        this.velocityDirty = true;
                        this.velocityModified = true;
                        playerEntity.playSound(SoundEvents.ENTITY_SLIME_SQUISH_SMALL, 1.0f, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.2f + 1.0f) / 0.8f);
                        playerProperties.setHeadbutt(false);
                        BlockState blockState = getWorld().getBlockState(closest);

                        if (blockState.getHardness(getWorld(), closest) >= 0) {
                            if (getWorld().isClient) {
                                UpgradeBodyClient.sendHeadBlockBreaking(playerEntity, closest);
                                playerEntity.playSound(SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 1.0f, 1f);
                                playerEntity.playSound(blockState.getSoundGroup().getBreakSound(), 1.0f, 1f);
                                BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState);

                                for (int i = 0; i < 20; ++i) {
                                    double g = 0.3 * playerEntity.getRandom().nextGaussian();
                                    double h = 0.3 * playerEntity.getRandom().nextDouble();
                                    double j = 0.3 * playerEntity.getRandom().nextGaussian();
                                    getWorld().addParticle(particleEffect,
                                            i % 2 == 0,
                                            closestPos.getX() + g, closestPos.getY() + h, closestPos.getZ() + j,
                                            velocity.getX() + g * 0.2, velocity.getY() + h * 0.4, velocity.getX() + j * 0.4);
                                }
                            }
                        }
                    }
                } else {
                    playerProperties.setHeadbutt(false);
                }
            }
        }
    }
}

