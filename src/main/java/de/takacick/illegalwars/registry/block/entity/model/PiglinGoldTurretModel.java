package de.takacick.illegalwars.registry.block.entity.model;

import de.takacick.illegalwars.registry.block.entity.PiglinGoldTurretBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class PiglinGoldTurretModel extends Model {

    private final ModelPart base;
    private final ModelPart turret;

    public PiglinGoldTurretModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);

        this.base = root.getChild("base");
        this.turret = this.base.getChild("turret");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 0).cuboid(-16.0F, -2.0F, 0.0F, 16.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 24.0F, -8.0F));

        ModelPartData turret = base.addChild("turret", ModelPartBuilder.create().uv(0, 54).cuboid(-5.0F, -13.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(53, 50).cuboid(2.0F, -13.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(83, -4).cuboid(-8.0313F, -14.5F, -2.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(83, -4).mirrored().cuboid(8.0313F, -14.5F, -2.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)).mirrored(false)
                .uv(49, 0).cuboid(5.0F, -16.0F, -2.5F, 3.0F, 8.0F, 5.0F, new Dilation(0.0F))
                .uv(43, 19).cuboid(-8.0F, -16.0F, -2.5F, 3.0F, 8.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-7.5F, -8.0F, -2.0F, 2.0F, 8.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 19).cuboid(5.5F, -8.0F, -2.0F, 2.0F, 8.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 19).cuboid(-7.0F, -3.0F, -7.0F, 14.0F, 3.0F, 14.0F, new Dilation(0.0F)), ModelTransform.pivot(-8.0F, -2.0F, 8.0F));

        ModelPartData barrel = turret.addChild("barrel", ModelPartBuilder.create().uv(33, 50).cuboid(-2.0F, -4.0F, 3.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(23, 37).cuboid(-2.0F, 2.0F, 3.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(20, 54).cuboid(-4.0F, -2.0F, 3.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(11, 54).cuboid(2.0F, -2.0F, 3.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(33, 37).cuboid(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 37).cuboid(-3.0F, -3.0F, -13.0F, 6.0F, 6.0F, 10.0F, new Dilation(0.0F))
                .uv(46, 50).cuboid(-3.0F, -3.0F, -15.0F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 37).cuboid(2.0F, -3.0F, -15.0F, 1.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(50, 41).cuboid(-2.0F, -3.0F, -15.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(50, 37).cuboid(-2.0F, 2.0F, -15.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    public void setAngles(PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity, float yaw) {
        this.turret.resetTransform();
        this.turret.yaw = yaw * ((float) Math.PI / 180);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}