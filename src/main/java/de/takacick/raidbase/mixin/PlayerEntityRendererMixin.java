package de.takacick.raidbase.mixin;

import de.takacick.raidbase.access.PlayerProperties;
import de.takacick.raidbase.client.renderer.PieFeatureRenderer;
import de.takacick.raidbase.client.renderer.SlimeSuitFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new PieFeatureRenderer<>(this));
        this.addFeature(new SlimeSuitFeatureRenderer<>(this, ctx.getModelLoader()));
    }

    @Inject(method = "scale(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At("TAIL"))
    private void scale(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, CallbackInfo info) {
        if (abstractClientPlayerEntity instanceof PlayerProperties playerProperties) {
            float h = 1;

            float i = MathHelper.lerp(f, playerProperties.getSlimeSuitLastStretch(),
                    playerProperties.getSlimeSuitStretch()) / (h * 0.5f + 1.0f);
            float j = 1.0f / (i + 1.0f);
            matrixStack.scale(j * h, 1.0f / j * h, j * h);
        }
    }

    @Inject(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("TAIL"))
    private void setupTransforms(AbstractClientPlayerEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo info) {
        if (abstractClientPlayerEntity instanceof PlayerProperties playerProperties && playerProperties.hasSlimeSuit()) {
            matrixStack.translate(0, 0.25, 0);
        }
    }
}
