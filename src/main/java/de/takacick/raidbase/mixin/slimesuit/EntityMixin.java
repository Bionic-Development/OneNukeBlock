package de.takacick.raidbase.mixin.slimesuit;

import de.takacick.raidbase.access.PlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
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
    private Vec3d pos;

    @Shadow
    private float standingEyeHeight;

    @Shadow
    public abstract World getWorld();

    @Shadow
    @Final
    protected Random random;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Inject(method = "calculateBoundingBox", at = @At("RETURN"), cancellable = true)
    public void calculateBoundingBox(CallbackInfoReturnable<Box> info) {
        if (this instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit()) {
            Box box = info.getReturnValue();
            double height = 0.5f + (box.maxY - box.minY);
            info.setReturnValue(new Box(box.minX - 0.6, box.minY, box.minZ - 0.5, box.maxX + 0.5, box.minY + height, box.maxZ + 0.6));
        }
    }

    @Inject(method = "calculateBoundsForPose", at = @At("RETURN"), cancellable = true)
    public void calculateBoundsForPose(CallbackInfoReturnable<Box> info) {
        if (this instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit()) {
            Box box = info.getReturnValue();
            double height = 0.5f + (box.maxY - box.minY);

            info.setReturnValue(new Box(box.minX - 0.6, box.minY, box.minZ - 0.5, box.maxX + 0.5, box.minY + height, box.maxZ + 0.6));
        }
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    public void getHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit()) {
            info.setReturnValue(info.getReturnValue() + 0.5f);
        }
    }

    @Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
    public void getStandingEyeHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit()) {
            info.setReturnValue(info.getReturnValue() + 0.25f);
        }
    }

    @Inject(method = "getEyeY", at = @At("HEAD"), cancellable = true)
    public void getEyeY(CallbackInfoReturnable<Double> info) {
        if (this instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit()) {
            info.setReturnValue(this.pos.getY() + this.standingEyeHeight + 0.25f);
        }
    }
}