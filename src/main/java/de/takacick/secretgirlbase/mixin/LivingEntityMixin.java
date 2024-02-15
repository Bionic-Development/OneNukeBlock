package de.takacick.secretgirlbase.mixin;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.access.LivingProperties;
import de.takacick.secretgirlbase.registry.ParticleRegistry;
import de.takacick.secretgirlbase.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.secretgirlbase.registry.particles.goop.GoopParticleEffect;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "secretgirlbase$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    @Final
    public LimbAnimator limbAnimator;

    @Shadow
    public abstract float getBodyYaw();

    private static final TrackedData<Float> secretgirlbase$BUBBLE_GUM = BionicDataTracker.registerData(new Identifier(SecretGirlBase.MOD_ID, "bubble_gum"), TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> secretgirlbase$BUBBLE_GUM_DELAY = BionicDataTracker.registerData(new Identifier(SecretGirlBase.MOD_ID, "bubble_gum_decrease"), TrackedDataHandlerRegistry.BOOLEAN);
    private int secretgirlbase$bubbleGumDelay = 0;
    private float secretgirlbase$bubbleGum = 0;
    private float secretgirlbase$prevBubbleGum = 0;
    private int secretgirlbase$teenyBerryTicks = 0;
    private int secretgirlbase$greatBerryTicks = 0;
    private boolean secretgirlbase$sizeChange = false;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(secretgirlbase$BUBBLE_GUM, 0f);
        getDataTracker().startTracking(secretgirlbase$BUBBLE_GUM_DELAY, false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        this.secretgirlbase$prevBubbleGum = this.secretgirlbase$bubbleGum;
        if (!getDataTracker().get(secretgirlbase$BUBBLE_GUM_DELAY)) {
            this.secretgirlbase$bubbleGum = MathHelper.clamp(this.secretgirlbase$bubbleGum - 0.01f, 0f, 1f);
        }

        if (!getWorld().isClient) {
            if (this.secretgirlbase$bubbleGumDelay > 0) {
                this.secretgirlbase$bubbleGumDelay--;
            }

            getDataTracker().set(secretgirlbase$BUBBLE_GUM_DELAY, this.secretgirlbase$bubbleGumDelay > 0);

            secretgirlbase$setBubbleGumStrength(this.secretgirlbase$bubbleGum);
        } else {
            float bubbleGum = getDataTracker().get(secretgirlbase$BUBBLE_GUM);
            if (Math.abs(this.secretgirlbase$bubbleGum - bubbleGum) > 0.1) {
                this.secretgirlbase$bubbleGum = bubbleGum;
            }

            if (this.secretgirlbase$bubbleGum > 0) {
                if (this.limbAnimator.isLimbMoving() && getRandom().nextDouble() <= 0.6) {
                    Vec3d vec3d = getRotationVector(0f, getBodyYaw() - 90f);
                    if (getRandom().nextDouble() <= 0.5) {
                        vec3d = vec3d.multiply(-1);
                    }

                    Vec3d pos = getPos().add(this.random.nextGaussian() * 0.03, 0, this.random.nextGaussian() * 0.03)
                            .add(vec3d.getX() * getWidth() * 0.4, 0, vec3d.getZ() * getWidth() * 0.4);

                    this.getWorld().addParticle(new GoopParticleEffect(Vec3d.unpackRgb(0xEA1CD0).toVector3f(), getRandom().nextFloat() * 0.25f + 0.1f, new Vec3d(0, 1, 0)),
                            pos.getX(), pos.getY() + 0.002, pos.getZ(),
                            this.random.nextGaussian() * 0.01, 0.1, this.random.nextGaussian() * 0.01);
                }

                if (getRandom().nextDouble() <= 0.2) {
                    double g = getX();
                    double j = getZ();

                    for (int i = 0; i < 1; ++i) {
                        double h = getRandomBodyY();
                        double d = random.nextGaussian() * 0.2;
                        double e = random.nextDouble() * 0.3;
                        double f = random.nextGaussian() * 0.2;

                        getWorld().addParticle(new GoopDropParticleEffect(Vec3d.unpackRgb(0xEA1CD0).toVector3f(), getRandom().nextFloat() * 0.25f + 0.1f), true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                    }
                }
            }
        }

        if (!getWorld().isClient) {
            if (this.secretgirlbase$teenyBerryTicks > 0) {
                this.secretgirlbase$teenyBerryTicks--;

                ScaleData scaleDataWidth = ScaleTypes.BASE.getScaleData(this);
                float height = 0.25f;
                if (this.secretgirlbase$teenyBerryTicks <= 0) {
                    height = 1f;
                    getWorld().playSound(null, getX(), getBodyY(0.5), getZ(), ParticleRegistry.GROW_SOUND, getSoundCategory(), 0.75f, 1f);
                }
                if (scaleDataWidth.getTargetScale() != height && scaleDataWidth.getScale() != height) {
                    scaleDataWidth.setTargetScale(height);
                    this.secretgirlbase$sizeChange = true;
                }
            }

            if (this.secretgirlbase$greatBerryTicks > 0) {
                this.secretgirlbase$greatBerryTicks--;
                ScaleData scaleDataWidth = ScaleTypes.BASE.getScaleData(this);
                float height = 5f;
                if (this.secretgirlbase$greatBerryTicks <= 0) {
                    height = 1f;
                    getWorld().playSound(null, getX(), getBodyY(0.5), getZ(), ParticleRegistry.SHRINK_SOUND, getSoundCategory(), 0.75f, 1f);
                }

                if (scaleDataWidth.getTargetScale() != height && scaleDataWidth.getScale() != height) {
                    scaleDataWidth.setTargetScale(height);
                    this.secretgirlbase$sizeChange = true;
                }
            }

            if (this.secretgirlbase$sizeChange) {
                ScaleData scaleDataWidth = ScaleTypes.BASE.getScaleData(this);

                if (scaleDataWidth.getTargetScale() > scaleDataWidth.getScale()) {
                    BionicUtils.sendEntityStatus(getWorld(), this, SecretGirlBase.IDENTIFIER, 6);
                } else if (scaleDataWidth.getTargetScale() < scaleDataWidth.getScale()) {
                    BionicUtils.sendEntityStatus(getWorld(), this, SecretGirlBase.IDENTIFIER, 7);
                } else {
                    this.secretgirlbase$sizeChange = false;
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (this.secretgirlbase$bubbleGumDelay > 0) {
            nbt.putInt("secretgirlbase$bubbleGumDelay", this.secretgirlbase$bubbleGumDelay);
        }

        if (this.secretgirlbase$bubbleGum > 0) {
            nbt.putFloat("secretgirlbase$prevBubbleGum", this.secretgirlbase$prevBubbleGum);
            nbt.putFloat("secretgirlbase$bubbleGum", this.secretgirlbase$bubbleGum);
        }

        if (this.secretgirlbase$teenyBerryTicks > 0) {
            nbt.putInt("secretgirlbase$teenyBerryTicks", this.secretgirlbase$teenyBerryTicks);
        }

        if (this.secretgirlbase$greatBerryTicks > 0) {
            nbt.putInt("secretgirlbase$greatBerryTicks", this.secretgirlbase$greatBerryTicks);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("secretgirlbase$bubbleGumDelay", NbtElement.INT_TYPE)) {
            this.secretgirlbase$bubbleGumDelay = nbt.getInt("secretgirlbase$bubbleGumDelay");
            getDataTracker().set(secretgirlbase$BUBBLE_GUM_DELAY, this.secretgirlbase$bubbleGumDelay > 0);
        }

        if (nbt.contains("secretgirlbase$bubbleGum", NbtElement.FLOAT_TYPE)) {
            this.secretgirlbase$prevBubbleGum = nbt.getFloat("secretgirlbase$prevBubbleGum");
            this.secretgirlbase$bubbleGum = nbt.getFloat("secretgirlbase$bubbleGum");
            getDataTracker().set(secretgirlbase$BUBBLE_GUM, this.secretgirlbase$bubbleGum);
        }

        if (nbt.contains("secretgirlbase$teenyBerryTicks", NbtElement.INT_TYPE)) {
            this.secretgirlbase$teenyBerryTicks = nbt.getInt("secretgirlbase$teenyBerryTicks");
        }

        if (nbt.contains("secretgirlbase$greatBerryTicks", NbtElement.INT_TYPE)) {
            this.secretgirlbase$greatBerryTicks = nbt.getInt("secretgirlbase$greatBerryTicks");
        }
    }

    public void secretgirlbase$setBubbleGumStrength(float strength) {
        if (this.secretgirlbase$bubbleGum < strength) {
            getDataTracker().set(secretgirlbase$BUBBLE_GUM, strength);
            this.secretgirlbase$bubbleGumDelay = 40;
        }
        strength = MathHelper.clamp(strength, 0f, 1f);

        this.secretgirlbase$bubbleGum = strength;
        getDataTracker().set(secretgirlbase$BUBBLE_GUM, strength);
    }

    public float secretgirlbase$getBubbleGumStrength(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.secretgirlbase$prevBubbleGum, this.secretgirlbase$bubbleGum);
    }

    public void secretgirlbase$setTeenyBerryTicks(int teenyBerryTicks) {
        if(this.secretgirlbase$teenyBerryTicks <= 0) {
            getWorld().playSound(null, getX(), getBodyY(0.5), getZ(), ParticleRegistry.SHRINK_SOUND, getSoundCategory(), 0.75f, 1f);
        }
        this.secretgirlbase$teenyBerryTicks = teenyBerryTicks;
        this.secretgirlbase$sizeChange = true;

        if (teenyBerryTicks > 0) {
            this.secretgirlbase$greatBerryTicks = 0;
        }
    }

    public void secretgirlbase$setGreatBerryTicks(int greatBerryTicks) {
        if(this.secretgirlbase$greatBerryTicks <= 0) {
            getWorld().playSound(null, getX(), getBodyY(0.5), getZ(), ParticleRegistry.GROW_SOUND, getSoundCategory(), 0.75f, 1f);
        }
        this.secretgirlbase$greatBerryTicks = greatBerryTicks;
        this.secretgirlbase$sizeChange = true;

        if (greatBerryTicks > 0) {
            this.secretgirlbase$teenyBerryTicks = 0;
        }
    }
}