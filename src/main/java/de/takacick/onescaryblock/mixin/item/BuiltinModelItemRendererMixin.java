package de.takacick.onescaryblock.mixin.item;

import de.takacick.onescaryblock.access.ItemRendererProperties;
import de.takacick.onescaryblock.client.item.model.*;
import de.takacick.onescaryblock.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Optional;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Unique
    private HashMap<Item, ItemModel> onescaryblock$itemModels = new HashMap<>();

    @Inject(method = "reload", at = @At("HEAD"))
    public void render(ResourceManager manager, CallbackInfo info) {
        this.onescaryblock$itemModels.clear();
        this.onescaryblock$itemModels.put(ItemRegistry.SCARY_ONE_BLOCK_ITEM, new ScaryOneBlockItemModel(ScaryOneBlockItemModel.getTexturedModelData().createModel()));
        this.onescaryblock$itemModels.put(ItemRegistry.PHANTOM_BLOCK_ITEM, new PhantomBlockItemModel(PhantomBlockItemModel.getTexturedModelData().createModel()));
        this.onescaryblock$itemModels.put(ItemRegistry.HEROBRINE_LIGHTNING_BOLT, new HerobrineLightningBoltItemModel());
        this.onescaryblock$itemModels.put(ItemRegistry.BLOOD_BORDER_SUIT, new BloodBorderSuitItemModel(BloodBorderSuitItemModel.getTexturedModelData().createModel()));
        this.onescaryblock$itemModels.put(ItemRegistry.ITEM_303, new Item303Model());
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Optional.ofNullable(this.onescaryblock$itemModels.get(stack.getItem())).ifPresent(animatedItemModel -> {
            LivingEntity livingEntity = MinecraftClient.getInstance().getItemRenderer() instanceof ItemRendererProperties itemRendererProperties ? itemRendererProperties.getTempLivingEntity() : null;
            matrices.push();

            if (!animatedItemModel.shouldIgnoreItemTransforms(stack)) {
                matrices.translate(0.5, 0, 0.5);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
                matrices.translate(0, -1.501f, 0);
            }

            long time = MinecraftClient.getInstance().world == null ? 0 : MinecraftClient.getInstance().world.getTime();
            float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();

            animatedItemModel.setAngles(livingEntity, stack, time, tickDelta);

            RenderLayer renderLayer = animatedItemModel.getLayer(animatedItemModel.getTexture(stack));
            animatedItemModel.render(matrices, vertexConsumers.getBuffer(renderLayer), light, overlay, 1f, 1f, 1f, 1f);
            animatedItemModel.renderFeatures(null, mode, stack, matrices, time, tickDelta, vertexConsumers, light, overlay, 1f, 1f, 1f, 1f);

            if (stack.hasGlint()) {
                animatedItemModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getArmorEntityGlint()), light, overlay, 1f, 1f, 1f, 1f);
            }
            matrices.pop();
        });
    }
}