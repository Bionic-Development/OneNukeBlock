package de.takacick.elementalblock.registry.entity.living.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class AirElementalGolemEntityModel
        extends SinglePartEntityModel<VexEntity>
        implements ModelWithArms {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart head;

    public AirElementalGolemEntityModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root.getChild(EntityModelPartNames.ROOT);
        this.body = this.root.getChild(EntityModelPartNames.BODY);
        this.rightArm = this.body.getChild(EntityModelPartNames.RIGHT_ARM);
        this.leftArm = this.body.getChild(EntityModelPartNames.LEFT_ARM);
        this.rightWing = this.body.getChild(EntityModelPartNames.RIGHT_WING);
        this.leftWing = this.body.getChild(EntityModelPartNames.LEFT_WING);
        this.head = this.body.getChild(EntityModelPartNames.HEAD);
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body = root.addChild("body", ModelPartBuilder.create().uv(0, 21).cuboid(-2.5F, -7.8F, -1.0F, 5.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(19, 11).cuboid(-1.5F, -6.8F, -1.0F, 3.0F, 7.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 28).cuboid(-0.5F, -2.0F, -3.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -7.8F, 0.0F));

        ModelPartData right_arm = body.addChild("right_arm", ModelPartBuilder.create().uv(24, 21).cuboid(-1.25F, -0.3F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(-0.1F)), ModelTransform.pivot(-1.75F, -7.75F, 0.0F));

        ModelPartData right_item = right_arm.addChild("right_item", ModelPartBuilder.create(), ModelTransform.pivot(-0.25F, 6.75F, 0.0F));

        ModelPartData left_arm = body.addChild("left_arm", ModelPartBuilder.create().uv(15, 21).cuboid(-0.75F, -0.3F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(-0.1F)), ModelTransform.pivot(1.75F, -7.75F, 0.0F));

        ModelPartData left_wing = body.addChild("left_wing", ModelPartBuilder.create().uv(0, 11).cuboid(0.0F, 0.0F, 0.0F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -6.8F, 1.0F, 0.2182F, -0.2618F, -0.4363F));

        ModelPartData right_wing = body.addChild("right_wing", ModelPartBuilder.create().uv(21, 0).cuboid(-9.0F, 0.0F, 0.0F, 9.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -6.8F, 1.0F, 0.2182F, 0.2618F, 0.4363F));
        return TexturedModelData.of(modelData, 48, 48);
    }
    @Override
    public void setAngles(VexEntity vexEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.head.yaw = i * ((float) Math.PI / 180);
        this.head.pitch = j * ((float) Math.PI / 180);
        float k = MathHelper.cos(h * 5.5f * ((float) Math.PI / 180)) * 0.1f;
        this.rightArm.roll = 0.62831855f + k;
        this.leftArm.roll = -(0.62831855f + k);
        if (vexEntity.isCharging()) {
            this.body.pitch = 0.0f;
            this.setChargingArmAngles( k);
        } else {
            this.body.pitch = 0.15707964f;
        }

        this.leftWing.yaw = MathHelper.cos(h * 45.836624f * ((float) Math.PI / 180)) * ((float) Math.PI / 180) * 16.2f;
        this.rightWing.yaw = -this.leftWing.yaw;
    }

    private void setChargingArmAngles(float f) {
        this.rightArm.pitch = -1.2217305f;
        this.rightArm.yaw = 0.2617994f;
        this.rightArm.roll = -0.47123888f - f;
        this.leftArm.pitch = -1.2217305f;
        this.leftArm.yaw = -0.2617994f;
        this.leftArm.roll = 0.47123888f + f;
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        boolean bl = arm == Arm.RIGHT;
        ModelPart modelPart = bl ? this.rightArm : this.leftArm;
        this.root.rotate(matrices);
        this.body.rotate(matrices);
        modelPart.rotate(matrices);
        matrices.scale(0.55f, 0.55f, 0.55f);
        this.translateForHand(matrices, bl);
    }

    private void translateForHand(MatrixStack matrices, boolean mainHand) {
        if (mainHand) {
            matrices.translate(0.046875, -0.15625, 0.078125);
        } else {
            matrices.translate(-0.046875, -0.15625, 0.078125);
        }
    }
}

