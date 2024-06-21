package de.takacick.onescaryblock.mixin.bloodfluid.overlay.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import de.takacick.onescaryblock.access.BloodProperties;
import de.takacick.onescaryblock.client.shader.OneScaryBlockLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @ModifyExpressionValue(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
    private VertexConsumer render(VertexConsumer original, @Local(argsOnly = true) LocalRef<VertexConsumerProvider> localRef, @Local(argsOnly = true) T livingEntity, @Local(argsOnly = true, ordinal = 1) float g) {
        if (livingEntity instanceof BloodProperties bloodProperties) {
            float progress = bloodProperties.getBloodStrength(g);
            if (progress > 0f) {

                VertexConsumer vertexConsumer = OneScaryBlockLayers.getTransformVertexConsumer()
                        .getBuffer(OneScaryBlockLayers.getBloodEntity(getTexture(livingEntity), Math.min(progress, 1f)));
                return VertexConsumers.union(original, vertexConsumer);
            }
        }

        return original;
    }
}
