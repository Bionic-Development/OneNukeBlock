package de.takacick.onenukeblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class SkylandTntBlockItemModel extends SinglePartItemModel {

    public SkylandTntBlockItemModel(ModelPart root) {
        super(root, RenderLayer::getEntityTranslucent);
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData tnt = bone.addChild("tnt", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData block = bone.addChild("block", ModelPartBuilder.create().uv(49, 11).cuboid(-1.0F, 1.0F, -3.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(2.0F, -7.0F, 2.0F, 2.0F, 8.0F, 2.0F, new Dilation(0.0F))
                .uv(49, 0).cuboid(6.0001F, -7.0F, 0.0F, 2.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(19, 47).cuboid(-2.0001F, -7.0F, 0.0F, 2.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(35, 38).cuboid(0.0F, -7.0F, 0.0F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(51, 52).cuboid(0.0F, -5.0F, -2.0001F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 53).cuboid(2.0F, -7.0001F, -2.0001F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 11).cuboid(0.0F, -7.0001F, 6.0001F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(36, 49).cuboid(0.0F, -5.0F, 6.0001F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 44).cuboid(0.0F, -9.0001F, 0.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.0F))
                .uv(33, 33).cuboid(-1.0F, 1.0F, 7.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 33).cuboid(-3.0F, 1.0F, -1.0F, 12.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.0F, -11.0F, -3.0F));

        ModelPartData block_r1 = block.addChild("block_r1", ModelPartBuilder.create().uv(0, 33).cuboid(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.2253F, 0.0F));

        ModelPartData block_r2 = block.addChild("block_r2", ModelPartBuilder.create().uv(9, 5).cuboid(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 0.0F, 6.0F, 0.0F, -0.6545F, 0.0F));

        ModelPartData block_r3 = block.addChild("block_r3", ModelPartBuilder.create().uv(9, 0).cuboid(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 0.0F, 6.0F, 0.0F, -2.2253F, 0.0F));

        ModelPartData block_r4 = block.addChild("block_r4", ModelPartBuilder.create().uv(3, 36).cuboid(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.6545F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}
