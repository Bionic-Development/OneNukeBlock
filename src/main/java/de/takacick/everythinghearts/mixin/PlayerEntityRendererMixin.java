package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.access.PlayerEntityModelProperties;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.registry.entity.custom.renderer.HeartHandsFeatureRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
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
        this.addFeature(new HeartHandsFeatureRenderer<>(this));
    }

    @Inject(method = "renderArm", at = @At("TAIL"))
    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        if (player instanceof PlayerProperties playerProperties && playerProperties.isHeart()) {
            if (getModel() instanceof PlayerEntityModelProperties modelProperties && modelProperties.getHeartHandsModel().getRoot().visible) {

                ModelPart modelPart = modelProperties.getHeartHandsModel().getRightArm();

                if (arm.equals(getModel().leftArm)) {
                    modelPart = modelProperties.getHeartHandsModel().getLeftArm();
                }

                modelPart.copyTransform(arm);
                matrices.push();
                modelPart.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(HeartHandsFeatureRenderer.TEXTURE)), light, OverlayTexture.DEFAULT_UV);
                matrices.pop();
            }
        }
    }
}