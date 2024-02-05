package de.takacick.secretcraftbase.registry.entity.custom.model;

import de.takacick.secretcraftbase.client.shaders.SecretCraftBaseLayers;
import de.takacick.secretcraftbase.registry.entity.custom.XPFarmEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector3f;

public class XPFarmEntityModel extends EntityModel<XPFarmEntity> {

    private final ModelPart root;
    private final ModelPart block;

    public XPFarmEntityModel(ModelPart modelPart) {
        super(SecretCraftBaseLayers::getXP);
        this.root = modelPart;
        this.block = this.root.getChild("base").getChild("block");
    }

    @Override
    public void setAngles(XPFarmEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        this.block.resetTransform();
        this.block.rotate(new Vector3f(-animationProgress * 10.0f * ((float) Math.PI / 180), animationProgress * 10.0f * ((float) Math.PI / 180), -animationProgress * 10.0f * ((float) Math.PI / 180)));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -9.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData block = base.addChild("block", ModelPartBuilder.create().uv(44, 4).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(1.5F)), ModelTransform.pivot(0.0F, -5.0F, 0.0F));

        ModelPartData pipe = modelPartData.addChild("pipe", ModelPartBuilder.create().uv(41, 13).cuboid(-2.0F, -8.0F, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 21).cuboid(-2.0F, -9.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(15, 27).cuboid(-2.0F, -11.0F, 2.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(15, 27).cuboid(-2.0F, -11.0F, -3.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(2.0F, -11.0F, -3.0F, 1.0F, 3.0F, 6.0F, new Dilation(0.0F))
                .uv(18, 17).cuboid(-3.0F, -11.0F, -3.0F, 1.0F, 3.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }
}