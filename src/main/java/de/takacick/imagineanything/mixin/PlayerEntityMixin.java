package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "imagineanything$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract PlayerAbilities getAbilities();

    @Shadow
    public abstract void sendAbilitiesUpdate();

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void startFallFlying();

    @Shadow
    public abstract void stopFallFlying();

    @Shadow
    public abstract void sendMessage(Text message, boolean actionBar);

    @Shadow
    private int sleepTimer;
    private static final TrackedData<Boolean> imagineanything$HEAD = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> imagineanything$TELEKINESIS_FLIGHT = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> imagineanything$IRON_MAN_FORCEFIELD = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> imagineanything$IRON_MAN_LASER = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> imagineanything$HOLDING = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> imagineanything$DISTANCE = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
    private final AnimationState imagineanything$headRemovalAnimationState = new AnimationState();
    private boolean imagineanything$fallFlying;
    private boolean imagineanything$ironMan;
    private int imagineanything$ironManLaserTicks = 0;
    private int imagineanything$ironManAbilityTicks = 0;
    private String imagineanything$ironManAbility = "";

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(imagineanything$HEAD, false);
        getDataTracker().startTracking(imagineanything$TELEKINESIS_FLIGHT, false);
        getDataTracker().startTracking(imagineanything$IRON_MAN_FORCEFIELD, false);
        getDataTracker().startTracking(imagineanything$IRON_MAN_LASER, false);
        getDataTracker().startTracking(imagineanything$HOLDING, -1);
        getDataTracker().startTracking(imagineanything$DISTANCE, 0f);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (world.isClient) {
            if (imagineanything$headRemovalAnimationState.getTimeRunning() >= 1.57f * 1000L) {
                imagineanything$headRemovalAnimationState.stop();
            }
        } else {
            if (imagineanything$hasTelekinesisFlight()) {

                if (!getAbilities().allowFlying) {
                    getAbilities().flying = false;
                    getAbilities().allowFlying = true;
                    sendAbilitiesUpdate();
                }

                if (getAbilities().flying) {
                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, ImagineAnything.IDENTIFIER, 5);
                }
            }

            if (imagineanything$ironManAbilityTicks > 0) {
                imagineanything$ironManAbilityTicks--;
                if (imagineanything$ironManAbilityTicks <= 0) {
                    sendMessage(Text.of(""), true);
                } else {
                    sendMessage(Text.of(imagineanything$ironManAbility), true);
                }
            }
        }

        if (getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
            if (!world.isClient) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 0, false, false, true));
            }

            if (!getAbilities().allowFlying) {
                getAbilities().allowFlying = true;
                sendAbilitiesUpdate();
            }
            imagineanything$ironMan = true;

            if ((getAbilities().flying || (isFallFlying() || imagineanything$getFallFlying())) && isSprinting()) {

                if (!isFallFlying()) {
                    startFallFlying();
                }
                setVelocity(getRotationVector().multiply(1.1));
                velocityModified = true;
                velocityDirty = true;

                if (getAbilities().flying) {
                    getAbilities().flying = false;
                    imagineanything$setFallFlying(true);
                }

                for (int index = 1; index < 3; index++) {
                    Vec3d pos = getPos().add(getRotationVec(1f).multiply(-0.4));

                    world.addParticle(ParticleTypes.FLAME, pos.getX(),
                            pos.getY(),
                            pos.getZ(), 0, 0, 0);
                }

            } else if (isFallFlying() || imagineanything$getFallFlying()) {
                if (!isOnGround()) {
                    getAbilities().flying = true;
                    stopFallFlying();
                }
                imagineanything$setFallFlying(false);
            }
        } else if (imagineanything$ironMan) {
            imagineanything$setFallFlying(false);
            if (!world.isClient) {
                removeStatusEffect(StatusEffects.RESISTANCE);
            }
            imagineanything$ironMan = false;
            imagineanything$setIronManForcefield(false);
        }

        if (!world.isClient) {
            if (imagineanything$ironManLaserTicks > 0 && getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
                imagineanything$ironManLaserTicks--;
                imagineanything$setIronManLaser(true);
                if (imagineanything$ironManLaserTicks <= 0) {
                    imagineanything$setIronManLaser(false);
                } else {
                    if (imagineanything$hasIronManLaser()) {
                        World world = getWorld();

                        HitResult hitResult = customRaycast((PlayerEntity) (Object) this, 150, 0, false);
                        HitResult entityRaycast = customEntityRaycast((PlayerEntity) (Object) this, 150, 0, false);

                        if (hitResult instanceof BlockHitResult blockHitResult) {
                            for (int x = -1; x < 2; x++) {
                                for (int y = -1; y < 2; y++) {
                                    for (int z = -1; z < 2; z++) {
                                        world.breakBlock(blockHitResult.getBlockPos().add(x, y, z), true);
                                    }
                                }
                            }
                        }

                        if (entityRaycast instanceof EntityHitResult entityHitResult && entityRaycast.getPos().distanceTo(getPos()) < hitResult.getPos().distanceTo(getPos())) {
                            entityHitResult.getEntity().damage(DamageSource.player((PlayerEntity) (Object) this), 3);
                        }
                    }
                }
            } else if (imagineanything$hasIronManLaser()) {
                imagineanything$setIronManLaser(false);
                imagineanything$ironManLaserTicks = 0;
            }
        }

        if (getSleepingPosition().isPresent()) {
            if (getWorld().getBlockState(getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED)) {
                this.sleepTimer = 0;
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("removedHead", getDataTracker().get(imagineanything$HEAD));
        nbt.putBoolean("telekinesis_flight", getDataTracker().get(imagineanything$TELEKINESIS_FLIGHT));
        nbt.putBoolean("ironManEquipped", imagineanything$ironMan);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(imagineanything$HEAD, nbt.getBoolean("removedHead"));
        getDataTracker().set(imagineanything$TELEKINESIS_FLIGHT, nbt.getBoolean("telekinesis_flight"));
        imagineanything$ironMan = nbt.getBoolean("ironManEquipped");
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (imagineanything$hasIronManForcefield()) {
            info.setReturnValue(false);
        }
    }


    @Inject(method = "onDeath", at = @At("TAIL"))
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (getSleepingPosition().isPresent()) {
            BlockPos blockPos = getSleepingPosition().get();
            if (getWorld().getBlockState(blockPos).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED)) {
                BlockState blockState;

                if ((blockState = this.world.getBlockState(blockPos)).getBlock() instanceof BedBlock) {
                    this.world.setBlockState(blockPos, blockState.with(BedBlock.OCCUPIED, true), Block.NOTIFY_ALL);
                }
            }
        }
    }


    @Inject(method = "wakeUp(ZZ)V", at = @At("HEAD"), cancellable = true)
    public void wakeUp(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo info) {
        if (getSleepingPosition().isPresent()) {
            if (getWorld().getBlockState(getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED) && !isDead()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "wakeUp()V", at = @At("HEAD"), cancellable = true)
    public void wakeUp(CallbackInfo info) {
        if (getSleepingPosition().isPresent()) {
            if (getWorld().getBlockState(getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED) && !isDead()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "checkFallFlying", at = @At("HEAD"), cancellable = true)
    public void checkFallFlying(CallbackInfoReturnable<Boolean> info) {
        if (imagineanything$fallFlying && getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
            this.startFallFlying();
            info.setReturnValue(true);
        }
    }

    private HitResult customRaycast(PlayerEntity playerEntity, double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d vec3d = playerEntity.isFallFlying() || playerEntity.isSwimming() ? playerEntity.getCameraPosVec(tickDelta).add(0, -0.4, 0) : playerEntity.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = playerEntity.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return playerEntity.world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, playerEntity));
    }

    private HitResult customEntityRaycast(PlayerEntity playerEntity, double maxDistance, float tickDelta, boolean includeFluids) {
        Vec3d vec3d = playerEntity.isFallFlying() || playerEntity.isSwimming() ? playerEntity.getCameraPosVec(tickDelta).add(0, -0.4, 0) : playerEntity.getCameraPosVec(tickDelta);
        Vec3d vec3d2 = playerEntity.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * maxDistance, vec3d2.y * maxDistance, vec3d2.z * maxDistance);
        return this.getEntityCollision(playerEntity, vec3d, vec3d3);
    }

    protected EntityHitResult getEntityCollision(PlayerEntity playerEntity, Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(playerEntity.world, playerEntity, currentPosition, nextPosition, new Box(currentPosition, nextPosition).expand(1), entity -> entity instanceof MobEntity);
    }

    public AnimationState imagineanything$getHeadRemovalState() {
        return imagineanything$headRemovalAnimationState;
    }

    public void imagineanything$setRemovedHead(boolean removedHead) {
        getDataTracker().set(imagineanything$HEAD, removedHead);
    }

    public boolean imagineanything$removedHead() {
        return getDataTracker().get(imagineanything$HEAD);
    }

    public void imagineanything$setTelekinesisFlight(boolean telekinesisFlight) {
        getDataTracker().set(imagineanything$TELEKINESIS_FLIGHT, telekinesisFlight);
    }

    public boolean imagineanything$hasTelekinesisFlight() {
        return getDataTracker().get(imagineanything$TELEKINESIS_FLIGHT);
    }

    public void imagineanything$setHolding(Entity entity) {
        if (entity == null) {
            this.dataTracker.set(imagineanything$HOLDING, -1);
            this.dataTracker.set(imagineanything$DISTANCE, 0f);
        } else {
            this.dataTracker.set(imagineanything$HOLDING, entity.getId());
            this.dataTracker.set(imagineanything$DISTANCE, entity.distanceTo(this));
        }
    }

    public int imagineanything$getHolding() {
        return this.dataTracker.get(imagineanything$HOLDING);
    }

    public float imagineanything$getDistance() {
        return this.dataTracker.get(imagineanything$DISTANCE);
    }

    public void imagineanything$setFallFlying(boolean fallFlying) {
        this.imagineanything$fallFlying = fallFlying;
    }

    public boolean imagineanything$getFallFlying() {
        return this.imagineanything$fallFlying;
    }

    public void imagineanything$setIronManForcefield(boolean ironManForcefield) {
        this.dataTracker.set(imagineanything$IRON_MAN_FORCEFIELD, ironManForcefield);
    }

    public boolean imagineanything$hasIronManForcefield() {
        return this.dataTracker.get(imagineanything$IRON_MAN_FORCEFIELD);
    }

    public void imagineanything$setIronManLaser(boolean ironManLaser) {
        getDataTracker().set(imagineanything$IRON_MAN_LASER, ironManLaser);
        this.imagineanything$ironManLaserTicks = ironManLaser ? 3 : 0;
    }

    public boolean imagineanything$hasIronManLaser() {
        return getDataTracker().get(imagineanything$IRON_MAN_LASER);
    }

    public void imagineanything$setIronManAbility(String ironManAbility) {
        this.imagineanything$ironManAbility = ironManAbility;
        this.imagineanything$ironManAbilityTicks = 30;
    }
}

