package de.takacick.illegalwars.registry.block.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class BaseWarsMoneyWheelModel extends Model {

    private final ModelPart podium;
    private final ModelPart wheel;
    private final ModelPart moneyWheel;

    public BaseWarsMoneyWheelModel() {
        super(RenderLayer::getEntityCutoutNoCull);
        this.podium = getTexturedModelData().createModel();
        this.wheel = getWheelTexturedModelData().createModel();
        this.moneyWheel = this.wheel.getChild("money_wheel").getChild("wheel");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData money_wheel = modelPartData.addChild("money_wheel", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 19.0F, -1.0F));

        ModelPartData podium = money_wheel.addChild("podium", ModelPartBuilder.create().uv(17, 33).cuboid(-1.0F, -17.0F, -2.0F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 19).cuboid(-1.0F, -14.0F, -2.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.0F))
                .uv(4, 17).cuboid(-2.0F, 1.0F, -1.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(4, 9).cuboid(-3.0F, 4.0F, -1.5F, 6.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(3, 1).cuboid(-3.0F, 0.0F, -1.5F, 6.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(24, 24).cuboid(-1.0F, -16.0F, 1.0F, 2.0F, 16.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 58).cuboid(-1.0F, -17.0F, -1.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData podium_back = podium.addChild("podium_back", ModelPartBuilder.create().uv(0, 42).cuboid(-11.0F, -11.0F, 2.0F, 7.0F, 6.0F, 1.0F, new Dilation(0.0F))
                .uv(34, 49).cuboid(-6.0F, -7.0F, 2.0F, 6.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(49, 49).cuboid(-6.0F, -16.0F, 2.0F, 6.0F, 7.0F, 1.0F, new Dilation(0.0F))
                .uv(17, 44).cuboid(-2.0F, -11.0F, 2.0F, 7.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, 0.0F, 0.0F));

        ModelPartData podium_back_r1 = podium_back.addChild("podium_back_r1", ModelPartBuilder.create().uv(35, 40).cuboid(0.0F, -7.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

        ModelPartData podium_back_r2 = podium_back.addChild("podium_back_r2", ModelPartBuilder.create().uv(35, 31).cuboid(0.0F, 0.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 1.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData podium_back_r3 = podium_back.addChild("podium_back_r3", ModelPartBuilder.create().uv(35, 22).cuboid(-7.0F, 0.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -16.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

        ModelPartData podium_back_r4 = podium_back.addChild("podium_back_r4", ModelPartBuilder.create().uv(0, 33).cuboid(-7.0F, -7.0F, 1.0F, 7.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData wheel = money_wheel.addChild("wheel", ModelPartBuilder.create().uv(61, 0).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(15, 60).cuboid(-8.0F, -3.0F, -1.0F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(45, 11).cuboid(-3.0F, -8.0F, -1.0F, 6.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(56, 58).cuboid(6.0F, -3.0F, -1.0F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(45, 3).cuboid(-3.0F, 6.0F, -1.0F, 6.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(22, 16).cuboid(-7.5F, -0.5F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 11).cuboid(6.5F, -0.5F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-0.5F, 6.5F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(22, 19).cuboid(-0.5F, -5.0F, -2.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        ModelPartData wheel_r1 = wheel.addChild("wheel_r1", ModelPartBuilder.create().uv(0, 3).cuboid(3.0F, -1.5F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(27, 0).cuboid(0.0F, -2.0F, -1.0F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 8.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        ModelPartData wheel_r2 = wheel.addChild("wheel_r2", ModelPartBuilder.create().uv(0, 8).cuboid(-4.0F, -1.5F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 27).cuboid(-7.0F, -2.0F, -1.0F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData wheel_r3 = wheel.addChild("wheel_r3", ModelPartBuilder.create().uv(0, 16).cuboid(3.0F, 0.5F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(27, 8).cuboid(0.0F, 0.0F, -1.0F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData wheel_r4 = wheel.addChild("wheel_r4", ModelPartBuilder.create().uv(18, 27).cuboid(-4.0F, 0.5F, -2.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(27, 16).cuboid(-7.0F, 0.0F, -1.0F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.7854F));
        return TexturedModelData.of(modelData, 80, 80);
    }

    public static TexturedModelData getWheelTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData money_wheel = modelPartData.addChild("money_wheel", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 19.0F, 0.0F));

        ModelPartData wheel = money_wheel.addChild("wheel", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -6.5F, 0.0F, 13.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, -1.0F));
        return TexturedModelData.of(modelData, 26, 13);
    }

    public void setRotation(float roll) {
        this.moneyWheel.resetTransform();
        this.moneyWheel.roll = roll * ((float) Math.PI / 180);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.podium.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    public void renderWheel(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.wheel.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}