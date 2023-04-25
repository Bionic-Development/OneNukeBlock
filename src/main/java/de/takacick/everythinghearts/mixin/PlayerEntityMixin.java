package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.LivingProperties;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.ParticleRegistry;
import de.takacick.everythinghearts.registry.particles.ColoredParticleEffect;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
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
@Implements({@Interface(iface = PlayerProperties.class, prefix = "everythinghearts$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract SoundCategory getSoundCategory();

    private static final TrackedData<Boolean> everythinghearts$HEART = BionicDataTracker.registerData(new Identifier(EverythingHearts.MOD_ID, "heart"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> everythinghearts$TRANSFORM = BionicDataTracker.registerData(new Identifier(EverythingHearts.MOD_ID, "heart_transform"), TrackedDataHandlerRegistry.INTEGER);
    private boolean everythinghearts$heartTouch = false;
    private int everythinghearts$heartTouchLevel = 1;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(everythinghearts$HEART, false);
        getDataTracker().startTracking(everythinghearts$TRANSFORM, -1);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!world.isClient) {
            if (everythinghearts$hasHeartTouch()) {
                World world = getEntityWorld();
                Box box = getBoundingBox().expand(isHolding(ItemRegistry.LOVER_TOTEM) ? 4.0 : 0f);
                BlockPos blockPos = new BlockPos(box.minX + 0.001, box.minY - 0.2, box.minZ + 0.001);
                BlockPos blockPos2 = new BlockPos(box.maxX - 0.001, box.maxY - 0.001, box.maxZ - 0.001);
                BlockPos.Mutable mutable = new BlockPos.Mutable();
                for (int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                    for (int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                        for (int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                            mutable.set(i, j, k);

                            BlockState blockState = world.getBlockState(mutable);
                            if (!blockState.isAir() && (blockState.getMaterial().blocksMovement()
                                    || blockState.getMaterial().isLiquid() || blockState.getBlock() instanceof FluidFillable)) {
                                EverythingHearts.replaceBlock(world, blockState, mutable, everythinghearts$getHeartTouchLevel());
                            }
                        }
                    }
                }

                if (isHolding(ItemRegistry.LOVER_TOTEM)) {
                    world.getOtherEntities(this, box).forEach(entity -> {
                        if (entity instanceof LivingProperties livingProperties) {
                            livingProperties.tryToHeartInfect(this);
                        }
                    });
                }
            }
        }

        if (everythinghearts$getHeartTransformTicks() > 0) {
            int transformTicks = everythinghearts$getHeartTransformTicks() - 1;
            addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, transformTicks, 0, false, false, false));

            everythinghearts$setHeartTransformTicks(transformTicks);

            if (transformTicks > 0) {
                if (transformTicks % 4 == 0) {
                    EverythingHearts.updateEntityHealth(this, Math.floor(transformTicks / 2f), false);

                    world.playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_WARDEN_HEARTBEAT, getSoundCategory(), 0.8f, 3f, true);
                }

                if (world.isClient) {
                    for (int i = 0; i < 2; ++i) {
                        double g = getX();
                        double h = getBodyY(0.5);
                        double j = getZ();
                        world.addParticle(ParticleRegistry.HEART, true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                        world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(0xFF1313))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
                    }
                }
            } else {
                if (!world.isClient) {
                    EverythingHearts.updateEntityHealth(this, 2, true);
                    addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 5, 0, false, false, false));

                    everythinghearts$setHeart(true);
                    everythinghearts$setHeartTouch(true);
                    BionicUtils.sendEntityStatus((ServerWorld) world, this, EverythingHearts.IDENTIFIER, 2);
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("everythinghearts$heart", getDataTracker().get(everythinghearts$HEART));
        nbt.putInt("everythinghearts$transform", getDataTracker().get(everythinghearts$TRANSFORM));
        nbt.putBoolean("everythinghearts$hearttouch", this.everythinghearts$heartTouch);
        nbt.putInt("everythinghearts$heartTouchLevel", this.everythinghearts$heartTouchLevel);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(everythinghearts$HEART, nbt.getBoolean("everythinghearts$heart"));
        getDataTracker().set(everythinghearts$TRANSFORM, nbt.getInt("everythinghearts$transform"));
        this.everythinghearts$heartTouch = nbt.getBoolean("everythinghearts$heartTouch");
        this.everythinghearts$heartTouchLevel = Math.min(Math.max(nbt.getInt("everythinghearts$heartTouchLevel"), 1), 2);
    }

    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    public void spawnSweepAttackParticles(CallbackInfo info) {
        if (getMainHandStack().isOf(ItemRegistry.WOODEN_HEART_SWORD)) {
            info.cancel();
        }
    }

    public void everythinghearts$setHeart(boolean heart) {
        getDataTracker().set(everythinghearts$HEART, heart);
    }

    public boolean everythinghearts$isHeart() {
        return getDataTracker().get(everythinghearts$HEART);
    }

    public void everythinghearts$setHeartTouch(boolean heartTouch) {
        this.everythinghearts$heartTouch = heartTouch;
    }

    public boolean everythinghearts$hasHeartTouch() {
        return this.everythinghearts$heartTouch && everythinghearts$isHeart();
    }

    public void everythinghearts$setHeartTouchLevel(int heartTouchLevel) {
        this.everythinghearts$heartTouchLevel = heartTouchLevel;
    }

    public int everythinghearts$getHeartTouchLevel() {
        return this.everythinghearts$heartTouchLevel;
    }

    public void everythinghearts$setHeartTransformTicks(int heartTransformTicks) {
        getDataTracker().set(everythinghearts$TRANSFORM, heartTransformTicks);
    }

    public int everythinghearts$getHeartTransformTicks() {
        return getDataTracker().get(everythinghearts$TRANSFORM);
    }
}

