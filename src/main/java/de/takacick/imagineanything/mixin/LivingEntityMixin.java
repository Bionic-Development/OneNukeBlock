package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.access.LivingProperties;
import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.network.TelekinesisExplosionHandler;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.item.HeadItem;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "imagineanything$")})
public abstract class LivingEntityMixin extends Entity {

    private static final TrackedData<Integer> imagineanything$HOLDER = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> imagineanything$VIBRATING = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    private boolean imagineanything$thrown;

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract ItemStack getOffHandStack();

    @Shadow
    public abstract Optional<BlockPos> getSleepingPosition();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(imagineanything$HOLDER, -1);
        getDataTracker().startTracking(imagineanything$VIBRATING, -1);
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At(value = "HEAD"), cancellable = true)
    private void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo info) {
        ItemStack stack = getMainHandStack().getItem() instanceof HeadItem ? getMainHandStack() : getOffHandStack();
        if (stack.getItem() instanceof HeadItem) {
            info.cancel();
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo info) {
        if (!world.isClient) {
            if (imagineanything$hasHolder()) {
                if (!(world.getEntityById(getDataTracker().get(imagineanything$HOLDER)) instanceof PlayerProperties playerProperties
                        && playerProperties.getHolding() == getId())) {
                    imagineanything$setHolder(null);
                }
            }

            if (isInsideWaterOrBubbleColumn()) {
                imagineanything$thrown = false;
            }

            if ((horizontalCollision || verticalCollision) && imagineanything$thrown) {
                imagineanything$thrown = false;
                TelekinesisExplosionHandler.createExplosion((ServerWorld) world, null, null, null, this.getX(), this.getBodyY(0.0625), this.getZ(), 2.3f, false, Explosion.DestructionType.BREAK);
            }

            if (imagineanything$getVibratingTicks() > 0) {
                imagineanything$setVibratingTicks(imagineanything$getVibratingTicks() - 1);

                if (imagineanything$getVibratingTicks() <= 0) {
                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, ImagineAnything.IDENTIFIER, 17);
                    Vec3d velocity = this.getRotationVector().multiply(-0.1, 0, -0.1);
                    ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.34), getZ(), ItemRegistry.POOP_ITEM.getDefaultStack(), velocity.getX(), velocity.getY(), velocity.getZ());
                    itemEntity.setToDefaultPickupDelay();
                    world.spawnEntity(itemEntity);
                    this.discard();
                }
            }
        }
    }

    @Inject(method = "handleFallDamage", at = @At(value = "HEAD"))
    private void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        if (!world.isClient) {
            if (imagineanything$thrown) {
                imagineanything$thrown = false;
                TelekinesisExplosionHandler.createExplosion((ServerWorld) world, null, null, null, this.getX(), this.getBodyY(0.0625), this.getZ(), 2.3f, false, Explosion.DestructionType.BREAK);
            }
        }
    }

    @Inject(method = "wakeUp()V", at = @At("HEAD"), cancellable = true)
    public void wakeUp(CallbackInfo info) {
        if (getSleepingPosition().isPresent()) {
            if (getWorld().getBlockState(getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED)) {
                info.cancel();
            }
        }
    }

    @Inject(method = "isFallFlying", at = @At("RETURN"), cancellable = true)
    public void isFallFlying(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue() && this instanceof PlayerProperties playerProperties) {
            info.setReturnValue(playerProperties.getFallFlying());
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3d travel(Vec3d movementInput) {
        if (imagineanything$hasHolder() || imagineanything$getVibratingTicks() > 0 || (getSleepingPosition().isPresent()
                && getWorld().getBlockState(getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED))) {
            return movementInput.multiply(0, 0, 0);
        }
        return movementInput;
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At(value = "HEAD"), cancellable = true)
    private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (stack.isOf(ItemRegistry.IRON_MAN_SUIT)) {
            info.setReturnValue(EquipmentSlot.CHEST);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putInt("vibratingTicks", getDataTracker().get(imagineanything$VIBRATING));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(imagineanything$VIBRATING, nbt.getInt("vibratingTicks"));
    }

    public void imagineanything$setHolder(LivingEntity livingEntity) {
        if (livingEntity == null) {
            getDataTracker().set(imagineanything$HOLDER, -1);
        } else {
            getDataTracker().set(imagineanything$HOLDER, livingEntity.getId());
        }
    }

    public boolean imagineanything$hasHolder() {
        return getDataTracker().get(imagineanything$HOLDER) != -1;
    }

    public void imagineanything$setThrown() {
        this.imagineanything$thrown = true;
    }

    public void imagineanything$setVibratingTicks(int vibratingTicks) {
        getDataTracker().set(imagineanything$VIBRATING, vibratingTicks);
    }

    public int imagineanything$getVibratingTicks() {
        return getDataTracker().get(imagineanything$VIBRATING);
    }
}