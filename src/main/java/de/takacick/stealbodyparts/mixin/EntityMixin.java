package de.takacick.stealbodyparts.mixin;

import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "calculateBoundingBox", at = @At("RETURN"), cancellable = true)
    public void calculateBoundingBox(CallbackInfoReturnable<Box> info) {
        if (this instanceof PlayerProperties playerProperties) {
            Box box = info.getReturnValue();

            double height = box.maxY - box.minY;
            double multiplier = 1d;

            if (!playerProperties.hasBodyPart(BodyPart.HEAD.getIndex())) {
                multiplier -= 0.25d;
            }

            if (!playerProperties.hasBodyPart(BodyPart.RIGHT_LEG.getIndex()) && !playerProperties.hasBodyPart(BodyPart.LEFT_LEG.getIndex())) {
                multiplier -= 0.375d;
            }

            if (multiplier != 1d) {
                info.setReturnValue(new Box(box.minX, box.minY, box.minZ, box.maxX, box.minY + height * multiplier, box.maxZ));
            }
        }
    }

    @Inject(method = "calculateBoundsForPose", at = @At("RETURN"), cancellable = true)
    public void calculateBoundsForPose(CallbackInfoReturnable<Box> info) {
        if (this instanceof PlayerProperties playerProperties) {
            Box box = info.getReturnValue();

            double height = box.maxY - box.minY;
            double multiplier = 1d;

            if (!playerProperties.hasBodyPart(BodyPart.HEAD.getIndex())) {
                multiplier -= 0.25d;
            }

            if (!playerProperties.hasBodyPart(BodyPart.RIGHT_LEG.getIndex()) && !playerProperties.hasBodyPart(BodyPart.LEFT_LEG.getIndex())) {
                multiplier -= 0.375d;
            }

            if (multiplier != 1d) {
                info.setReturnValue(new Box(box.minX, box.minY, box.minZ, box.maxX, box.minY + height * multiplier, box.maxZ));
            }
        }
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    public void getHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PlayerProperties playerProperties) {
            float multiplier = 1f;

            if (!playerProperties.hasBodyPart(BodyPart.HEAD.getIndex())) {
                multiplier -= 0.25f;
            }

            if (!playerProperties.hasBodyPart(BodyPart.RIGHT_LEG.getIndex()) && !playerProperties.hasBodyPart(BodyPart.LEFT_LEG.getIndex())) {
                multiplier -= 0.375f;
            }

            info.setReturnValue(info.getReturnValue() * multiplier);
        }
    }

    @Inject(method = "getEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F", at = @At("RETURN"), cancellable = true)
    public void getEyeHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PlayerProperties playerProperties) {
            float multiplier = 1f;

            if (!playerProperties.hasBodyPart(BodyPart.HEAD.getIndex())) {
                multiplier -= 0.25f;
            }

            if (!playerProperties.hasBodyPart(BodyPart.RIGHT_LEG.getIndex()) && !playerProperties.hasBodyPart(BodyPart.LEFT_LEG.getIndex())) {
                multiplier -= 0.375f;
            }

            info.setReturnValue(info.getReturnValue() * multiplier);
        }
    }

    @Inject(method = "getStandingEyeHeight", at = @At("RETURN"), cancellable = true)
    public void getStandingEyeHeight(CallbackInfoReturnable<Float> info) {
        if (this instanceof PlayerProperties playerProperties) {
            float multiplier = 1f;

            if (!playerProperties.hasBodyPart(BodyPart.HEAD.getIndex())) {
                multiplier -= 0.25f;
            }

            if (!playerProperties.hasBodyPart(BodyPart.RIGHT_LEG.getIndex()) && !playerProperties.hasBodyPart(BodyPart.LEFT_LEG.getIndex())) {
                multiplier -= 0.375f;
            }

            info.setReturnValue(info.getReturnValue() * multiplier);
        }
    }
}

