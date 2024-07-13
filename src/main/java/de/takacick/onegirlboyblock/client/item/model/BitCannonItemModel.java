package de.takacick.onegirlboyblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BitCannonItemModel extends SinglePartItemModel {

    private final ModelPart bitCannon;
    private final ModelPart cannon;

    public BitCannonItemModel(ModelPart root) {
        super(root);
        this.bitCannon = root.getChild("8_bit_cannon");
        this.cannon = this.bitCannon.getChild("cannon");
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {

    }

    public void rotateCannon(MatrixStack matrixStack) {
        this.getPart().rotate(matrixStack);
        this.bitCannon.rotate(matrixStack);
        this.cannon.rotate(matrixStack);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bit_cannon = modelPartData.addChild("8_bit_cannon", ModelPartBuilder.create().uv(0, 0).cuboid(-9.5F, -10.5F, 1.5F, 3.0F, 3.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-9.5F, -10.5F, 1.5F, 3.0F, 3.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 22).cuboid(-10.0F, -11.0F, 0.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(19, 17).cuboid(-10.5F, -11.5F, 9.5F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(-10.0F, -11.0F, 10.5F, 4.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-12.75F, -10.0F, 14.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(15, 0).cuboid(-13.0303F, -10.5303F, 11.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(14, 12).cuboid(-12.75F, -10.0F, 15.5F, 5.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 31.0F, -11.0F));

        ModelPartData cannon = bit_cannon.addChild("cannon", ModelPartBuilder.create(), ModelTransform.of(-8.0F, -9.0F, 0.5F, -1.5708F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    public static TexturedModelData getThirdPersonTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData bit_cannon = modelPartData.addChild("8_bit_cannon", ModelPartBuilder.create().uv(0, 0).cuboid(-13.5F, -14.5F, -15.5F, 3.0F, 3.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-13.5F, -14.5F, -15.5F, 3.0F, 3.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 22).cuboid(-14.0F, -15.0F, -16.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(19, 17).cuboid(-14.5F, -15.5F, -7.5F, 5.0F, 5.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(-14.0F, -15.0F, -6.5F, 4.0F, 4.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-16.75F, -14.0F, -2.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(15, 0).cuboid(-17.0303F, -14.5303F, -5.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
                .uv(14, 12).cuboid(-16.75F, -14.0F, -1.5F, 5.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(12.0F, 12.0F, 0.0F));

        ModelPartData cannon = bit_cannon.addChild("cannon", ModelPartBuilder.create(), ModelTransform.of(-12.0F, -13.0F, -16.5F, -1.5708F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }
}
