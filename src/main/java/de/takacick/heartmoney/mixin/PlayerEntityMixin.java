package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.access.PlayerProperties;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.entity.custom.HeartShopPortalEntity;
import de.takacick.heartmoney.registry.entity.custom.ShopItemEntity;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
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

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "heartmoney$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract SoundCategory getSoundCategory();

    @Shadow
    public abstract boolean isCreative();

    @Shadow public abstract void resetLastAttackedTicks();

    private static final TrackedData<Boolean> heartmoney$HEART = BionicDataTracker.registerData(new Identifier(HeartMoney.MOD_ID, "heart"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> heartmoney$BLOOD_RIPTIDE = BionicDataTracker.registerData(new Identifier(HeartMoney.MOD_ID, "blood_riptide"), TrackedDataHandlerRegistry.BOOLEAN);
    private HeartShopPortalEntity heartmoney$heartShopPortal;
    private int heartmoney$removeHearts = 0;
    private int heartmoney$stepRemoveHearts = 0;
    private double heartmoney$heartMultiplier = 1d;
    private int heartmoney$bloodRiptideTicks = 1;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(heartmoney$HEART, false);
        getDataTracker().startTracking(heartmoney$BLOOD_RIPTIDE, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!world.isClient) {
            if (this.heartmoney$removeHearts > 0) {
                int removed = Math.min(this.heartmoney$stepRemoveHearts, this.heartmoney$removeHearts);
                this.heartmoney$removeHearts -= removed;
                world.playSound((PlayerEntity) (Object) this, getX(), getY() + calculateBoundingBox().getYLength() / 2, getZ(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.NEUTRAL, 0.1f, 1);
                HeartMoney.updateEntityHealth(this, getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH) - removed, false);
            }

            if (this.heartmoney$bloodRiptideTicks > 0) {
                heartmoney$setBloodRiptideTicks(this.riptideTicks > 0 ? this.heartmoney$bloodRiptideTicks - 1 : 0);

                if (heartmoney$hasBloodRiptide()) {
                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, HeartMoney.IDENTIFIER, 13);
                }

                this.fallDistance = 0;

                this.world.playSound(null, new BlockPos(getX(), getY(), getZ()), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (this.heartmoney$bloodRiptideTicks % 3 == 0) {
                    this.world.playSound(null, new BlockPos(getX(), getY(), getZ()), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 0.25F, 3.0F);
                }

                this.world.getOtherEntities(this, new Box(getX() - 4, getY() - 4, getZ() - 4, getX() + 4, getY() + 4, getZ() + 4)).forEach(entity -> {
                    if (entity instanceof LivingEntity target && target.isPartOfGame() && target.damage(DamageSource.mob(this), 15f)) {
                        BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, HeartMoney.IDENTIFIER, 14);

                        for (int i = 0; i < world.getRandom().nextBetween(10, 30); i++) {
                            ItemEntity itemEntity = new ItemEntity(world, target.getX(), target.getBodyY(0.5), target.getZ(), ItemRegistry.HEART.getDefaultStack(),
                                    world.getRandom().nextGaussian() * 0.3, world.getRandom().nextDouble() * 0.4, world.getRandom().nextGaussian() * 0.3);
                            itemEntity.setPickupDelay(5);
                            world.spawnEntity(itemEntity);
                        }
                    }
                });
            }
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (info.getReturnValue() && !world.isClient) {
            HeartMoney.updateEntityHealth(this, getHealth(), false);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("heartmoney$heart", getDataTracker().get(heartmoney$HEART));
        nbt.putInt("heartmoney$removeHearts", this.heartmoney$removeHearts);
        nbt.putInt("heartmoney$stepRemoveHearts", this.heartmoney$stepRemoveHearts);
        nbt.putDouble("heartmoney$heartMultiplier", this.heartmoney$heartMultiplier);

        if (this.heartmoney$heartShopPortal != null && !world.isClient) {
            this.heartmoney$heartShopPortal.setDead(true);
            this.heartmoney$heartShopPortal = null;
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(heartmoney$HEART, nbt.getBoolean("heartmoney$heart"));
        this.heartmoney$removeHearts = nbt.getInt("heartmoney$removeHearts");
        this.heartmoney$stepRemoveHearts = nbt.getInt("heartmoney$stepRemoveHearts");
        if (nbt.contains("heartmoney$heartMultiplier", NbtElement.INT_TYPE)) {
            this.heartmoney$heartMultiplier = nbt.getDouble("heartmoney$heartMultiplier");
        }
    }

    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    public void spawnSweepAttackParticles(CallbackInfo info) {
        if (getMainHandStack().isOf(ItemRegistry.LIFE_STEAL_SCYTHE)) {
            double d = -MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticles(ParticleRegistry.HEART_SWEEP_ATTACK, this.getX() + d, this.getBodyY(0.5), this.getZ() + e, 0, d, 0.0, e, 0.0);
                BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 16);
            }
            info.cancel();
        } else if (getMainHandStack().isOf(ItemRegistry.HEART_SWORD) || getMainHandStack().isOf(ItemRegistry.BLOOD_KATANA)) {
            info.cancel();
        }
    }

    @Inject(method = "addCritParticles", at = @At("HEAD"), cancellable = true)
    public void addCritParticles(Entity target, CallbackInfo info) {
        if (getMainHandStack().isOf(ItemRegistry.HEART_SWORD)) {
            for (int i = 0; i < 5; ++i) {
                double g = target.getX();
                double h = target.getBodyY(0.5);
                double j = target.getZ();
                world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
            }
            info.cancel();
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    public void attack(Entity target, CallbackInfo info) {
        if (target instanceof ShopItemEntity && isCreative()) {
            target.discard();
            info.cancel();
        }
    }

    public void heartmoney$setHeart(boolean heart) {
        getDataTracker().set(heartmoney$HEART, heart);
    }

    public boolean heartmoney$isHeart() {
        return getDataTracker().get(heartmoney$HEART);
    }

    public void heartmoney$addRemoveHearts(int heartmoney$removeHearts) {
        this.heartmoney$removeHearts += heartmoney$removeHearts;
        this.heartmoney$stepRemoveHearts = (int) Math.ceil((double) this.heartmoney$removeHearts / 20d);
    }

    public int heartmoney$getRemoveHearts() {
        return this.heartmoney$removeHearts;
    }

    public void heartmoney$setHeartShopPortal(HeartShopPortalEntity heartShopPortalEntity) {
        if (this.heartmoney$heartShopPortal != null && this.heartmoney$heartShopPortal.isAlive()) {
            this.heartmoney$heartShopPortal.setDead(true);
        }

        this.heartmoney$heartShopPortal = heartShopPortalEntity;
    }

    public void heartmoney$setHeartMultiplier(double heartMultiplier) {
        this.heartmoney$heartMultiplier = heartMultiplier;
    }

    public double heartmoney$getHeartMultiplier() {
        return this.heartmoney$heartMultiplier * (getOffHandStack().isOf(ItemRegistry.HOLY_HEART_RING) || getMainHandStack().isOf(ItemRegistry.HOLY_HEART_RING) ? 1.5 : 1);
    }

    public void heartmoney$setBloodRiptideTicks(int bloodRiptideTicks) {
        this.heartmoney$bloodRiptideTicks = bloodRiptideTicks;
        getDataTracker().set(heartmoney$BLOOD_RIPTIDE, this.heartmoney$bloodRiptideTicks > 0);
    }

    public boolean heartmoney$hasBloodRiptide() {
        return getDataTracker().get(heartmoney$BLOOD_RIPTIDE);
    }
}

