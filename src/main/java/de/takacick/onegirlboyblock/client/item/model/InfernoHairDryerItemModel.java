package de.takacick.onegirlboyblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class InfernoHairDryerItemModel extends SinglePartItemModel {

    public InfernoHairDryerItemModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData inferno_hair_dryer = modelPartData.addChild("inferno_hair_dryer", ModelPartBuilder.create().uv(19, 0).cuboid(-9.0F, -9.0F, 7.0F, 2.0F, 7.0F, 3.0F, new Dilation(0.0F))
                .uv(26, 12).cuboid(-8.0F, -2.0F, 9.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 11).cuboid(-9.5F, -12.5F, 1.5F, 3.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(2, 24).cuboid(-9.5F, -12.5F, 8.5F, 3.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 26.0F, -8.5F));

        ModelPartData inferno_hair_dryer_r1 = inferno_hair_dryer.addChild("inferno_hair_dryer_r1", ModelPartBuilder.create().uv(14, 17).cuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -10.0F, 8.5F, 0.7854F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    public static TexturedModelData getThirdPersonTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData inferno_hair_dryer = modelPartData.addChild("inferno_hair_dryer", ModelPartBuilder.create().uv(19, 0).cuboid(-1.0F, -16.0F, -7.0F, 2.0F, 7.0F, 3.0F, new Dilation(0.0F))
                .uv(26, 12).cuboid(0.0F, -9.0F, -5.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 11).cuboid(-1.5F, -19.5F, -12.5F, 3.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(2, 24).cuboid(-1.5F, -19.5F, -5.5F, 3.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 1.0F));

        ModelPartData inferno_hair_dryer_r1 = inferno_hair_dryer.addChild("inferno_hair_dryer_r1", ModelPartBuilder.create().uv(14, 17).cuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-2.0F, -2.5F, -2.5F, 4.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.0F, -5.5F, 0.7854F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }
}
