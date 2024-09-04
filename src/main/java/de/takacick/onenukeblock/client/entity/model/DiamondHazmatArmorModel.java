package de.takacick.onenukeblock.client.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class DiamondHazmatArmorModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    public final ModelPart rightBelt;
    public final ModelPart leftBelt;

    public DiamondHazmatArmorModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.root = root;
        this.head = this.root.getChild("head");
        this.body = this.root.getChild("body");
        this.rightArm = this.root.getChild("right_arm");
        this.leftArm = this.root.getChild("left_arm");
        this.rightLeg = this.root.getChild("right_leg");
        this.leftLeg = this.root.getChild("left_leg");
        this.rightBelt = this.root.getChild("right_belt");
        this.leftBelt = this.root.getChild("left_belt");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public void copyFromBipedState(BipedEntityModel<T> model) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        model.copyStateTo(this);

        this.head.copyTransform(model.head);
        this.body.copyTransform(model.body);
        this.rightArm.copyTransform(model.rightArm);
        this.leftArm.copyTransform(model.leftArm);
        this.rightLeg.copyTransform(model.rightLeg);
        this.leftLeg.copyTransform(model.leftLeg);

        this.rightBelt.copyTransform(model.rightLeg);
        this.leftBelt.copyTransform(model.leftLeg);
    }

    @Override
    public void setAngles(T playerEntity, float f, float g, float h, float i, float j) {

    }

    public void setVisible(boolean visible) {
        this.head.visible = visible;
        this.body.visible = visible;
        this.rightArm.visible = visible;
        this.leftArm.visible = visible;
        this.rightLeg.visible = visible;
        this.leftLeg.visible = visible;
        this.leftBelt.visible = visible;
        this.rightBelt.visible = visible;
    }

    @Override
    public Optional<ModelPart> getChild(String name) {
        return super.getChild(name);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(51, 46).cuboid(-1.9419F, -3.25F, -5.3334F, 4.0F, 3.0F, 1.0F, new Dilation(0.1F))
                .uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.501F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head_r1 = head.addChild("head_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-2.7767F, -3.75F, -4.3585F, 0.0F, 0.3054F, 0.0F));

        ModelPartData head_r2 = head.addChild("head_r2", ModelPartBuilder.create().uv(0, 4).cuboid(-1.0F, 1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(2.7767F, -3.75F, -4.3585F, 0.0F, -0.3054F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 17).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.11F))
                .uv(31, 59).cuboid(-3.0F, 1.5F, 2.0F, 6.0F, 8.0F, 3.0F, new Dilation(0.11F))
                .uv(54, 46).cuboid(-0.5F, -1.7F, 2.0F, 1.0F, 3.0F, 1.0F, new Dilation(0.11F))
                .uv(0, 62).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.26F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(0, 51).cuboid(-3.0F, 7.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.4F))
                .uv(25, 29).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F))
                .uv(50, 0).cuboid(-3.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.4F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(50, 18).cuboid(-1.0F, 7.0F, -2.0F, 4.0F, 3.0F, 4.0F, new Dilation(0.4F))
                .uv(50, 9).cuboid(-1.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.4F))
                .uv(42, 29).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(17, 46).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(34, 46).cuboid(-2.0F, 7.0F, -2.0F, 4.0F, 5.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        ModelPartData right_belt = modelPartData.addChild("right_belt", ModelPartBuilder.create().uv(0, 34).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData left_belt = modelPartData.addChild("left_belt", ModelPartBuilder.create().uv(33, 0).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.1F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }
}