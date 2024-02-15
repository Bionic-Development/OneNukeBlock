package de.takacick.secretgirlbase.mixin.bubblegum;

import de.takacick.secretgirlbase.access.LivingProperties;
import de.takacick.secretgirlbase.client.shaders.SecretGirlBaseLayers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "getRenderLayer", at = @At(value = "RETURN"), cancellable = true)
    private void getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline, CallbackInfoReturnable<@Nullable RenderLayer> info) {
        if (info.getReturnValue() != null) {
            float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
            if (entity instanceof LivingProperties livingProperties
                    && livingProperties.getBubbleGumStrength(tickDelta) > 0f) {
                info.setReturnValue(SecretGirlBaseLayers.getBubbleGum(getTexture(entity),
                        livingProperties.getBubbleGumStrength(tickDelta)));
            }
        }
    }
}
