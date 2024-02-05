package de.takacick.secretcraftbase.registry.entity.custom.model;

import de.takacick.secretcraftbase.registry.entity.custom.IronGolemFarmEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class IronGolemFarmEntityModel extends EntityModel<IronGolemFarmEntity> {

    private final ModelPart root;

    public IronGolemFarmEntityModel(ModelPart modelPart) {
        this.root = modelPart;
    }

    @Override
    public void setAngles(IronGolemFarmEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -12.0F, -6.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F))
                .uv(24, 0).cuboid(-1.0F, -5.0F, -8.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 26.0F, 2.0F));

        ModelPartData pipe = modelPartData.addChild("pipe", ModelPartBuilder.create().uv(1, 99).cuboid(-3.0F, -13.0F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F))
                .uv(6, 56).cuboid(-3.0F, -14.0F, -3.0F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(10, 98).cuboid(-4.0F, -16.0F, 3.0F, 8.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 87).cuboid(-4.0F, -16.0F, -4.0F, 8.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(10, 100).cuboid(3.0F, -16.0F, -3.0F, 1.0F, 3.0F, 6.0F, new Dilation(0.0F))
                .uv(24, 54).cuboid(-4.0F, -16.0F, -3.0F, 1.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}