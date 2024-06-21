package de.takacick.onescaryblock.client.item.model;

import de.takacick.onescaryblock.access.BloodBorderProperties;
import de.takacick.onescaryblock.client.entity.feature.BloodBorderSuitFeatureRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class BloodBorderSuitItemModel extends ItemModel {

    private final ModelPart root;

    public BloodBorderSuitItemModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucentCull);

        this.root = root;
    }

    @Override
    public void setAngles(LivingEntity livingEntity, ItemStack itemStack, long time, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

    }

    @Override
    public void renderFeatures(@Nullable LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, ItemStack itemStack, MatrixStack matrices, long time, float tickDelta, VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        float delta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
        float progress = 0f;
        if (livingEntity instanceof BloodBorderProperties bloodBorderProperties && livingEntity.getEquippedStack(EquipmentSlot.CHEST).equals(itemStack)) {
            progress = bloodBorderProperties.getBloodBorderSuitHelper().getProgress(delta);
        }

        BloodBorderSuitFeatureRenderer.renderModel(matrices, this.root, vertices, time + delta, light, progress);

        super.renderFeatures(livingEntity, modelTransformationMode, itemStack, matrices, time, tickDelta, vertices, light, overlay, red, green, blue, alpha);
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

        ModelPartData body = bone.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 16).cuboid(-4.0F, 12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData right_arm = bone.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, 10.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, -22.0F, 0.0F));

        ModelPartData left_arm = bone.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, 10.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, -22.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
