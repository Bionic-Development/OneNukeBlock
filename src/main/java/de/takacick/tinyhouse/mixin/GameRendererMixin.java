package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.EntityProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    MinecraftClient client;

    @Shadow
    protected abstract void tiltViewWhenHurt(MatrixStack matrices, float tickDelta);

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getBobView()Lnet/minecraft/client/option/SimpleOption;", shift = At.Shift.BEFORE))
    private void renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        ClientPlayerEntity playerEntity = client.player;
        boolean bl = this.client.getCameraEntity() instanceof LivingEntity && ((LivingEntity) this.client.getCameraEntity()).isSleeping();
        if (playerEntity instanceof EntityProperties entityProperties && entityProperties.isStuckInsidePiston()
                && !(this.client.options.getPerspective().isFirstPerson() && !bl)) {
            InGameOverlayRenderer.renderOverlays(this.client, matrices);
            this.tiltViewWhenHurt(matrices, tickDelta);
        }
    }
}
