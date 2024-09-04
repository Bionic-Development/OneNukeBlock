package de.takacick.onenukeblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class DiamondHazmatArmorItemModel extends SinglePartItemModel {

    public DiamondHazmatArmorItemModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public static TexturedModelData getHelmetTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = bone.addChild("head", ModelPartBuilder.create().uv(51, 46).cuboid(-1.9419F, -3.25F, -5.3334F, 4.0F, 3.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.501F)), ModelTransform.pivot(0.0F, -0.5F, 0.0F));

        ModelPartData head_r1 = head.addChild("head_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-2.7767F, -3.75F, -4.3585F, 0.0F, 0.3054F, 0.0F));

        ModelPartData head_r2 = head.addChild("head_r2", ModelPartBuilder.create().uv(0, 4).cuboid(-1.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(2.7767F, -3.75F, -4.3585F, 0.0F, -0.3054F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    public static TexturedModelData getChestplateTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 36.0F, 0.0F));

        ModelPartData body = bone.addChild("body", ModelPartBuilder.create().uv(0, 17).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.11F))
                .uv(31, 59).cuboid(-3.0F, 1.5F, 2.0F, 6.0F, 8.0F, 3.0F, new Dilation(0.11F))
                .uv(54, 46).cuboid(-0.5F, -1.7F, 2.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.11F))
                .uv(0, 62).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.26F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData right_arm = bone.addChild("right_arm", ModelPartBuilder.create().uv(0, 51).cuboid(-3.0F, 7.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.4F))
                .uv(25, 29).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F))
                .uv(50, 0).cuboid(-3.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.4F)), ModelTransform.of(-5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        ModelPartData left_arm = bone.addChild("left_arm", ModelPartBuilder.create().uv(50, 18).cuboid(-1.0F, 7.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.4F))
                .uv(50, 9).cuboid(-1.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.4F))
                .uv(42, 29).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F)), ModelTransform.of(5.0F, -22.0F, 0.0F, 0.0F, 0.0F, -0.1309F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    public static TexturedModelData getLeggingsTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData belt = bone.addChild("belt", ModelPartBuilder.create().uv(0, 17).cuboid(-4.0F, -16.0F, -2.0F, 8.0F, 4.0F, 4.0F, new Dilation(0.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right_belt = bone.addChild("right_belt", ModelPartBuilder.create().uv(0, 34).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F)), ModelTransform.of(-1.9F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData left_belt = bone.addChild("left_belt", ModelPartBuilder.create().uv(33, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F)), ModelTransform.of(1.9F, -12.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    public static TexturedModelData getBootsTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.1F, 24.0F, 0.0F));

        ModelPartData right_leg = bone.addChild("right_leg", ModelPartBuilder.create().uv(17, 46).cuboid(-2.1F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.of(-2.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

        ModelPartData left_leg = bone.addChild("left_leg", ModelPartBuilder.create().uv(34, 46).cuboid(-1.9F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.of(1.8F, -12.0F, 0.0F, 0.0F, 0.0F, -0.0436F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
