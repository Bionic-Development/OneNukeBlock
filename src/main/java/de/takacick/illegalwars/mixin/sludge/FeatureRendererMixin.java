package de.takacick.illegalwars.mixin.sludge;

import de.takacick.illegalwars.access.LivingProperties;
import de.takacick.illegalwars.client.shaders.IllegalWarsLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderer.class)
public abstract class FeatureRendererMixin {

    @Inject(method = "renderModel", at = @At(value = "HEAD"), cancellable = true)
    private static <T extends LivingEntity> void renderModel(EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float red, float green, float blue, CallbackInfo info) {
        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
        if (entity instanceof LivingProperties livingProperties
                && livingProperties.getSludgeStrength(tickDelta) > 0f) {
            if (entity.getWorld().getRandom().nextDouble() <= 0.7) {
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(IllegalWarsLayers.getEntityTranslucentCull(texture,
                        livingProperties.getSludgeStrength(tickDelta)));
                model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);
                info.cancel();
            }
        }
    }
}
