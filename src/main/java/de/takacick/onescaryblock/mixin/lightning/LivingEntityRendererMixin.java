package de.takacick.onescaryblock.mixin.lightning;

import de.takacick.onescaryblock.access.HerobrineLightningProperties;
import de.takacick.onescaryblock.client.utils.LightningRenderer;
import de.takacick.onescaryblock.registry.entity.custom.renderer.HerobrineLightningEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (livingEntity instanceof HerobrineLightningProperties herobrineLightningProperties && herobrineLightningProperties.getHerobrineLightningHelper().isRunning()) {
            float progress = herobrineLightningProperties.getHerobrineLightningHelper().getProgress(g);
            if (progress > 0f) {
                Random random = Random.create();
                for (int x = 0; x < random.nextBetween(1, 5); x++) {
                    matrixStack.push();
                    matrixStack.translate(random.nextGaussian() * 0.1, livingEntity.getHeight() * random.nextDouble(), random.nextGaussian() * 0.1);
                    matrixStack.scale(0.0075f, 0.0075f, 0.0075f);
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Random.create().nextGaussian()));
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float) Random.create().nextGaussian()));
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) Random.create().nextGaussian()));
                    LightningRenderer.renderLightning(matrixStack, vertexConsumerProvider, HerobrineLightningEntityRenderer.LIGHTNING_COLOR_HEX, random.nextLong(), 70f, 2f, Math.max((int) (progress * 2), 1), 15f);
                    matrixStack.pop();
                }
            }
        }
    }
}