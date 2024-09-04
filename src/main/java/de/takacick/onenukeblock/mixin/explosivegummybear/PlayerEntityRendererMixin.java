package de.takacick.onenukeblock.mixin.explosivegummybear;

import de.takacick.onenukeblock.utils.data.AttachmentTypes;
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

    @Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1, shift = At.Shift.AFTER))
    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info) {
        var animationHelper = player.getAttached(AttachmentTypes.EXPLOSIVE_GUMMY_BEAR);
        int overlay = 0;
        if (animationHelper != null) {
            int tick = animationHelper.getTick() - 20;

            if (tick > 0) {
                tick = 30 - tick;
                if (tick / 5 % 2 == 0) {
                    overlay = OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10);
                }
            }
        }

        if (overlay != 0) {
            Identifier identifier = player.getSkinTextures().texture();
            arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(identifier)), light, overlay);
            sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(identifier)), light, overlay);
        }
    }
}
