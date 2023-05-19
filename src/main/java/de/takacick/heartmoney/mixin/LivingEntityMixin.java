package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.access.LivingProperties;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.entity.projectiles.LifeStealScytheEntity;
import de.takacick.heartmoney.registry.item.HeartPulseBlaster;
import de.takacick.heartmoney.registry.item.HeartSword;
import de.takacick.heartmoney.registry.item.LifeStealScythe;
import de.takacick.heartmoney.registry.item.LoveBarrierSuit;
import de.takacick.heartmoney.registry.particles.ColoredParticleEffect;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "heartmoney$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract boolean clearStatusEffects();

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow
    protected abstract ItemStack getSyncedArmorStack(EquipmentSlot slot);

    private static final TrackedData<Boolean> heartmoney$HEART_KILLED = BionicDataTracker.registerData(new Identifier(HeartMoney.MOD_ID, "heart_killed"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> heartmoney$MAID_EXPLOSION = BionicDataTracker.registerData(new Identifier(HeartMoney.MOD_ID, "maid_explosion"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final int heartmoney$maxMaidExplosionTicks = 25;
    private int heartmoney$prevMaidExplosionTicks = 0;
    private int heartmoney$maidExplosionTicks = 0;
    private boolean heartmoney$loveBarrierSuit;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V", shift = At.Shift.BEFORE))
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        if (damageSource.getSource() instanceof LivingEntity livingEntity) {
            if (livingEntity.getMainHandStack().getItem() instanceof HeartSword) {
                dropItem(ItemRegistry.HEART);
                getDataTracker().set(heartmoney$HEART_KILLED, true);
            } else if (livingEntity.getMainHandStack().getItem() instanceof LifeStealScythe) {
                for (int i = 0; i < world.getRandom().nextBetween(50, 100); i++) {
                    ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.HEART.getDefaultStack(),
                            world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.35, world.getRandom().nextGaussian() * 0.25);
                    itemEntity.setPickupDelay(5);
                    world.spawnEntity(itemEntity);
                }
                getDataTracker().set(heartmoney$HEART_KILLED, true);
                BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 15);
            } else if (livingEntity.getMainHandStack().getItem() instanceof HeartPulseBlaster) {
                for (int i = 0; i < world.getRandom().nextBetween(20, 50); i++) {
                    ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.HEART.getDefaultStack(),
                            world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.35, world.getRandom().nextGaussian() * 0.25);
                    itemEntity.setPickupDelay(5);
                    world.spawnEntity(itemEntity);
                }
                getDataTracker().set(heartmoney$HEART_KILLED, true);
            }
        } else if (damageSource.getSource() instanceof LifeStealScytheEntity) {
            for (int i = 0; i < world.getRandom().nextBetween(50, 100); i++) {
                ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.HEART.getDefaultStack(),
                        world.getRandom().nextGaussian() * 0.25, world.getRandom().nextDouble() * 0.35, world.getRandom().nextGaussian() * 0.25);
                itemEntity.setPickupDelay(5);
                world.spawnEntity(itemEntity);
            }
            getDataTracker().set(heartmoney$HEART_KILLED, true);
            BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 15);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!this.world.isClient) {
            if (getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof LoveBarrierSuit) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, Integer.MAX_VALUE, 0, false, false, true));
                this.heartmoney$loveBarrierSuit = true;
            } else if (this.heartmoney$loveBarrierSuit) {
                BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 6);
                removeStatusEffect(StatusEffects.REGENERATION);
                this.heartmoney$loveBarrierSuit = false;
            }
        }

        if (getDataTracker().get(heartmoney$MAID_EXPLOSION)) {
            this.heartmoney$prevMaidExplosionTicks = this.heartmoney$maidExplosionTicks;
            this.heartmoney$maidExplosionTicks = Math.min(this.heartmoney$maidExplosionTicks + 1, heartmoney$maxMaidExplosionTicks);

            if (this.heartmoney$maidExplosionTicks % 5 == 0) {
                world.playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, getSoundCategory(), 0.35f, 8f, true);
            }
            if (this.heartmoney$maidExplosionTicks == heartmoney$maxMaidExplosionTicks) {
                if (!this.world.isClient) {
                    for (int i = 0; i < world.getRandom().nextBetween(1, 5); i++) {
                        ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.HEART.getDefaultStack(),
                                world.getRandom().nextGaussian() * 0.1, world.getRandom().nextDouble() * 0.21, world.getRandom().nextGaussian() * 0.1);
                        itemEntity.setPickupDelay(10);
                        world.spawnEntity(itemEntity);
                    }

                    BionicUtils.sendEntityStatus((ServerWorld) this.world, this, HeartMoney.IDENTIFIER, 2);
                    this.discard();
                }
            }
        } else if (this.heartmoney$maidExplosionTicks > 0) {
            this.heartmoney$prevMaidExplosionTicks = this.heartmoney$maidExplosionTicks;
            this.heartmoney$maidExplosionTicks -= 1;
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(heartmoney$HEART_KILLED, false);
        getDataTracker().startTracking(heartmoney$MAID_EXPLOSION, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("heartmoney$heartKilled", getDataTracker().get(heartmoney$HEART_KILLED));
        nbt.putBoolean("heartmoney$maidExplosion", getDataTracker().get(heartmoney$MAID_EXPLOSION));
        nbt.putBoolean("heartmoney$loveBarrierSuit", this.heartmoney$loveBarrierSuit);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(heartmoney$HEART_KILLED, nbt.getBoolean("heartmoney$heartKilled"));
        getDataTracker().set(heartmoney$MAID_EXPLOSION, nbt.getBoolean("heartmoney$maidExplosion"));
        this.heartmoney$loveBarrierSuit = nbt.getBoolean("heartmoney$loveBarrierSuit");
    }

    @Inject(method = "addDeathParticles", at = @At("HEAD"), cancellable = true)
    public void addDeathParticles(CallbackInfo info) {
        if (getDataTracker().get(heartmoney$HEART_KILLED)) {
            world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, getSoundCategory(), 1.0f, 6.0f, false);

            for (int i = 0; i < 5; ++i) {
                double g = getX();
                double h = getBodyY(0.5);
                double j = getZ();
                world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
            }
            for (int i = 0; i < 5; ++i) {
                double g = getX();
                double h = getBodyY(0.5);
                double j = getZ();
                world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
            }
            for (int i = 0; i < 15; ++i) {
                double d = world.getRandom().nextGaussian() * 0.02;
                double e = world.getRandom().nextGaussian() * 0.02;
                double f = world.getRandom().nextGaussian() * 0.02;
                world.addParticle(ParticleRegistry.HEART_POOF, getParticleX(1.0), getRandomBodyY(), getParticleZ(1.0), d, e, f);
            }
            info.cancel();
        }
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At(value = "HEAD"))
    public void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo info) {
        if (getMainHandStack().isOf(ItemRegistry.HEART_SWORD)) {
            double d = -MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticles(getMainHandStack().isOf(ItemRegistry.BLOOD_KATANA) ? ParticleRegistry.BLOOD_SWEEP_ATTACK : ParticleRegistry.HEART_SWEEP_ATTACK, this.getX() + d, this.getBodyY(0.5) + getRotationVector().getY() * 0.75, this.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0f, 1f);
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, this.getSoundCategory(), 0.2f, 1f);
        } else if (getMainHandStack().isOf(ItemRegistry.BLOOD_KATANA)) {
            double d = -MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticles(ParticleRegistry.BLOOD_SWEEP_ATTACK, this.getX() + d, this.getBodyY(0.5) + getRotationVector().getY() * 0.75, this.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0f, 1f);
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, this.getSoundCategory(), 0.2f, 1f);
        }
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At(value = "HEAD"), cancellable = true)
    private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (stack.isOf(ItemRegistry.MAID_SUIT_ARMOR) || stack.isOf(ItemRegistry.LOVE_BARRIER_SUIT)
                || stack.isOf(ItemRegistry.HEART_JET_PACK)) {
            info.setReturnValue(EquipmentSlot.CHEST);
        }
    }

    @ModifyVariable(method = "travel", at = @At("HEAD"), argsOnly = true)
    private Vec3d travel(Vec3d movementInput) {
        if (getDataTracker().get(heartmoney$MAID_EXPLOSION)) {
            return movementInput.multiply(0, 0, 0);
        }
        return movementInput;
    }

    @Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true)
    protected void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue()) {
            ItemStack itemStack = null;
            for (Hand hand : Hand.values()) {
                ItemStack itemStack2 = this.getStackInHand(hand);
                if (!itemStack2.isOf(ItemRegistry.LOVER_TOTEM)) continue;
                itemStack = itemStack2.copy();
                itemStack2.decrement(1);
                break;
            }
            if (itemStack != null) {
                if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
                    Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
                }
                this.setVelocity(0, 2, 0);
                this.velocityDirty = true;
                this.velocityModified = true;
                this.setHealth(1.0f);
                this.clearStatusEffects();
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 3));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 200, 0));
                BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 3);
            }
            info.setReturnValue(itemStack != null);
        }
    }

    @Inject(method = "getEquipmentChanges", at = @At(value = "RETURN"))
    private void getEquipmentChanges(CallbackInfoReturnable<@Nullable Map<EquipmentSlot, ItemStack>> info) {
        if (info.getReturnValue() != null) {
            EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;

            ItemStack oldStack = this.getSyncedArmorStack(equipmentSlot);
            ItemStack itemStack = this.getEquippedStack(equipmentSlot);

            if (!(oldStack.getItem() instanceof LoveBarrierSuit) && itemStack.getItem() instanceof LoveBarrierSuit) {
                BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 5);
            }
        }
    }

    public void heartmoney$setMaidExplosion(boolean maidExplosion) {
        getDataTracker().set(heartmoney$MAID_EXPLOSION, maidExplosion);
    }

    public boolean heartmoney$isMaidExploding() {
        return getDataTracker().get(heartmoney$MAID_EXPLOSION);
    }

    public float heartmoney$getMaidExplosionProgress(float tickDelta) {
        return MathHelper.getLerpProgress(MathHelper.lerp(tickDelta, this.heartmoney$prevMaidExplosionTicks, this.heartmoney$maidExplosionTicks), 0, heartmoney$maxMaidExplosionTicks);
    }
}