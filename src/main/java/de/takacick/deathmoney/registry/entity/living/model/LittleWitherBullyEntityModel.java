package de.takacick.deathmoney.registry.entity.living.model;

import de.takacick.deathmoney.registry.entity.living.LittleWitherBullyEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class LittleWitherBullyEntityModel extends SinglePartEntityModel<LittleWitherBullyEntity> implements ModelWithArms {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public LittleWitherBullyEntityModel(ModelPart root) {
        this.root = root.getChild(EntityModelPartNames.ROOT);
        this.head = this.root.getChild(EntityModelPartNames.HEAD);
        this.body = this.root.getChild(EntityModelPartNames.BODY);
        this.rightWing = this.body.getChild(EntityModelPartNames.RIGHT_WING);
        this.leftWing = this.body.getChild(EntityModelPartNames.LEFT_WING);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -6.01F, -2.5F, 6.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(26, 28).cuboid(-1.5F, 1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(17, 14).cuboid(-4.5F, 0.0F, -1.0F, 9.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 28).cuboid(-2.5F, 2.0F, -1.0F, 5.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(15, 28).cuboid(-1.5F, 2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData rightItem = body.addChild("rightItem", ModelPartBuilder.create(), ModelTransform.of(0.0F, 5.0F, -2.0F, -1.3963F, 0.0F, 0.0F));

        ModelPartData left_wing = body.addChild("left_wing", ModelPartBuilder.create().uv(23, 0).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 1.0F, 1.0F));

        ModelPartData right_wing = body.addChild("right_wing", ModelPartBuilder.create().uv(0, 14).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 5.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 1.0F, 1.0F));
        return TexturedModelData.of(modelData, 48, 48);
    }

    @Override
    public void setAngles(LittleWitherBullyEntity littleWitherBullyEntity, float f, float g, float h, float i, float j) {
        float r;
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.head.pitch = j * ((float) Math.PI / 180);
        this.head.yaw = i * ((float) Math.PI / 180);
        float k = h * 20.0f * ((float) Math.PI / 180) + g;
        float l = MathHelper.cos(k) * (float) Math.PI * 0.15f;
        float n = h * 9.0f * ((float) Math.PI / 180);
        float o = Math.min(g / 0.3f, 1.0f);
        float p = 1.0f - o;
        this.rightWing.pitch = 0.43633232f;
        this.rightWing.yaw = -0.61086524f + l;
        this.leftWing.pitch = 0.43633232f;
        this.leftWing.yaw = 0.61086524f - l;
        this.body.pitch = o * 0.6981317f;
        this.root.pivotY += (float) Math.cos(n) * 0.25f * p;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {

    }
}