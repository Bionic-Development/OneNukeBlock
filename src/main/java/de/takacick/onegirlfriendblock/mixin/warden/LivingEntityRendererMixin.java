package de.takacick.onegirlfriendblock.mixin.warden;

import de.takacick.onegirlfriendblock.registry.entity.living.ZukoHumanoidEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
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

    @Inject(method = "getOverlay", at = @At(value = "HEAD"), cancellable = true)
    private static void getOverlay(LivingEntity entity, float whiteOverlayProgress, CallbackInfoReturnable<Integer> info) {
        if (entity instanceof ZukoHumanoidEntity) {
            info.setReturnValue(OverlayTexture.packUv(OverlayTexture.getU(whiteOverlayProgress), OverlayTexture.getV(entity.hurtTime > 0)));
        }
    }
}
