package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.IllegalWarsClient;
import de.takacick.illegalwars.access.LivingProperties;
import de.takacick.illegalwars.access.PiglinProperties;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.block.SludgeLiquidBlock;
import de.takacick.illegalwars.registry.block.entity.PiglinGoldTurretBlockEntity;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
@Implements({@Interface(iface = LivingProperties.class, prefix = "illegalwars$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    protected abstract void updateLimbs(boolean posDelta);

    private static final TrackedData<Boolean> illegalwars$POOP = BionicDataTracker.registerData(new Identifier(IllegalWars.MOD_ID, "poop"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> illegalwars$SLUDGE = BionicDataTracker.registerData(new Identifier(IllegalWars.MOD_ID, "sludge"), TrackedDataHandlerRegistry.FLOAT);
    private int illegalwars$poopTicks = 0;
    private float illegalwars$sludge = 0;
    private float illegalwars$prevSludge = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(illegalwars$POOP, false);
        getDataTracker().startTracking(illegalwars$SLUDGE, 0f);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (this.illegalwars$poopTicks > 0) {
                illegalwars$setPoopTicks(this.illegalwars$poopTicks - 1);

                if (!illegalwars$hasPoop() && isPlayer()) {
                    BionicUtils.sendEntityStatus(getWorld(), this, IllegalWars.IDENTIFIER, 4);
                }
            }
        } else {
            if (illegalwars$hasPoop() && getRandom().nextDouble() <= 0.25) {
                IllegalWarsClient.addPotion(getPos().add(0, getHeight() / 2, 0), 0x462A14, 1);
            }
        }

        this.illegalwars$prevSludge = this.illegalwars$sludge;
        if (getFluidHeight(SludgeLiquidBlock.SLUDGE) > 0) {
            this.illegalwars$sludge = MathHelper.clamp(this.illegalwars$sludge + 0.05f, 0f, 1f);
        } else {
            this.illegalwars$sludge = MathHelper.clamp(this.illegalwars$sludge - 0.01f, 0f, 1f);
        }

        if (!getWorld().isClient) {
            illegalwars$setSludgeStrength(this.illegalwars$sludge);
        } else {
            float sludge = getDataTracker().get(illegalwars$SLUDGE);
            if (Math.abs(this.illegalwars$sludge - sludge) > 0.1) {
                this.illegalwars$sludge = sludge;
            }

            if (this.illegalwars$sludge > 0) {
                double g = getX();
                double j = getZ();

                for (int i = 0; i < 1; ++i) {
                    double h = getRandomBodyY();
                    double d = random.nextGaussian() * 0.2;
                    double e = random.nextDouble() * 0.3;
                    double f = random.nextGaussian() * 0.2;

                    getWorld().addParticle(ParticleRegistry.FALLING_SLUDGE,
                            true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
                    if (getRandom().nextDouble() <= 0.2) {
                        getWorld().playSound(g + d, h + e, j + f, SoundEvents.BLOCK_MUD_BREAK, SoundCategory.BLOCKS, 1f, 1f, true);
                    }
                }
            }
        }

        if (!getWorld().isClient) {
            if(this instanceof PiglinProperties piglinProperties)
            if (piglinProperties.isUsingPiglinGoldTurret()) {
                if (!(getWorld().getBlockEntity(piglinProperties.getPiglinGoldTurret()) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity
                        && equals(piglinGoldTurretBlockEntity.getShooter())
                        && piglinGoldTurretBlockEntity.getPos().isWithinDistance(getPos(), 2))) {
                    if (getWorld().getBlockEntity(piglinProperties.getPiglinGoldTurret()) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity) {
                        if (equals(piglinGoldTurretBlockEntity.getShooter())) {
                            piglinGoldTurretBlockEntity.setShooter(null);
                            piglinProperties.setPiglinGoldTurret(null);
                        } else if (piglinGoldTurretBlockEntity.getShooter() == null
                                && piglinGoldTurretBlockEntity.getPos().isWithinDistance(getPos(), 2)) {
                            piglinGoldTurretBlockEntity.setShooter((PiglinEntity) (Object) this);
                        }
                    } else {
                        piglinProperties.setPiglinGoldTurret(null);
                    }
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (this.illegalwars$poopTicks > 0) {
            nbt.putInt("illegalwars$poopTicks", this.illegalwars$poopTicks);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.illegalwars$poopTicks = nbt.getInt("illegalwars$poopTicks");
    }

    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    public void travel(Vec3d movementInput, CallbackInfo info) {
        if (this instanceof PiglinProperties piglinProperties
                && piglinProperties.isUsingPiglinGoldTurret()) {
            this.updateLimbs(this instanceof Flutterer);
            info.cancel();
        }
    }

    public void illegalwars$setPoopTicks(int poopTicks) {
        this.illegalwars$poopTicks = poopTicks;
        getDataTracker().set(illegalwars$POOP, poopTicks > 0);
    }

    public boolean illegalwars$hasPoop() {
        return getDataTracker().get(illegalwars$POOP);
    }

    public void illegalwars$setSludgeStrength(float strength) {
        this.illegalwars$sludge = strength;
        getDataTracker().set(illegalwars$SLUDGE, strength);
    }

    public float illegalwars$getSludgeStrength(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.illegalwars$prevSludge, this.illegalwars$sludge);
    }

}