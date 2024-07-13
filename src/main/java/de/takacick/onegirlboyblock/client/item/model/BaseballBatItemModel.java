package de.takacick.onegirlboyblock.client.item.model;

import de.takacick.onegirlboyblock.utils.ArmHelper;
import de.takacick.onegirlboyblock.utils.data.AttachmentTypes;
import de.takacick.onegirlboyblock.utils.data.attachments.BaseballBatAnimationHelper;
import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.joml.Vector3f;

public class BaseballBatItemModel extends SinglePartItemModel {

    public BaseballBatItemModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        if (livingEntity != null) {
            BaseballBatAnimationHelper animationSyncHelper = livingEntity.getAttached(AttachmentTypes.BASEBALL_BAT);
            if (animationSyncHelper != null && animationSyncHelper.getTick(tickDelta) > 0) {
                long rotation = (long) (animationSyncHelper.getRotation(tickDelta) / 20 * 1000);

                if(ArmHelper.getArmStack(livingEntity, Arm.LEFT).equals(itemStack)) {
                    AnimationHelper.animate(this, ROTATE_LEFT, rotation, 1.0f, new Vector3f());
                } else {
                    AnimationHelper.animate(this, ROTATE, rotation, 1.0f, new Vector3f());
                }
                AnimationHelper.animate(this, ROTATE_HORIZONTAL, rotation, 1.0f, new Vector3f());
            }
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData baseball_bat = modelPartData.addChild("baseball_bat", ModelPartBuilder.create().uv(13, 13).cuboid(-1.5F, 2.5F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(17, 0).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 13).cuboid(-1.5F, -6.5F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.0F, -14.5F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.5F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

    public static TexturedModelData getThirdPersonTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData baseball_bat = modelPartData.addChild("baseball_bat", ModelPartBuilder.create().uv(13, 13).cuboid(-1.5F, 2.5F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(17, 0).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 13).cuboid(-1.5F, -6.5F, -1.5F, 3.0F, 4.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.0F, -14.5F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.1F, 0.0F, 1.5708F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }


    public static final Animation ROTATE = Animation.Builder.create(0.5f).looping()
            .addBoneAnimation("baseball_bat",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(360f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1.4f, 1.5f, -1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(360f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation ROTATE_HORIZONTAL = Animation.Builder.create(2f).looping()
            .addBoneAnimation("baseball_bat",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 87.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 87.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.9583434f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2f, AnimationHelper.createRotationalVector(0f, 87.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 87.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 87.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.9583434f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.75f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(2f, AnimationHelper.createRotationalVector(0f, 87.5f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ROTATE_LEFT = Animation.Builder.create(0.5f).looping()
            .addBoneAnimation("baseball_bat",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(-360f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(-1.4f, 1.5f, -1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(360f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();
}
