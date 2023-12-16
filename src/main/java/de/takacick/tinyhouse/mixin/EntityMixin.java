package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.LivingProperties;
import de.takacick.tinyhouse.access.PlayerProperties;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@Implements({@Interface(iface = EntityProperties.class, prefix = "tinyhouse$")})
public abstract class EntityMixin {

    @Shadow
    protected abstract Vec3d getRotationVector(float pitch, float yaw);

    @Shadow
    public abstract void setBodyYaw(float bodyYaw);

    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract double getBodyY(double heightScale);

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract float getPitch();

    @Shadow
    public abstract DataTracker getDataTracker();

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract int getId();

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public abstract void tick();

    @Shadow
    private Vec3d pos;
    @Shadow
    private float standingEyeHeight;

    @Shadow
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow
    @Final
    protected Random random;
    private static final TrackedData<Integer> tinyhouse$BLOCK_MAGNET_HOLDER = BionicDataTracker.registerData(new Identifier(TinyHouse.MOD_ID, "block_magnet_holder"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> tinyhouse$CRUSHED = BionicDataTracker.registerData(new Identifier(TinyHouse.MOD_ID, "crushed"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> tinyhouse$STUCK_INSIDE_PISTON = BionicDataTracker.registerData(new Identifier(TinyHouse.MOD_ID, "stuck_inside_piston"), TrackedDataHandlerRegistry.BOOLEAN);
    private int tinyhouse$crushedTicks = 0;
    private int tinyhouse$stuckTicks = 0;
    private float tinyhouse$prevHeight = 1f;
    private float tinyhouse$height = 1f;
    private float tinyhouse$targetHeight = 1f;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(tinyhouse$BLOCK_MAGNET_HOLDER, -1);
        getDataTracker().startTracking(tinyhouse$CRUSHED, false);
        getDataTracker().startTracking(tinyhouse$STUCK_INSIDE_PISTON, false);
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    public void baseTick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (tinyhouse$getBlockMagnetOwner() > 0) {
                Entity entity = getWorld().getEntityById(tinyhouse$getBlockMagnetOwner());

                if (!(entity instanceof PlayerProperties playerProperties && playerProperties.getBlockMagnetHolding() == getId())) {
                    tinyhouse$setBlockMagnetOwner(null);
                }
            }

            if (this.tinyhouse$crushedTicks > 0) {
                tinyhouse$setCrushed(this.tinyhouse$crushedTicks - 1);
            } else {
                tinyhouse$setStuckInsidePiston(0);
            }

            if (this.tinyhouse$stuckTicks > 0) {
                tinyhouse$setStuckInsidePiston(this.tinyhouse$stuckTicks - 1);
            } else {
                tinyhouse$setStuckInsidePiston(0);
            }
        }

        if (tinyhouse$isCrushed()) {
            this.tinyhouse$prevHeight = 0.003f;
            this.tinyhouse$height = 0.003f;
            this.tinyhouse$targetHeight = 1.15f;
        } else if (tinyhouse$getCrushedHeight(0f) != this.tinyhouse$targetHeight) {

            if (this.tinyhouse$height == 0.001f) {
                this.playSound(SoundEvents.ENTITY_SLIME_SQUISH_SMALL, 1f, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            }

            this.tinyhouse$prevHeight = this.tinyhouse$height;
            if (this.tinyhouse$targetHeight == 1f) {
                this.tinyhouse$height = MathHelper.clamp(this.tinyhouse$height - 0.1f, this.tinyhouse$targetHeight, this.tinyhouse$height);
            } else {
                this.tinyhouse$height += 0.35f;
            }

            if (this.tinyhouse$height >= this.tinyhouse$targetHeight && this.tinyhouse$targetHeight != 1f) {
                this.tinyhouse$targetHeight = 1f;
            }
        }
    }

    @Inject(method = "isSprinting", at = @At("HEAD"), cancellable = true)
    public void isSprinting(CallbackInfoReturnable<Boolean> info) {
        if (this instanceof LivingProperties livingProperties && livingProperties.isBurning()) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", at = @At(value = "HEAD"), cancellable = true)
    private void updatePassengerPosition(Entity passenger, Entity.PositionUpdater positionUpdater, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties && playerProperties.getBlockMagnetHolding() >= 0) {
            if (playerProperties.getBlockMagnetHolding() == passenger.getId()) {
                Vec3d rotation = getRotationVector(getPitch(), getYaw()).multiply(2.5);

                this.setBodyYaw(getYaw());

                double d = getBodyY(0.5);
                positionUpdater.accept(passenger, this.getX() + rotation.getX(), d + rotation.getY(), this.getZ() + rotation.getZ());

                info.cancel();
            }
        }
    }

    @Inject(method = "tickRiding", at = @At(value = "HEAD"), cancellable = true)
    private void tickRiding(CallbackInfo info) {
        if (this instanceof EntityProperties entityProperties && entityProperties.getBlockMagnetOwner() >= 0) {
            Entity entity = getWorld().getEntityById(entityProperties.getBlockMagnetOwner());
            if (entity == null) {
                entityProperties.setBlockMagnetOwner(null);
            } else {
                if (!(entity instanceof PlayerProperties playerProperties && playerProperties.getBlockMagnetHolding() == getId())) {
                    entityProperties.setBlockMagnetOwner(null);
                    return;
                }
                this.setVelocity(Vec3d.ZERO);
                this.tick();
                entity.updatePassengerPosition((Entity) (Object) this);
                info.cancel();
            }
        }
    }

    @Inject(method = "calculateBoundingBox", at = @At("RETURN"), cancellable = true)
    public void calculateBoundingBox(CallbackInfoReturnable<Box> info) {
        float crushedHeight = tinyhouse$getCrushedHeight(0.5f);
        if (crushedHeight != 1f) {
            Box box = info.getReturnValue();
            double height = box.maxY - box.minY;

            info.setReturnValue(new Box(box.minX, box.minY, box.minZ, box.maxX, box.minY + height * crushedHeight, box.maxZ));
        }
    }

    @Inject(method = "calculateBoundsForPose", at = @At("RETURN"), cancellable = true)
    public void calculateBoundsForPose(CallbackInfoReturnable<Box> info) {
        float crushedHeight = tinyhouse$getCrushedHeight(0.5f);
        if (crushedHeight != 1f) {
            Box box = info.getReturnValue();
            double height = box.maxY - box.minY;

            info.setReturnValue(new Box(box.minX, box.minY, box.minZ, box.maxX, box.minY + height * crushedHeight, box.maxZ));
        }
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    public void getHeight(CallbackInfoReturnable<Float> info) {
        float crushedHeight = tinyhouse$getCrushedHeight(0.5f);
        if (crushedHeight != 1f) {
            info.setReturnValue(info.getReturnValue() * crushedHeight);
        }
    }

    @Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
    public void getStandingEyeHeight(CallbackInfoReturnable<Float> info) {
        float crushedHeight = Math.max(tinyhouse$getCrushedHeight(0.5f), 0.1f);
        if (crushedHeight != 1f) {
            info.setReturnValue(info.getReturnValue() * crushedHeight);
        }
    }

    @Inject(method = "getEyeY", at = @At("HEAD"), cancellable = true)
    public void getEyeY(CallbackInfoReturnable<Double> info) {
        float crushedHeight = Math.max(tinyhouse$getCrushedHeight(0.5f), 0.1f);
        if (crushedHeight != 1f) {
            info.setReturnValue(this.pos.getY() + this.standingEyeHeight * crushedHeight);
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (this.tinyhouse$crushedTicks > 0) {
            nbt.putInt("tinyhouse$crushedTicks", this.tinyhouse$crushedTicks);
        }

        if (this.tinyhouse$stuckTicks > 0) {
            nbt.putInt("tinyhouse$stuckTicks", this.tinyhouse$stuckTicks);
        }
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    public void readNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("tinyhouse$crushedTicks", NbtElement.INT_TYPE)) {
            tinyhouse$setCrushed(nbt.getInt("tinyhouse$crushedTicks"));
        }

        if (nbt.contains("tinyhouse$stuckTicks", NbtElement.INT_TYPE)) {
            tinyhouse$setStuckInsidePiston(nbt.getInt("tinyhouse$stuckTicks"));
        }
    }

    public void tinyhouse$setBlockMagnetOwner(Entity blockMagnetHolding) {
        getDataTracker().set(tinyhouse$BLOCK_MAGNET_HOLDER, blockMagnetHolding != null ? blockMagnetHolding.getId() : -1);
    }

    public int tinyhouse$getBlockMagnetOwner() {
        return getDataTracker().get(tinyhouse$BLOCK_MAGNET_HOLDER);
    }

    public void tinyhouse$setCrushed(int crushedTicks) {
        this.tinyhouse$crushedTicks = crushedTicks;
        getDataTracker().set(tinyhouse$CRUSHED, crushedTicks > 0);
    }

    public boolean tinyhouse$isCrushed() {
        return getDataTracker().get(tinyhouse$CRUSHED);
    }

    public float tinyhouse$getCrushedHeight(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.tinyhouse$prevHeight, this.tinyhouse$height);
    }

    public void tinyhouse$setStuckInsidePiston(int stuckInsidePistonTicks) {
        this.tinyhouse$stuckTicks = stuckInsidePistonTicks;
        getDataTracker().set(tinyhouse$STUCK_INSIDE_PISTON, stuckInsidePistonTicks > 0);
    }

    public boolean tinyhouse$isStuckInsidePiston() {
        return getDataTracker().get(tinyhouse$STUCK_INSIDE_PISTON);
    }

}