package de.takacick.stealbodyparts.mixin;

import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.registry.entity.custom.renderer.CarvedHeartFeatureRenderer;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
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

    @Inject(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("HEAD"), cancellable = true)
    private void setupTransforms(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo info) {
        if (abstractClientPlayerEntity instanceof PlayerProperties playerProperties) {
            if (!abstractClientPlayerEntity.getPose().equals(EntityPose.SLEEPING)
                    && !abstractClientPlayerEntity.getPose().equals(EntityPose.SWIMMING)
                    && !abstractClientPlayerEntity.getPose().equals(EntityPose.SPIN_ATTACK)) {
                if (!playerProperties.hasBodyPart(BodyPart.RIGHT_LEG.getIndex())
                        && !playerProperties.hasBodyPart(BodyPart.LEFT_LEG.getIndex())) {
                    matrixStack.translate(0, -0.73, 0);
                }
            }
        }
    }
}