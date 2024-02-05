package de.takacick.secretcraftbase.client.model;

import de.takacick.secretcraftbase.access.PlayerProperties;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public class AnimationBodyModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    public final ModelPart rightArm;

    public AnimationBodyModel() {
        this.root = getTexturedModelData().createModel().getChild("bone");
        this.rightArm = root.getChild("right_arm");
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        bone.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, Dilation.NONE), ModelTransform.pivot(-5.0f, 2.0f + 0, 0.0f));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (livingEntity instanceof PlayerProperties playerProperties) {
            this.updateAnimation(playerProperties.getHeartRemovalState(), CARVE, h);
        }
    }

    @Override
    public Optional<ModelPart> getChild(String name) {
        return super.getChild(name);
    }

    public static final Animation CARVE = Animation.Builder.create(2f)
            .addBoneAnimation("right_arm",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.20834334f, AnimationHelper.createTranslationalVector(-0.9000000000000001f, -1.1f, 0.5999999999999999f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.875f, AnimationHelper.createTranslationalVector(-0f, -1.1f, 0.6f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("right_arm",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.20834334f, AnimationHelper.createRotationalVector(-130.10231982630694f, -34.35517707463152f, 91.45801234648707f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(-141.36387590048707f, -17.08348738978042f, 96.56499320059149f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(-132.7943384745681f, -35.576844377060965f, 77.63390963395861f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5416766f, AnimationHelper.createRotationalVector(-125.28999999999999f, -35.58f, 77.63f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.6766666f, AnimationHelper.createRotationalVector(-141.36387590048707f, -17.08348738978042f, 96.56499320059149f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7916766f, AnimationHelper.createRotationalVector(-132.7943384745681f, -35.576844377060965f, 77.63390963395861f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-125.28999999999999f, -35.58f, 77.63f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(-141.36387590048707f, -17.08348738978042f, 96.56499320059149f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.125f, AnimationHelper.createRotationalVector(-132.7943384745681f, -35.576844377060965f, 77.63390963395861f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.2083433f, AnimationHelper.createRotationalVector(-125.28999999999999f, -35.58f, 77.63f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.3433333f, AnimationHelper.createRotationalVector(-141.36387590048707f, -17.08348738978042f, 96.56499320059149f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.4583433f, AnimationHelper.createRotationalVector(-132.7943384745681f, -35.576844377060965f, 77.63390963395861f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.5416767f, AnimationHelper.createRotationalVector(-125.28999999999999f, -35.58f, 77.63f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.6766667f, AnimationHelper.createRotationalVector(-141.36387590048707f, -17.08348738978042f, 96.56499320059149f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.7916767f, AnimationHelper.createRotationalVector(-132.7943384745681f, -35.576844377060965f, 77.63390963395861f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.875f, AnimationHelper.createRotationalVector(-125.28999999999999f, -35.58f, 77.63f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(2f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .build();
}