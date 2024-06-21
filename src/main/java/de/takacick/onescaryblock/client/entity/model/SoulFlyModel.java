package de.takacick.onescaryblock.client.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class SoulFlyModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;

    public SoulFlyModel(ModelPart root) {
        this.root = root;
        this.rightLeg = this.root.getChild("right_leg");
        this.leftLeg = this.root.getChild("left_leg");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public void copyFromBipedState(BipedEntityModel<T> model) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        model.copyStateTo(this);

        this.rightLeg.copyTransform(model.rightLeg);
        this.leftLeg.copyTransform(model.leftLeg);
    }

    @Override
    public void setAngles(T playerEntity, float f, float g, float h, float i, float j) {

    }

    @Override
    public Optional<ModelPart> getChild(String name) {
        return super.getChild(name);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    public static TexturedModelData getTexturedFireModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 32), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData right_leg_flames = right_leg.addChild("right_leg_flames", ModelPartBuilder.create(), ModelTransform.pivot(-0.1F, 10.295F, 2.8359F));

        ModelPartData left_leg_flames_r1 = right_leg_flames.addChild("left_leg_flames_r1", ModelPartBuilder.create().uv(0, 48).mirrored().cuboid(0.0F, -2.0F, -2.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.906F, 0.0175F, -2.8359F, 0.0F, 0.0F, 0.3054F));

        ModelPartData right_leg_flames_r1 = right_leg_flames.addChild("right_leg_flames_r1", ModelPartBuilder.create().uv(0, 52).mirrored().cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.3054F, 0.0F, 0.0F));

        ModelPartData right_leg_flames_r2 = right_leg_flames.addChild("right_leg_flames_r2", ModelPartBuilder.create().uv(0, 48).mirrored().cuboid(0.0F, -2.0F, -2.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.719F, 0.0425F, -2.8109F, 0.0F, 0.0F, -0.3054F));

        ModelPartData right_leg_flames_r3 = right_leg_flames.addChild("right_leg_flames_r3", ModelPartBuilder.create().uv(0, 52).mirrored().cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, -5.6F, 0.3054F, 0.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 32), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        ModelPartData left_leg_flames = left_leg.addChild("left_leg_flames", ModelPartBuilder.create(), ModelTransform.pivot(0.1F, 10.295F, 2.8359F));

        ModelPartData left_leg_flames_r2 = left_leg_flames.addChild("left_leg_flames_r2", ModelPartBuilder.create().uv(0, 48).mirrored().cuboid(0.0F, -2.0F, -2.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-2.919F, 0.0425F, -2.8109F, 0.0F, 0.0F, -0.3054F));

        ModelPartData left_leg_flames_r3 = left_leg_flames.addChild("left_leg_flames_r3", ModelPartBuilder.create().uv(25, 0).mirrored().cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.3054F, 0.0F, 0.0F));

        ModelPartData left_leg_flames_r4 = left_leg_flames.addChild("left_leg_flames_r4", ModelPartBuilder.create().uv(25, 0).mirrored().cuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 0.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, -5.6F, 0.3054F, 0.0F, 0.0F));

        ModelPartData left_leg_flames_r5 = left_leg_flames.addChild("left_leg_flames_r5", ModelPartBuilder.create().uv(0, 48).mirrored().cuboid(0.0F, -2.0F, -2.0F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(2.706F, 0.0175F, -2.8359F, 0.0F, 0.0F, 0.3054F));
        return TexturedModelData.of(modelData, 64, 64);
    }

}