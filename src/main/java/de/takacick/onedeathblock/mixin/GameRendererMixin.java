package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.registry.entity.living.SuperbrineEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    public abstract Camera getCamera();

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        if (getCamera().getFocusedEntity() instanceof PlayerProperties playerProperties
                && (playerProperties.getMeteorShakeTicks() > 0 || playerProperties.getShockedTicks() > 0
                || getCamera().getFocusedEntity().getVehicle() instanceof SuperbrineEntity)) {
            float g = (float) (Math.cos((double) getCamera().getFocusedEntity().age * 3.25) * Math.PI * (double) 0.4f);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(g));
        }
    }
}