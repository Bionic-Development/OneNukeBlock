package de.takacick.secretcraftbase.registry.entity.custom.model;

import de.takacick.secretcraftbase.registry.entity.custom.ArmoryRoomEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class ArmoryRoomEntityModel extends EntityModel<ArmoryRoomEntity> {

    private final ModelPart root;

    public ArmoryRoomEntityModel(ModelPart modelPart) {
        this.root = modelPart;
    }

    @Override
    public void setAngles(ArmoryRoomEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(24, 16).cuboid(-8.0F, 12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F))
                .uv(24, 16).mirrored().cuboid(4.0F, 12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(1.0F)).mirrored(false)
                .uv(0, 16).cuboid(-4.0F, 12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(1.01F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData pipe = modelPartData.addChild("pipe", ModelPartBuilder.create().uv(4, 5).cuboid(-3.0F, -15.0F, -3.0F, 6.0F, 5.0F, 6.0F, new Dilation(0.0F))
                .uv(7, 0).cuboid(-4.0F, -18.0F, 3.0F, 8.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 0).cuboid(-4.0F, -18.0F, -4.0F, 8.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(7, 0).cuboid(3.0F, -18.0F, -3.0F, 1.0F, 3.0F, 6.0F, new Dilation(0.0F))
                .uv(7, 0).cuboid(-4.0F, -18.0F, -3.0F, 1.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }
}