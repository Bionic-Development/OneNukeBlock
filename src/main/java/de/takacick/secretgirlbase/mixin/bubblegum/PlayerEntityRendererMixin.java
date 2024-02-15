package de.takacick.secretgirlbase.mixin.bubblegum;

import de.takacick.secretgirlbase.access.LivingProperties;
import de.takacick.secretgirlbase.client.shaders.SecretGirlBaseLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/PlayerEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER), cancellable = true)
    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {
        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
        if (player instanceof LivingProperties livingProperties
                && livingProperties.getBubbleGumStrength(tickDelta) > 0f) {
            Identifier identifier = player.getSkinTextures().texture();

            arm.pitch = 0.0f;
            arm.render(matrices, vertexConsumers.getBuffer(SecretGirlBaseLayers.getBubbleGum(identifier, livingProperties.getBubbleGumStrength(tickDelta))), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            sleeve.pitch = 0.0f;
            sleeve.render(matrices, vertexConsumers.getBuffer(SecretGirlBaseLayers.getBubbleGum(identifier, livingProperties.getBubbleGumStrength(tickDelta))), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            info.cancel();
        }
    }
}
