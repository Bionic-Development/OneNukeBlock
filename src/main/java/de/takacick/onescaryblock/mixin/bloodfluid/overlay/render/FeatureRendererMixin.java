package de.takacick.onescaryblock.mixin.bloodfluid.overlay.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onescaryblock.access.BloodProperties;
import de.takacick.onescaryblock.client.shader.OneScaryBlockLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FeatureRenderer.class)
public abstract class FeatureRendererMixin<T extends Entity, M extends EntityModel<T>> {

    @ModifyExpressionValue(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"))
    private static <T extends LivingEntity> VertexConsumer renderModel(VertexConsumer original, @Local(argsOnly = true) Identifier texture, @Local(argsOnly = true) T entity) {
        if (entity instanceof BloodProperties bloodProperties) {
            float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
            float progress = bloodProperties.getBloodStrength(tickDelta);
            if (progress > 0f) {
                VertexConsumer vertexConsumer = OneScaryBlockLayers.getTransformVertexConsumer().getBuffer(OneScaryBlockLayers.getBloodEntity(texture, Math.min(progress, 1f)));
                return VertexConsumers.union(original, vertexConsumer);
            }
        }

        return original;
    }
}

