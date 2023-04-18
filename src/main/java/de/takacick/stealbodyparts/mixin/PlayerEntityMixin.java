package de.takacick.stealbodyparts.mixin;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.registry.ItemRegistry;
import de.takacick.stealbodyparts.registry.particles.goop.GoopDropParticleEffect;
import de.takacick.stealbodyparts.utils.BodyPart;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "stealbodyparts$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract SoundCategory getSoundCategory();

    private static final TrackedData<Byte> stealbodyparts$BODY_PARTS = BionicDataTracker.registerData(new Identifier(StealBodyParts.MOD_ID, "parts"), TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> stealbodyparts$HEART = BionicDataTracker.registerData(new Identifier(StealBodyParts.MOD_ID, "heart"), TrackedDataHandlerRegistry.BOOLEAN);
    private final AnimationState stealbodyparts$heartRemovalAnimationState = new AnimationState();
    private int stealbodyparts$heartRemovalTicks = 0;
    private BodyPart stealbodyparts$nextBodyPart;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(stealbodyparts$BODY_PARTS, (byte) 0);
        getDataTracker().startTracking(stealbodyparts$HEART, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!world.isClient) {
            if (this.stealbodyparts$heartRemovalTicks > 0) {
                this.stealbodyparts$heartRemovalTicks--;

                if (this.stealbodyparts$heartRemovalTicks <= 0) {
                    Vec3d right = getRotationVector(getPitch(), getYaw() + 90).multiply(0.05);
                    Vec3d vel = getRotationVector().multiply(0.35).add(right.getX() * world.getRandom().nextGaussian(), 0.1, right.getZ() * world.getRandom().nextGaussian());
                    ItemEntity itemEntity
                            = new ItemEntity(world, getX(), getBodyY(0.65), getZ(), ItemRegistry.HEART.getDefaultStack(), vel.getX(), vel.getY(), vel.getZ());
                    itemEntity.setPickupDelay(30);
                    itemEntity.setOwner(getUuid());
                    world.spawnEntity(itemEntity);

                    StealBodyParts.updateEntityHealth(this, 18, false);
                }
            }
        } else {
            if (this.stealbodyparts$heartRemovalAnimationState.isRunning()) {
                Vec3d vec3d = getRotationVector().multiply(0.15);
                Vec3d right = getRotationVector(getPitch(), getYaw() + 90).multiply(0.05);
                if (age % 15 == 0) {
                    world.playSound(getX(), getBodyY(0.65), getZ(), SoundEvents.ENTITY_GENERIC_HURT, getSoundCategory(), 1f, 1f, true);
                    animateDamage();
                }

                for (int i = 0; i < 2; i++) {
                    Vec3d vel = vec3d.add(right.getX() * world.getRandom().nextGaussian(), 0, right.getZ() * world.getRandom().nextGaussian());

                    world.addParticle(new GoopDropParticleEffect(new Vec3f(Vec3d.unpackRgb(0x8a0303)),
                                    (float) (world.getRandom().nextDouble() * 0.5f)), getX(), getBodyY(0.65), getZ(),
                            vel.getX(), vel.getY(), vel.getZ());
                }

                if (this.stealbodyparts$heartRemovalAnimationState.getTimeRunning() >= 1.57f * 1000L) {
                    animateDamage();
                    this.stealbodyparts$heartRemovalAnimationState.stop();
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putByte("stealbodyparts$bodyparts", getDataTracker().get(stealbodyparts$BODY_PARTS));
        nbt.putBoolean("stealbodyparts$removedHeart", getDataTracker().get(stealbodyparts$HEART));
        nbt.putInt("stealbodyparts$heartRemovalTicks", this.stealbodyparts$heartRemovalTicks);
        if (this.stealbodyparts$nextBodyPart != null) {
            nbt.putString("stealbodyparts$nextBodyPart", this.stealbodyparts$nextBodyPart.getName());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(stealbodyparts$BODY_PARTS, nbt.getByte("stealbodyparts$bodyparts"));
        getDataTracker().set(stealbodyparts$HEART, nbt.getBoolean("stealbodyparts$removedHeart"));
        this.stealbodyparts$heartRemovalTicks = nbt.getInt("stealbodyparts$heartRemovalTicks");

        if (nbt.contains("stealbodyparts$nextBodyPart", NbtCompound.STRING_TYPE)) {
            this.stealbodyparts$nextBodyPart = BodyPart.getByName(nbt.getString("stealbodyparts$nextBodyPart"));
        }
    }

    public AnimationState stealbodyparts$getHeartRemovalState() {
        return stealbodyparts$heartRemovalAnimationState;
    }

    public void stealbodyparts$setRemovedHeart(boolean removedHeart) {
        getDataTracker().set(stealbodyparts$HEART, removedHeart);
    }

    public boolean stealbodyparts$removedHeart() {
        return getDataTracker().get(stealbodyparts$HEART);
    }

    public void stealbodyparts$setHeartRemovalTicks(int heartRemovalTicks) {
        this.stealbodyparts$heartRemovalTicks = heartRemovalTicks;
    }

    public void stealbodyparts$setBodyPart(int index, boolean value) {
        byte b = getDataTracker().get(stealbodyparts$BODY_PARTS);
        if (!value) {
            getDataTracker().set(stealbodyparts$BODY_PARTS, (byte) (b | 1 << index));
        } else {
            getDataTracker().set(stealbodyparts$BODY_PARTS, (byte) (b & ~(1 << index)));
        }
    }

    public boolean stealbodyparts$hasBodyPart(int index) {
        return (getDataTracker().get(stealbodyparts$BODY_PARTS) & 1 << index) == 0;
    }

    public void stealbodyparts$setNextBodyPart(BodyPart bodyPart) {
        this.stealbodyparts$nextBodyPart = bodyPart;
    }

    public BodyPart stealbodyparts$getNextBodyPart() {
        return this.stealbodyparts$nextBodyPart;
    }
}

