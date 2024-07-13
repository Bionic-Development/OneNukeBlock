package de.takacick.onegirlboyblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class FootballGearItemModel extends SinglePartItemModel {

    public FootballGearItemModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -14.0F, 4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 0).cuboid(-12.0F, -14.0F, 4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(8.0F, 20.0F, -8.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(0.0F, -8.0F, -4.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 16).cuboid(8.001F, -8.0F, -4.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 16).mirrored().cuboid(-4.001F, -8.0F, -4.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-4.0F, 21.0F, 2.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
