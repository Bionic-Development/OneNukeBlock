package de.takacick.imagineanything.registry.entity.living.model;

import de.takacick.imagineanything.registry.entity.living.MysteriousEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.mob.MobEntity;

public class MysteriousEntityModel<I extends MobEntity> extends SinglePartEntityModel<MysteriousEntity> {

    public static final Animation ROTATE = Animation.Builder.create(2f).looping()
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(1.4583433f, AnimationHelper.method_41829(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37884)))
            .addBoneAnimation("bone5",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37884),
                            new Keyframe(2f, AnimationHelper.method_41829(0f, -360f, 0f),
                                    Transformation.Interpolations.field_37884)))
            .addBoneAnimation("bone5",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.method_41823(0.8f, 0.8f, 0.8f),
                                    Transformation.Interpolations.field_37884)))
            .addBoneAnimation("bone4",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37884),
                            new Keyframe(2f, AnimationHelper.method_41829(0f, 360f, 0f),
                                    Transformation.Interpolations.field_37884)))
            .addBoneAnimation("bone2",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37884),
                            new Keyframe(2f, AnimationHelper.method_41829(360f, 360f, 0f),
                                    Transformation.Interpolations.field_37884))).build();

    private ModelPart bone;

    public MysteriousEntityModel(ModelPart root) {
        this.bone = root.getChild("bone");
    }

    @Override
    public void setAngles(MysteriousEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.updateAnimation(entity.getAnimationState(), ROTATE, animationProgress);
        getPart().getChild("bone5").visible = false;
        getPart().getChild("bone4").visible = false;
    }

    @Override
    public ModelPart getPart() {
        return this.bone;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData bone5 = bone.addChild("bone5", ModelPartBuilder.create().uv(-32, 33).cuboid(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone4 = bone.addChild("bone4", ModelPartBuilder.create().uv(-32, 33).cuboid(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(-2.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 72, 72);
    }

    public static TexturedModelData getOuterTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData bone5 = bone.addChild("bone5", ModelPartBuilder.create().uv(-32, 33).cuboid(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone4 = bone.addChild("bone4", ModelPartBuilder.create().uv(-32, 33).cuboid(-16.0F, 0.0F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(-1.9F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 72, 72);
    }
}

