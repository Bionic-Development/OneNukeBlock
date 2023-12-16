package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.TinyHouse;
import de.takacick.tinyhouse.access.EntityProperties;
import de.takacick.tinyhouse.access.LivingProperties;
import de.takacick.tinyhouse.registry.ItemRegistry;
import de.takacick.tinyhouse.registry.item.BlockMagnet;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "tinyhouse$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract float getYaw(float tickDelta);

    @Shadow
    public abstract void setSprinting(boolean sprinting);

    @Shadow
    @Final
    public LimbAnimator limbAnimator;

    @Shadow
    public abstract float getHeadYaw();

    @Shadow
    public abstract boolean isInsideWall();

    private static final TrackedData<Boolean> tinyhouse$BURNING = BionicDataTracker.registerData(new Identifier(TinyHouse.MOD_ID, "burning"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> tinyhouse$FROZEN = BionicDataTracker.registerData(new Identifier(TinyHouse.MOD_ID, "frozen"), TrackedDataHandlerRegistry.BOOLEAN);
    private int tinyhouse$burningTicks = 0;
    private int tinyhouse$frozenBodyTicks = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(tinyhouse$BURNING, false);
        getDataTracker().startTracking(tinyhouse$FROZEN, false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (tinyhouse$isBurning()) {
            if (isOnGround() || this.tinyhouse$burningTicks >= 10) {
                move(MovementType.SELF, getRotationVector(0f, getYaw()).multiply(0.5));
            }
            this.tinyhouse$burningTicks++;

            if (this.tinyhouse$burningTicks <= 1) {
                setOnFireFor(5);
            }

            if (getWorld().isClient) {
                if (tinyhouse$isBurning()) {
                    Vec3d rot = getRotationVector(0f, getYaw());
                    Vec3d pos = new Vec3d(getRandom().nextGaussian(), getRandom().nextDouble(), getRandom().nextGaussian());

                    getWorld().addParticle(ParticleTypes.SMOKE, true, getX() + pos.getX() * getWidth() * 0.8, getBodyY(0.5 + getRandom().nextDouble() * 0.6), getZ() + pos.getZ() * getWidth() * 0.8,
                            rot.getX() * 0.01, rot.getY() * 0.01, rot.getZ() * 0.01);
                    getWorld().addParticle(ParticleTypes.FLAME, true, getX() + pos.getX() * getWidth() * 0.8, getBodyY(0.5 + getRandom().nextDouble() * 0.6), getZ() + pos.getZ() * getWidth() * 0.8,
                            rot.getX() * 0.01, rot.getY() * 0.01, rot.getZ() * 0.01);
                }
            } else {
                tinyhouse$setBurning(getFireTicks() > 0);
            }
        } else {
            this.tinyhouse$burningTicks = 0;
        }

        if (tinyhouse$hasFrozenBody()) {
            if (isOnGround()) {
                addVelocity(getRotationVector(0f, getYaw()).multiply(-0.004));
            }

            if (!getWorld().isClient) {
                tinyhouse$setFrozenBody(this.getFrozenTicks() <= 0 ? 0 : this.tinyhouse$frozenBodyTicks - 1);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tickTail(CallbackInfo info) {
        if (tinyhouse$hasFrozenBody()) {
            this.inPowderSnow = true;
        }
    }

    @Inject(method = "getMovementSpeed()F", at = @At("HEAD"), cancellable = true)
    public void getMovementSpeed(CallbackInfoReturnable<Float> info) {
        if (this instanceof EntityProperties entityProperties
                && entityProperties.isStuckInsidePiston()) {
            info.setReturnValue(0f);
        }
    }

    @Inject(method = "getJumpVelocity()F", at = @At("HEAD"), cancellable = true)
    public void getJumpVelocity(CallbackInfoReturnable<Float> info) {
        if (this instanceof EntityProperties entityProperties
                && entityProperties.isStuckInsidePiston()) {
            info.setReturnValue(0f);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (tinyhouse$isBurning()) {
            nbt.putBoolean("tinyhouse$burning", true);
        }

        if (this.tinyhouse$frozenBodyTicks > 0) {
            nbt.putInt("tinyhouse$frozenBodyTicks", this.tinyhouse$frozenBodyTicks);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("tinyhouse$burning", NbtElement.BYTE_TYPE)) {
            tinyhouse$setBurning(nbt.getBoolean("tinyhouse$burning"));
        }

        if (nbt.contains("tinyhouse$frozenBodyTicks", NbtElement.INT_TYPE)) {
            this.tinyhouse$frozenBodyTicks = nbt.getInt("tinyhouse$frozenBodyTicks");
            tinyhouse$setFrozenBody(this.tinyhouse$frozenBodyTicks);
        }
    }

    @Inject(method = "getStepHeight", at = @At("RETURN"), cancellable = true)
    public void getStepHeight(CallbackInfoReturnable<Float> info) {
        if (tinyhouse$isBurning() || tinyhouse$hasFrozenBody()) {
            info.setReturnValue(Math.max(info.getReturnValue(), 1f));
        }
    }

    @Inject(method = "updateLimbs(F)V", at = @At("HEAD"), cancellable = true)
    public void updateLimbs(float posDelta, CallbackInfo info) {
        if (tinyhouse$isBurning()) {
            float f = Math.min(posDelta * 10.0f, 1.0f);
            this.limbAnimator.updateLimbs(f, 1.0f);
            info.cancel();
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        if (!getWorld().isClient) {
            if (getType().equals(EntityType.CHICKEN)) {
                if (damageSource.isOf(BlockMagnet.BLOCK_MAGNET)) {
                    dropItem(ItemRegistry.CHICKEN_ITEM);
                    this.discard();
                    info.cancel();
                }
            }
        }
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    public float travel(Block instance) {
        if (tinyhouse$hasFrozenBody()) {
            return 1.1f;
        }
        return instance.getSlipperiness();
    }

    public void tinyhouse$setBurning(boolean burning) {

        if (!burning && tinyhouse$isBurning()) {
            if (!getWorld().isClient) {
                BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, TinyHouse.IDENTIFIER, 3);
            }
        }

        getDataTracker().set(tinyhouse$BURNING, burning);
    }

    public boolean tinyhouse$isBurning() {
        return getDataTracker().get(tinyhouse$BURNING);
    }

    public void tinyhouse$setFrozenBody(int frozenBodyTicks) {
        this.tinyhouse$frozenBodyTicks = frozenBodyTicks;

        if (frozenBodyTicks <= 0 && tinyhouse$hasFrozenBody()) {
            if (!getWorld().isClient) {
                BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, TinyHouse.IDENTIFIER, 4);
            }
        }

        getDataTracker().set(tinyhouse$FROZEN, frozenBodyTicks > 0);
    }

    public boolean tinyhouse$hasFrozenBody() {
        return getDataTracker().get(tinyhouse$FROZEN);
    }
}