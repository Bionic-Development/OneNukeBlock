package de.takacick.imagineanything.registry.entity.custom.model;

import de.takacick.imagineanything.access.PlayerProperties;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class HeadModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    public final ModelPart head;
    public final ModelPart rightArm;
    public final ModelPart leftArm;

    public HeadModel() {
        this.root = getTexturedModelData().createModel().getChild("bone");
        this.head = root.getChild("head");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        bone.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation.NONE), ModelTransform.pivot(0.0f, 0.0f + 0, 0.0f));
        bone.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation.NONE.add(0.5f)), ModelTransform.pivot(0.0f, 0.0f + 0, 0.0f));
        bone.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(-5.0f, 2.0f + 0, 0.0f));
        bone.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(5.0f, 2.0f + 0, 0.0f));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (livingEntity instanceof PlayerProperties playerProperties) {
            this.updateAnimation(playerProperties.getHeadRemovalState(), HEAD_REMOVAL, h);
        }
    }

    @Override
    public Optional<ModelPart> getChild(String name) {
        return super.getChild(name);
    }

    public static final Animation HEAD_REMOVAL = Animation.Builder.create(1.9167667f)
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.method_41823(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5f, AnimationHelper.method_41823(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5834334f, AnimationHelper.method_41823(0f, 0.5f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.7916766f, AnimationHelper.method_41823(0f, 0.5f, -2f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(1f, AnimationHelper.method_41823(0f, -0.5f, -6f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(1.25f, AnimationHelper.method_41823(0f, -4.5f, -10f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(1.5f, AnimationHelper.method_41823(0f, -10.5f, -9f),
                                    Transformation.Interpolations.field_37885)))
            .addBoneAnimation("right_arm",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.5f, AnimationHelper.method_41823(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5834334f, AnimationHelper.method_41823(0f, 0.4f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.7916766f, AnimationHelper.method_41823(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885)))
            .addBoneAnimation("right_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5f, AnimationHelper.method_41829(-185f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5834334f, AnimationHelper.method_41829(-178.78f, -15f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.7916766f, AnimationHelper.method_41829(-163.24f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(1.5f, AnimationHelper.method_41829(-55f, -15f, 0f),
                                    Transformation.Interpolations.field_37885)))
            .addBoneAnimation("left_arm",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0.5f, AnimationHelper.method_41823(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5834334f, AnimationHelper.method_41823(0f, 0.4f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.7916766f, AnimationHelper.method_41823(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885)))
            .addBoneAnimation("left_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.method_41829(0f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5f, AnimationHelper.method_41829(-185f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.5834334f, AnimationHelper.method_41829(-178.59f, 15f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(0.7916766f, AnimationHelper.method_41829(-162.58f, 0f, 0f),
                                    Transformation.Interpolations.field_37885),
                            new Keyframe(1.5f, AnimationHelper.method_41829(-55f, 15f, 0f),
                                    Transformation.Interpolations.field_37885))).build();
}