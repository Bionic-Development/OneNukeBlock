package de.takacick.stealbodyparts.mixin;

import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.registry.entity.custom.renderer.CarvedHeartFeatureRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new CarvedHeartFeatureRenderer<>(this));
    }

    @Inject(method = "renderArm", at = @At("HEAD"), cancellable = true)
    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {
        if (((PlayerProperties) player).getHeartRemovalState().isRunning()) {
            System.out.println("TEST");
            PlayerEntityModel playerEntityModel = this.getModel();
            playerEntityModel.handSwingProgress = 0.0f;
            playerEntityModel.sneaking = false;
            playerEntityModel.leaningPitch = 0.0f;

            float tickDelta = MinecraftClient.getInstance().getTickDelta();
            playerEntityModel.setAngles(player, 0.0f, tickDelta, getAnimationProgress(player, tickDelta), 0.0f, 0.0f);
            playerEntityModel.setAngles(player, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

        }
    }
}