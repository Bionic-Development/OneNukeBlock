package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.access.PlayerProperties;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    @Shadow
    public abstract M getModel();

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "HEAD"))
    private void scale(T livingEntity, float f, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity instanceof PlayerProperties playerProperties) {
            int ticks = playerProperties.getCakeExplosionTicks();
            if ((float) ticks - delta + 1.0f < 10.0f && ticks > 0) {
                float h = 1.0f - ((float) ticks - delta + 1.0f) / 10.0f;
                h = MathHelper.clamp(h, 0.0f, 1.0f);
                h *= h;
                h *= h;
                float k = 1.0f + h * 0.3f;
                matrixStack.scale(k, k, k);
            }
        }
    }

    @Inject(method = "getOverlay", at = @At(value = "HEAD"), cancellable = true)
    private static void getOverlay(LivingEntity livingEntity, float whiteOverlayProgress, CallbackInfoReturnable<Integer> info) {
        if (livingEntity instanceof PlayerProperties playerProperties) {
            int ticks = playerProperties.getCakeExplosionTicks();

            if (ticks > 0) {
                info.setReturnValue(ticks / 5 % 2 == 0 ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : OverlayTexture.DEFAULT_UV);
            }
        }
    }
}