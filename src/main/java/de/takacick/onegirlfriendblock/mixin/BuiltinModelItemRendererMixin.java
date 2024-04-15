package de.takacick.onegirlfriendblock.mixin;

import de.takacick.onegirlfriendblock.client.model.FrenchFriesModel;
import de.takacick.onegirlfriendblock.client.model.MaidSuitModel;
import de.takacick.onegirlfriendblock.client.renderer.MaidSuitRenderer;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Unique
    private FrenchFriesModel onegirlfriendblock$frenchFries;
    @Unique
    private MaidSuitModel onegirlfriendblock$maidSuit;

    @Inject(method = "reload", at = @At("HEAD"))
    public void render(ResourceManager manager, CallbackInfo info) {
        this.onegirlfriendblock$frenchFries = new FrenchFriesModel(FrenchFriesModel.getTexturedModelData().createModel());
        this.onegirlfriendblock$maidSuit = new MaidSuitModel(MaidSuitModel.getTexturedModelData().createModel());
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (stack.isOf(ItemRegistry.FRENCH_FRIES)) {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.translate(0, -1.501f, 0);

            RenderLayer renderLayer = this.onegirlfriendblock$frenchFries.getLayer(FrenchFriesModel.TEXTURE);
            this.onegirlfriendblock$frenchFries.render(matrices, vertexConsumers.getBuffer(renderLayer), light, overlay, 1f, 1f, 1f, 1f);
        } else if (stack.isOf(ItemRegistry.MAID_SUIT)) {
            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.translate(0, -1.501f, 0);

            RenderLayer renderLayer = this.onegirlfriendblock$maidSuit.getLayer(MaidSuitRenderer.TEXTURE);
            this.onegirlfriendblock$maidSuit.render(matrices, vertexConsumers.getBuffer(renderLayer), light, overlay, 1f, 1f, 1f, 1f);
        }
    }
}