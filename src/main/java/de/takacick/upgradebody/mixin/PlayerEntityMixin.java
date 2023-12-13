package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.EffectRegistry;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import de.takacick.upgradebody.registry.bodypart.upgrades.KillerDriller;
import de.takacick.upgradebody.registry.entity.custom.ShopItemEntity;
import de.takacick.upgradebody.registry.entity.custom.UpgradeShopPortalEntity;
import de.takacick.upgradebody.server.datatracker.BodyPartTracker;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "upgradebody$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract boolean isCreative();

    private static final TrackedData<BodyPartManager> upgradebody$BODY_PARTS = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "body_parts"), BodyPartTracker.BODY_PARTS);
    private static final TrackedData<Boolean> upgradebody$HEADBUTT = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "headbutt"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> upgradebody$KILLER_DRILLING = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "killer_drilling"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> upgradebody$CYBER_SLICE = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "cyber_slice"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> upgradebody$ENERGY_BELLY_BLAST = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "energy_belly_blast"), TrackedDataHandlerRegistry.BOOLEAN);
    private UpgradeShopPortalEntity upgradebody$upgradeShopPortalEntity;
    private int upgradebody$energyBellyBlastTicks = 0;
    private int upgradebody$energyBellyBlastUsageTicks = 0;
    private int upgradebody$killerDrillingTicks = 0;
    public float upgradebody$targetStretch;
    public float upgradebody$stretch;
    public float upgradebody$lastStretch;
    private boolean upgradebody$onGroundLastTick;
    private boolean upgradebody$adjustedStepHeight;
    private int upgradebody$noFallDamageTicks = 0;
    private int upgradebody$chainsawTicks = 0;
    private int upgradebody$cyberSliceTicks = 0;

    protected PlayerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(upgradebody$BODY_PARTS, new BodyPartManager(List.of(BodyParts.HEAD), false));
        getDataTracker().startTracking(upgradebody$HEADBUTT, false);
        getDataTracker().startTracking(upgradebody$KILLER_DRILLING, false);
        getDataTracker().startTracking(upgradebody$CYBER_SLICE, false);
        getDataTracker().startTracking(upgradebody$ENERGY_BELLY_BLAST, false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (upgradebody$isUpgrading()) {
            if (upgradebody$hasBodyPart(BodyParts.TANK_TRACKS)) {
                this.setStepHeight(1.2f);
                this.upgradebody$adjustedStepHeight = true;
            } else if (this.upgradebody$adjustedStepHeight) {
                this.upgradebody$adjustedStepHeight = false;
                setStepHeight(0.6f);
            }

            if (upgradebody$getBodyPartManager().isHeadOnly()) {
                this.upgradebody$stretch += (this.upgradebody$targetStretch - this.upgradebody$stretch) * 0.5f;
                this.upgradebody$lastStretch = this.upgradebody$stretch;
                if (this.isOnGround() && !this.upgradebody$onGroundLastTick) {
                    if (getWorld().isClient) {
                        for (int j = 0; j < 8; ++j) {
                            float f = this.random.nextFloat() * ((float) Math.PI * 2);
                            float g = this.random.nextFloat() * 0.5f + 0.5f;
                            float h = MathHelper.sin(f) * 0.5f * g;
                            float k = MathHelper.cos(f) * 0.5f * g;
                            this.getWorld().addParticle(ParticleRegistry.PLAYER_SLIME, this.getX() + (double) h, this.getY(), this.getZ() + (double) k, 0.0, 0.0, 0.0);
                        }
                    }
                    this.playSound(SoundEvents.ENTITY_SLIME_SQUISH_SMALL, this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f) / 0.8f);
                    this.upgradebody$targetStretch = -0.5f;
                } else if (!this.isOnGround() && this.upgradebody$onGroundLastTick) {
                    this.upgradebody$targetStretch = 1.0f;
                }

                this.upgradebody$onGroundLastTick = this.isOnGround();
                this.upgradebody$targetStretch *= 0.6f;
            }

            if (!getWorld().isClient) {
                if (this.upgradebody$killerDrillingTicks > 0) {
                    this.upgradebody$killerDrillingTicks--;
                    this.fallDistance = 0;
                    this.upgradebody$noFallDamageTicks = 10;
                    if (this.upgradebody$killerDrillingTicks <= 0) {
                        upgradebody$setKillerDrilling(false);
                    }
                }

                if (upgradebody$isKillerDrilling()
                        && !upgradebody$getBodyPartManager().hasBodyPart(BodyParts.KILLER_DRILLER)) {
                    upgradebody$setKillerDrilling(false);
                }

                if (this.upgradebody$energyBellyBlastTicks > 0) {
                    this.upgradebody$energyBellyBlastTicks--;
                    if (this.upgradebody$energyBellyBlastTicks <= 0) {
                        upgradebody$setEnergyBellyBlast(false);
                    }
                }

                if (upgradebody$isUsingEnergyBellyBlast()
                        && !upgradebody$getBodyPartManager().hasBodyPart(BodyParts.ENERGY_BELLY_CANNON)) {
                    upgradebody$setEnergyBellyBlast(false);
                }

                if (this.upgradebody$cyberSliceTicks > 0) {
                    this.upgradebody$cyberSliceTicks--;
                    if (this.upgradebody$cyberSliceTicks <= 0) {
                        upgradebody$setCyberSlice(false);
                    }
                }

                if (upgradebody$isUsingCyberSlice()
                        && !upgradebody$getBodyPartManager().hasBodyPart(BodyParts.CYBER_CHAINSAWS)) {
                    upgradebody$setCyberSlice(false);
                }
            }

            if (upgradebody$isUsingEnergyBellyBlast()) {
                this.upgradebody$energyBellyBlastUsageTicks++;
            } else {
                this.upgradebody$energyBellyBlastUsageTicks = 0;
            }

            if (this.upgradebody$chainsawTicks > 0) {
                this.upgradebody$chainsawTicks--;
                if (!this.getMainHandStack().isEmpty()) {
                    this.upgradebody$chainsawTicks = 0;
                }
            }

            if (upgradebody$isKillerDrilling()) {
                KillerDriller.killerDrilling(getWorld(), (PlayerEntity) (Object) this);
            }

            upgradebody$getBodyPartManager().getBodyParts().forEach(bodyPart -> {
                bodyPart.tick((PlayerEntity) (Object) this);
            });
        } else if (this.upgradebody$adjustedStepHeight) {
            this.upgradebody$adjustedStepHeight = false;
            setStepHeight(0.6f);
        }

        if (this.upgradebody$noFallDamageTicks > 0) {
            this.upgradebody$noFallDamageTicks--;
        }
    }

    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    public void getMovementSpeed(CallbackInfoReturnable<Float> info) {
        if (upgradebody$isUpgrading()) {
            if (!upgradebody$getBodyPartManager().canWalk()) {
                info.setReturnValue(0f);
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (upgradebody$getBodyPartManager().isUpgrading()
                || !upgradebody$getBodyPartManager().getBodyParts().isEmpty()) {
            nbt.put("upgradebody$bodyparts", BodyPartManager.toNbt(upgradebody$getBodyPartManager(), new NbtCompound()));
        }

        if (upgradebody$getBodyPartManager().isHeadOnly()) {
            nbt.putBoolean("upgradebody$onGroundLastTick", this.upgradebody$onGroundLastTick);
        }

        if (this.upgradebody$upgradeShopPortalEntity != null && !getWorld().isClient) {
            this.upgradebody$upgradeShopPortalEntity.setDead(true);
            this.upgradebody$upgradeShopPortalEntity = null;
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("upgradebody$bodyparts", NbtElement.COMPOUND_TYPE)) {
            getDataTracker().set(upgradebody$BODY_PARTS, BodyPartManager.fromNbt(nbt.getCompound("upgradebody$bodyparts")));
        }

        this.upgradebody$onGroundLastTick = nbt.getBoolean("upgradebody$onGroundLastTick");
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo info) {
        if (target instanceof ShopItemEntity && isCreative()) {
            target.discard();
            info.cancel();
        } else if (upgradebody$isUpgrading()
                && upgradebody$getBodyPartManager().hasBodyPart(BodyParts.CYBER_CHAINSAWS)
                && getMainHandStack().isEmpty()) {
            if (!getWorld().isClient) {
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
                }
            }
        }
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    public void getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info) {
        if (upgradebody$isUpgrading()) {
            if (pose.equals(EntityPose.CROUCHING) || pose.equals(EntityPose.STANDING)) {
                BodyPartManager bodyPartManager = upgradebody$getBodyPartManager();
                info.setReturnValue(bodyPartManager.calculateDimensions());
            }
        }
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        if (upgradebody$isUpgrading()) {
            if (upgradebody$getBodyPartManager().isHeadOnly() || this.upgradebody$noFallDamageTicks > 0) {
                info.setReturnValue(false);
            }
        }
    }

    public void upgradebody$setUpgradeShopPortal(UpgradeShopPortalEntity emeraldShopPortal) {
        if (this.upgradebody$upgradeShopPortalEntity != null && this.upgradebody$upgradeShopPortalEntity.isAlive()) {
            this.upgradebody$upgradeShopPortalEntity.setDead(true);
        }

        this.upgradebody$upgradeShopPortalEntity = emeraldShopPortal;
    }

    public void upgradebody$setUpgrading(boolean upgrading) {
        BodyPartManager bodyPartManager = upgradebody$getBodyPartManager();
        if (bodyPartManager.isUpgrading() != upgrading) {
            bodyPartManager.setUpgrading(upgrading);
            upgradebody$setBodyPartManager(bodyPartManager);
            UpgradeBody.updateEntityHealth(this, upgrading ? upgradebody$getBodyPartManager().getHearts() : 20d, true);

            if (upgrading) {
                upgradebody$getBodyPartManager().getBodyParts().forEach(bodyPart -> {
                    bodyPart.onEquip((PlayerEntity) (Object) this);
                });
            } else {
                upgradebody$getBodyPartManager().getBodyParts().forEach(bodyPart -> {
                    bodyPart.onDequip((PlayerEntity) (Object) this);
                });
            }
        }
    }

    public boolean upgradebody$isUpgrading() {
        return upgradebody$getBodyPartManager().isUpgrading();
    }

    public void upgradebody$setBodyPartManager(BodyPartManager bodyPartManager) {
        getDataTracker().set(upgradebody$BODY_PARTS, bodyPartManager, true);
    }

    public BodyPartManager upgradebody$getBodyPartManager() {
        return getDataTracker().get(upgradebody$BODY_PARTS);
    }

    public void upgradebody$setBodyPart(BodyPart bodyPart, boolean enable) {
        BodyPartManager bodyPartManager = upgradebody$getBodyPartManager();
        if (bodyPartManager.setBodyPart(bodyPart, enable)) {

            if (enable) {
                bodyPart.onEquip((PlayerEntity) (Object) this);
            } else {
                bodyPart.onDequip((PlayerEntity) (Object) this);
            }

            upgradebody$setBodyPartManager(bodyPartManager);
            if (upgradebody$isUpgrading()) {
                UpgradeBody.updateEntityHealth(this, upgradebody$getBodyPartManager().getHearts(), enable);
            }
        }
    }

    public boolean upgradebody$hasBodyPart(BodyPart bodyPart) {
        return upgradebody$getBodyPartManager().hasBodyPart(bodyPart);
    }

    public float upgradebody$getStretch() {
        return this.upgradebody$stretch;
    }

    public float upgradebody$getLastStretch() {
        return this.upgradebody$lastStretch;
    }

    public void upgradebody$setHeadbutt(boolean headbutt) {
        getDataTracker().set(upgradebody$HEADBUTT, headbutt, true);
    }

    public boolean upgradebody$isUsingHeadbutt() {
        return getDataTracker().get(upgradebody$HEADBUTT);
    }

    public void upgradebody$setKillerDrilling(boolean killerDrilling) {
        getDataTracker().set(upgradebody$KILLER_DRILLING, killerDrilling);
        this.upgradebody$killerDrillingTicks = killerDrilling ? 3 : 0;
    }

    public boolean upgradebody$isKillerDrilling() {
        return getDataTracker().get(upgradebody$KILLER_DRILLING);
    }

    public void upgradebody$setEnergyBellyBlast(boolean energyBellyBlast) {
        getDataTracker().set(upgradebody$ENERGY_BELLY_BLAST, energyBellyBlast);
        this.upgradebody$energyBellyBlastTicks = energyBellyBlast ? 3 : 0;
    }

    public boolean upgradebody$isUsingEnergyBellyBlast() {
        return getDataTracker().get(upgradebody$ENERGY_BELLY_BLAST);
    }

    public int upgradebody$getEnergyBellyBlastUsageTicks() {
        return this.upgradebody$energyBellyBlastUsageTicks;
    }

    public void upgradebody$setCyberSlice(boolean cyberSlice) {
        getDataTracker().set(upgradebody$CYBER_SLICE, cyberSlice);
        this.upgradebody$cyberSliceTicks = cyberSlice ? 3 : 0;
    }

    public boolean upgradebody$isUsingCyberSlice() {
        return getDataTracker().get(upgradebody$CYBER_SLICE);
    }

    public void upgradebody$setCyberChainsawTicks(int cyberChainsawTicks) {
        this.upgradebody$chainsawTicks = cyberChainsawTicks;
    }

    public boolean upgradebody$hasCyberChainsawAnimation() {
        return this.upgradebody$chainsawTicks > 0 || upgradebody$isUsingCyberSlice();
    }
}
