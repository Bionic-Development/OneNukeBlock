package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.client.model.TerraDrillModel;
import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.elementalblock.registry.entity.projectile.model.TsunamicTridentEntityModel;
import de.takacick.elementalblock.registry.entity.projectile.renderer.TsunamicTridentEntityRenderer;
import de.takacick.elementalblock.registry.item.TerraDrill;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Shadow
    @Final
    private EntityModelLoader entityModelLoader;

    @Unique
    private TsunamicTridentEntityModel elementalblock$modelTrident;
    @Unique
    private TerraDrillModel elementalblock$terraDrill;

    @Inject(method = "reload", at = @At("TAIL"))
    public void render(ResourceManager manager, CallbackInfo info) {
        this.elementalblock$modelTrident = new TsunamicTridentEntityModel(this.entityModelLoader.getModelPart(EntityModelLayers.TRIDENT));
        this.elementalblock$terraDrill = new TerraDrillModel();
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Item item = stack.getItem();
        if (item.equals(ItemRegistry.TSUNAMIC_TRIDENT)) {
            matrices.push();
            matrices.scale(1.0f, -1.0f, -1.0f);
            VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.elementalblock$modelTrident.getLayer(TsunamicTridentEntityRenderer.TEXTURE), false, stack.hasGlint());
            this.elementalblock$modelTrident.render(matrices, vertexConsumer2, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        } else if (item.equals(ItemRegistry.TERRA_DRILL)) {
            matrices.push();
            if (!mode.equals(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
                matrices.scale(1.0f, -1.0f, -1.0f);
            }

            this.elementalblock$terraDrill.setRotation(TerraDrill.getRotation(stack, MinecraftClient.getInstance().getTickDelta()));
            VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.elementalblock$terraDrill.getLayer(TerraDrillModel.TEXTURE), false, stack.hasGlint());
            this.elementalblock$terraDrill.render(matrices, vertexConsumer2, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
            matrices.pop();
        }
    }
}

