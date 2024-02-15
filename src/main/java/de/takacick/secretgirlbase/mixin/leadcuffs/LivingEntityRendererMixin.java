package de.takacick.secretgirlbase.mixin.leadcuffs;

import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    @Shadow
    protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "setupTransforms", at = @At("HEAD"), cancellable = true)
    private void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo info) {
        if (entity.getVehicle() instanceof FireworkTimeBombEntity fireworkTimeBombEntity) {
            if (bodyYaw != fireworkTimeBombEntity.getRiderDirection().asRotation()) {
                setupTransforms(entity, matrices, animationProgress, fireworkTimeBombEntity.getRiderDirection().asRotation(), tickDelta);
                info.cancel();
            }
        }
    }
}