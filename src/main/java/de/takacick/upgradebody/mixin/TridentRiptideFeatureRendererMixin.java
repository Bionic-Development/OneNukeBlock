package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.access.PlayerProperties;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentRiptideFeatureRenderer.class)
public abstract class TridentRiptideFeatureRendererMixin<T extends LivingEntity>
        extends FeatureRenderer<T, PlayerEntityModel<T>> {

    @Shadow
    @Final
    private ModelPart aura;

    @Shadow
    @Final
    public static Identifier TEXTURE;

    @Shadow
    public abstract void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l);

    public TridentRiptideFeatureRendererMixin(FeatureRendererContext<T, PlayerEntityModel<T>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "TAIL"))
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if (!livingEntity.isUsingRiptide() || !(livingEntity instanceof PlayerProperties playerProperties && playerProperties.isKillerDrilling())) {
            return;
        }

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        for (int m = 0; m < 3; ++m) {
            matrixStack.push();
            float n = j * (float) (-(45 + m * 5));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(n));
            float o = 0.75f * (float) m;
            matrixStack.scale(o, o, o);
            matrixStack.translate(0.0, -0.2f + 0.6f * (float) m, 0.0);
            this.aura.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0.29f, 0.93f, 0.85f, 1.0f);
            matrixStack.pop();
        }
    }
}
