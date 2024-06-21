package de.takacick.onescaryblock.mixin.bloodfluid.overlay;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.BloodProperties;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.onescaryblock.registry.block.fluid.BloodFluid;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = BloodProperties.class, prefix = "onescaryblock$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract float getBodyYaw();

    @Unique
    private static final TrackedData<Float> onescaryblock$BLOOD_OVERLAY = BionicDataTracker.registerData(new Identifier(OneScaryBlock.MOD_ID, "blood_overlay"), TrackedDataHandlerRegistry.FLOAT);
    private float onescaryblock$blood = 0;
    private float onescaryblock$prevBlood = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onescaryblock$BLOOD_OVERLAY, 0f);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        this.onescaryblock$prevBlood = this.onescaryblock$blood;
        if (getFluidHeight(BloodFluid.BLOOD) > 0) {
            this.onescaryblock$blood = MathHelper.clamp(this.onescaryblock$blood + 0.05f, 0f, 1f);
        } else {
            this.onescaryblock$blood = MathHelper.clamp(this.onescaryblock$blood - (isInsideWaterOrBubbleColumn() ?  0.2f : 0.01f), 0f, 1f);
        }

        if (!getWorld().isClient) {
            onescaryblock$setBloodOverlayStrength(this.onescaryblock$blood);
        } else {
            float blood = onescaryblock$getBloodStrength(1f);
            if (Math.abs(this.onescaryblock$blood - blood) > 0.1) {
                this.onescaryblock$blood = blood;
            }

            if (this.onescaryblock$blood > 0 && getFluidHeight(BloodFluid.BLOOD) < 0.7) {
                double g = getX();
                double j = getZ();

                for (int i = 0; i < 1; ++i) {
                    double h = getRandomBodyY();
                    double d = random.nextGaussian() * 0.2;
                    double e = random.nextDouble() * 0.3;
                    double f = random.nextGaussian() * 0.2;

                    getWorld().addParticle(ParticleRegistry.FALLING_BLOOD,
                            true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                    if (getRandom().nextDouble() <= 0.2) {
                        getWorld().playSound(g + d, h + e, j + f, ParticleRegistry.BLOOD_DROP, SoundCategory.BLOCKS, 1f, 1f + getRandom().nextFloat() * 0.2f, true);
                    }
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (onescaryblock$getBloodStrength(1f) > 0) {
            nbt.putFloat("onescaryblock$bloodOverlay", onescaryblock$getBloodStrength(1f));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("onescaryblock$bloodOverlay", NbtElement.FLOAT_TYPE)) {
            getDataTracker().set(onescaryblock$BLOOD_OVERLAY, nbt.getFloat("onescaryblock$bloodOverlay"));
        }
    }

    public void onescaryblock$setBloodOverlayStrength(float strength) {
        strength = MathHelper.clamp(strength, 0f, 1f);

        getDataTracker().set(onescaryblock$BLOOD_OVERLAY, strength);
    }

    public float onescaryblock$getBloodStrength(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.onescaryblock$prevBlood, this.onescaryblock$blood);
    }
}