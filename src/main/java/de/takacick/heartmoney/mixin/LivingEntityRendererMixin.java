package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.access.LivingProperties;
import de.takacick.heartmoney.client.CustomLayers;
import de.takacick.heartmoney.registry.ItemRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    @Shadow
    public abstract M getModel();

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "getRenderLayer", at = @At(value = "HEAD"), cancellable = true)
    private void getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline, CallbackInfoReturnable<@Nullable RenderLayer> cir) {
        if (showBody) {
            if (getModel() instanceof BipedEntityModel && entity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.MAID_SUIT_ARMOR)) {
                boolean slim = false;
                if (getModel() instanceof PlayerEntityModelAccessor playerEntityModel) {
                    slim = playerEntityModel.getThinArms();
                }

                cir.setReturnValue(CustomLayers.MAID_CUTOUT.apply(getTexture(entity), CustomLayers.getOverlayTexture(slim)));
            }
        }
    }

    @ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void render(Args args, T livingEntity, float f, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (args.size() >= 6 && livingEntity instanceof LivingProperties livingProperties) {

            float progress = livingProperties.getMaidExplosionProgress(delta);
            if (progress <= 0) {
                return;
            }

            Vec3f startColor = new Vec3f(args.<Float>get(4), args.<Float>get(5), args.<Float>get(6));
            float[] endColor = {1f, 0.5f, 0.5f};

            float r = startColor.getX() * (1 - progress) + endColor[0] * progress;
            float g = startColor.getY() * (1 - progress) + endColor[1] * progress;
            float b = startColor.getZ() * (1 - progress) + endColor[2] * progress;

            args.set(4, r);
            args.set(5, g);
            args.set(6, b);
        }
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "HEAD"))
    private void scale(T livingEntity, float f, float delta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity instanceof LivingProperties livingProperties) {
            float g = livingProperties.getMaidExplosionProgress(delta);
            if (g > 0) {
                g = MathHelper.clamp(g, 0.0f, 1.0f);
                g *= g;
                g *= g;
                float j = (1.0f + g * 0.4f);
                matrixStack.scale(j, j, j);
            }
        }
    }
}