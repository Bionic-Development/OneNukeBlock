package de.takacick.raidbase.mixin;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.PlayerProperties;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "raidbase$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    private static final TrackedData<Boolean> raidbase$SLIME_SUIT = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "slime_suit"), TrackedDataHandlerRegistry.BOOLEAN);
    public float raidbase$targetStretch;
    public float raidbase$stretch;
    public float raidbase$lastStretch;
    private boolean raidbase$onGroundLastTick;

    protected PlayerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(raidbase$SLIME_SUIT, false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (raidbase$hasSlimeSuit()) {
            if (!getWorld().isClient) {
                getWorld().getOtherEntities(this, getBoundingBox()).forEach(entity -> {
                    if (entity instanceof ProjectileEntity projectileEntity) {
                        projectileEntity.age = projectileEntity.age - 1;
                        if (projectileEntity.getVelocity().multiply(1, 0, 1).length() <= 0.1 && projectileEntity.getVelocity().getY() <= 0) {

                            Vec3d vec3d = projectileEntity.getPos().subtract(getPos().add(0, getHeight() / 2, 0))
                                    .normalize();

                            projectileEntity.setVelocity((vec3d.getY() < 0 ? vec3d.multiply(1, -1, 1) : vec3d).add(0, 0.6, 0));
                        } else {
                            projectileEntity.setVelocity(getVelocity().multiply(-0.7));
                        }
                        projectileEntity.setOwner(this);

                        projectileEntity.velocityModified = true;
                    }
                });
            }

            this.raidbase$stretch += (this.raidbase$targetStretch - this.raidbase$stretch) * 0.5f;
            this.raidbase$lastStretch = this.raidbase$stretch;
            if (this.isOnGround() && !this.raidbase$onGroundLastTick) {
                if (getWorld().isClient) {
                    for (int j = 0; j < 15; ++j) {
                        float f = this.random.nextFloat() * ((float) Math.PI * 2);
                        float g = this.random.nextFloat() * 0.5f + 0.5f;
                        float h = MathHelper.sin(f) * 0.9f * g;
                        float k = MathHelper.cos(f) * 0.9f * g;
                        this.getWorld().addParticle(ParticleTypes.ITEM_SLIME, this.getX() + (double) h, this.getY(), this.getZ() + (double) k, 0.0, 0.0, 0.0);
                    }
                }
                this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
                this.raidbase$targetStretch = -0.5f;
            } else if (!this.isOnGround() && this.raidbase$onGroundLastTick) {
                this.raidbase$targetStretch = 1.0f;
            }

            this.raidbase$onGroundLastTick = this.isOnGround();
            this.raidbase$targetStretch *= 0.6f;
        }
    }

    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    public void getMovementSpeed(CallbackInfoReturnable<Float> info) {
        if (raidbase$hasSlimeSuit()) {
            info.setReturnValue(0f);
        }
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        if (raidbase$hasSlimeSuit()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (raidbase$hasSlimeSuit()) {
            nbt.putBoolean("raidbase$slimeSuit", raidbase$hasSlimeSuit());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("raidbase$slimeSuit", NbtCompound.BYTE_TYPE)) {
            raidbase$setSlimeSuit(nbt.getBoolean("raidbase$slimeSuit"));
        }
    }

    public void raidbase$setSlimeSuit(boolean drillCubeMech) {
        getDataTracker().set(raidbase$SLIME_SUIT, drillCubeMech);
    }

    public boolean raidbase$hasSlimeSuit() {
        return getDataTracker().get(raidbase$SLIME_SUIT);
    }

    public float raidbase$getSlimeSuitStretch() {
        return this.raidbase$stretch;
    }

    public float raidbase$getSlimeSuitLastStretch() {
        return this.raidbase$lastStretch;
    }
}
