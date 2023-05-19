package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.access.PlayerProperties;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentRiptideFeatureRenderer.class)
public abstract class TridentRiptideRenderFeatureMixin<T extends LivingEntity>
        extends FeatureRenderer<T, PlayerEntityModel<T>> {

    @Shadow
    @Final
    private ModelPart aura;

    @Shadow
    @Final
    public static Identifier TEXTURE;

    public TridentRiptideRenderFeatureMixin(FeatureRendererContext<T, PlayerEntityModel<T>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo info) {
        if (livingEntity instanceof PlayerProperties playerProperties && playerProperties.hasBloodRiptide()) {
            Vec3f color = new Vec3f(Vec3d.unpackRgb(0x820A0A));

            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
            for (int m = 0; m < 3; ++m) {
                matrixStack.push();
                float n = j * (float) (-(45 + m * 5));
                matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(n));
                float o = 0.75f * (float) m;
                matrixStack.scale(o, o, o);
                matrixStack.translate(0.0, -0.2f + 0.6f * (float) m, 0.0);
                this.aura.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, color.getX(), color.getY(), color.getZ(), 1f);
                matrixStack.pop();
            }
            info.cancel();
        }
    }
}
