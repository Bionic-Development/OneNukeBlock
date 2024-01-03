package de.takacick.raidbase.mixin;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.LivingProperties;
import de.takacick.raidbase.access.PigProperties;
import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.raidbase.registry.ParticleRegistry;
import de.takacick.raidbase.registry.block.LightningWaterBlock;
import de.takacick.raidbase.registry.particles.TargetParticleEffect;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "raidbase$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract float getYaw(float tickDelta);

    @Shadow
    public abstract void setSprinting(boolean sprinting);

    @Shadow
    public abstract float getHeadYaw();

    @Shadow
    public abstract boolean isInsideWall();

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    public int hurtTime;
    @Shadow
    public int maxHurtTime;

    @Shadow
    protected abstract void tickHandSwing();

    @Shadow
    public float lastHandSwingProgress;
    @Shadow
    public float handSwingProgress;

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    private static final TrackedData<Integer> raidbase$BANNED = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "banned"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> raidbase$ELECTRO_SHOCK = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "water_electro_shock"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> raidbase$GLITCHING = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "glitching"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> raidbase$PIE = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "pie"), TrackedDataHandlerRegistry.BOOLEAN);
    private int raidbase$waterElectroShock = 0;
    private Entity raidbase$renderEntity;
    private int raidbase$glitchingTicks = 0;
    private int raidbase$glitching = 0;
    private int raidbase$prevGlitching = 0;
    private int raidbase$glitchTime = 0;
    private int raidbase$pieTicks = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(raidbase$BANNED, -1);
        getDataTracker().startTracking(raidbase$ELECTRO_SHOCK, false);
        getDataTracker().startTracking(raidbase$GLITCHING, false);
        getDataTracker().startTracking(raidbase$PIE, false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (this.raidbase$pieTicks > 0) {
                raidbase$setPieTicks(this.raidbase$pieTicks - 1);

                if(!raidbase$hasPie() && isPlayer()) {
                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, RaidBase.IDENTIFIER, 7);
                }
            }

            if (this.raidbase$glitchingTicks > 0) {
                raidbase$setGlitchy(this.raidbase$glitchingTicks - 1);
                this.raidbase$glitchTime++;

                ((ServerWorld) getWorld()).spawnParticles(new TargetParticleEffect(ParticleRegistry.HACK_TARGET, getId()), getX(), getBodyY(0.5), getZ(), 3, 2, 2, 2, 0);

                if (getWorld().getRandom().nextBetween(40, 80) < this.raidbase$glitchTime) {
                    this.raidbase$glitchTime = 0;
                    this.damage(getWorld().getDamageSources().cramming(), 3);
                }
            } else {
                this.raidbase$glitchTime = 0;
            }
        } else {
            if (raidbase$isGlitchy()) {
                this.raidbase$prevGlitching = this.raidbase$glitching;
                this.raidbase$glitching += 1;
            } else {
                this.raidbase$prevGlitching = 0;
                this.raidbase$glitching = 0;
            }
        }

        if (this.raidbase$waterElectroShock > 0) {
            this.raidbase$waterElectroShock--;
            if (age % 3 == 0) {
                damage(getWorld().getDamageSources().create(LightningWaterBlock.LIGHTNING_WATER), 1.5f);
            }
            if (this.raidbase$waterElectroShock <= 0) {
                this.raidbase$setWaterElectroShock(0);
            }
        }

        if (raidbase$isGettingBanned()) {
            int banTicks = getDataTracker().get(raidbase$BANNED) - 1;
            raidbase$setGettingBanned(banTicks);

            if (banTicks >= 0) {
                if (!getWorld().isClient) {
                    if (banTicks % 5 == 0) {
                        getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT, 0.75f, 2.4f);
                    }

                    if (banTicks == 15) {
                        setVelocity(0, 0, 0);
                        velocityModified = true;
                        BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, RaidBase.IDENTIFIER, 3);
                    }

                    addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, banTicks, 0, false, false, false));
                    if (banTicks == 0) {
                        raidbase$setGettingBanned(-1);
                        LightningEntity lightningEntity = new LightningEntity(EntityRegistry.BAN_LIGHTNING, getWorld());
                        lightningEntity.setPos(getX(), getY() - 0.3, getZ());
                        lightningEntity.setCosmetic(true);
                        getWorld().spawnEntity(lightningEntity);
                        if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
                            serverPlayerEntity.networkHandler.disconnect(Text.translatable("multiplayer.disconnect.banned"));
                        } else {
                            discard();
                        }
                    }
                } else {
                    for (int x = 0; x < 2; x++) {
                        double d = getX() + 0.6 * getWorld().getRandom().nextGaussian();
                        double e = getY() + 0.6 * getWorld().getRandom().nextGaussian();
                        double f = getZ() + 0.6 * getWorld().getRandom().nextGaussian();
                        getWorld().addParticle(ParticleRegistry.GLITCHED, d, e, f,
                                MathHelper.nextBetween(getWorld().getRandom(), -1.0F, 1.0F) * 0.083333336F,
                                0.05000000074505806D, MathHelper.nextBetween(getWorld().getRandom(), -1.0F, 1.0F) * 0.083333336F);
                    }

                    if (getWorld().getRandom().nextDouble() <= 0.4) {
                        for (int i = 0; i < 10; i++) {
                            EntityType<?> entityType1 = Registries.ENTITY_TYPE.getRandom(getWorld().getRandom()).get().value();
                            if (entityType1.getTranslationKey().contains("minecraft")) {
                                if (entityType1.create(getWorld()) instanceof LivingEntity livingEntity) {
                                    this.raidbase$renderEntity = livingEntity;

                                    if (livingEntity instanceof MobEntity mobEntity) {
                                        SoundEvent soundEvent = mobEntity.getAmbientSound();
                                        if (soundEvent != null) {
                                            getWorld().playSound(getX(), getY(), getZ(),
                                                    soundEvent,
                                                    SoundCategory.AMBIENT, 1f, mobEntity.getPitch(), true);
                                        }
                                    }
                                    livingEntity.hurtTime = hurtTime;
                                    livingEntity.maxHurtTime = maxHurtTime;
                                    break;
                                }
                            }
                        }
                    } else {
                        this.raidbase$renderEntity = null;
                    }
                }
            }
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tickMovement(CallbackInfo info) {
        if (this instanceof PigProperties pigProperties && pigProperties.isPigSoldier()) {
            this.lastHandSwingProgress = this.handSwingProgress;
            tickHandSwing();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (raidbase$isGettingBanned()) {
            nbt.putInt("raidbase$banned", raidbase$getBanTicks());
        }

        if (this.raidbase$waterElectroShock > 0) {
            nbt.putInt("raidbase$waterElectroShock", this.raidbase$waterElectroShock);
        }

        if (this.raidbase$glitchingTicks > 0) {
            nbt.putInt("raidbase$glitchingTicks", this.raidbase$glitchingTicks);
        }

        if (this.raidbase$pieTicks > 0) {
            nbt.putInt("raidbase$pieTicks", this.raidbase$pieTicks);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("raidbase$banned", NbtElement.INT_TYPE)) {
            raidbase$setGettingBanned(nbt.getInt("raidbase$banned"));
        }

        this.raidbase$waterElectroShock = nbt.getInt("raidbase$waterElectroShock");
        this.raidbase$glitchingTicks = nbt.getInt("raidbase$glitchingTicks");
        this.raidbase$pieTicks = nbt.getInt("raidbase$pieTicks");
    }

    public void raidbase$setGettingBanned(int banTicks) {
        getDataTracker().set(raidbase$BANNED, banTicks);
    }

    public boolean raidbase$isGettingBanned() {
        return getDataTracker().get(raidbase$BANNED) >= 0;
    }

    public int raidbase$getBanTicks() {
        return getDataTracker().get(raidbase$BANNED);
    }

    public Entity raidbase$getRenderEntity() {
        return this.raidbase$renderEntity;
    }

    public void raidbase$setWaterElectroShock(int waterElectroShock) {
        this.raidbase$waterElectroShock = waterElectroShock;
        getDataTracker().set(raidbase$ELECTRO_SHOCK, waterElectroShock > 0);
    }

    public boolean raidbase$isWaterElectroShocked() {
        return getDataTracker().get(raidbase$ELECTRO_SHOCK);
    }

    public void raidbase$setGlitchy(int glitchingTicks) {
        this.raidbase$glitchingTicks = glitchingTicks;
        getDataTracker().set(raidbase$GLITCHING, glitchingTicks > 0);
    }

    public boolean raidbase$isGlitchy() {
        return getDataTracker().get(raidbase$GLITCHING);
    }

    public float raidbase$getGlitchSytrength(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.raidbase$prevGlitching, this.raidbase$glitching);
    }

    public void raidbase$setPieTicks(int pieTicks) {
        this.raidbase$pieTicks = pieTicks;
        getDataTracker().set(raidbase$PIE, pieTicks > 0);
    }

    public boolean raidbase$hasPie() {
        return getDataTracker().get(raidbase$PIE);
    }
}