package de.takacick.onescaryblock.client.item.model;

import de.takacick.onescaryblock.client.entity.feature.BloodBorderSuitFeatureRenderer;
import de.takacick.onescaryblock.registry.EntityRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class HerobrineLightningBoltItemModel extends ItemModel {

    private final ModelPart root;

    public HerobrineLightningBoltItemModel() {
        super(RenderLayer::getEntityTranslucentCull);

        this.root = getTexturedModelData().createModel();
    }

    @Override
    public void setAngles(LivingEntity livingEntity, ItemStack itemStack, long time, float tickDelta) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

    }

    @Override
    public void renderFeatures(@Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, ItemStack itemStack, MatrixStack matrices, long time, float tickDelta, VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.translate(0.5f, 0f, 0.5f);
        if (modelTransformationMode.equals(ModelTransformationMode.GUI)) {
            matrices.translate(0f, -0.25f, 0f);
            matrices.scale(0.02f, 0.02f, 0.02f);
        } else {
            matrices.scale(0.025f, 0.025f, 0.025f);
        }

        MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityRegistry.HEROBRINE_LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertices, light);

        matrices.pop();

        super.renderFeatures(livingEntity, modelTransformationMode, itemStack, matrices, time, tickDelta, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public boolean shouldIgnoreItemTransforms(ItemStack itemStack) {
        return true;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public Identifier getTexture(ItemStack itemStack) {
        return BloodBorderSuitFeatureRenderer.TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
