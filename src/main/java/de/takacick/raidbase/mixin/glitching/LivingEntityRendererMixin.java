package de.takacick.raidbase.mixin.glitching;

import de.takacick.raidbase.access.LivingProperties;
import de.takacick.raidbase.client.shaders.RaidBaseLayers;
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
        if (entity instanceof LivingProperties livingProperties
                && livingProperties.isGlitchy() && info.getReturnValue() != null) {
            if (entity.getWorld().getRandom().nextDouble() <= 0.7) {
                info.setReturnValue(RaidBaseLayers.getEntityTranslucentCull(getTexture(entity),
                        livingProperties.getGlitchSytrength(MinecraftClient.getInstance().getTickDelta())));
            }
        }
    }
}