package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.entity.living.SecretGiantJumpySlimeEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract Box getBoundingBox();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract float getHeight();

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public abstract void setVelocity(Vec3d velocity);

    @Shadow
    public float fallDistance;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    @Final
    protected Random random;

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    public void adjustMovementForCollisions(Vec3d movement, CallbackInfoReturnable<Vec3d> info) {
        getWorld().getOtherEntities((Entity) (Object) this, getBoundingBox().stretch(movement))
                .stream()
                .filter(entity -> entity instanceof SecretGiantJumpySlimeEntity).findAny()
                .ifPresent(entity -> {
                    if (getPos().getY() > entity.getY() + (getHeight() * 0.75) && !entity.isSpectator()) {

                        if (!getWorld().isClient) {
                            BionicUtils.sendEntityStatus(getWorld(), (Entity) (Object) entity, SecretCraftBase.IDENTIFIER, 5);
                        }

                        setVelocity(getVelocity().multiply(1.0, 0.0, 1.0).add(0, 10, 0));
                        this.fallDistance = 0;
                        info.setReturnValue(movement);
                    }
                });
    }
}

