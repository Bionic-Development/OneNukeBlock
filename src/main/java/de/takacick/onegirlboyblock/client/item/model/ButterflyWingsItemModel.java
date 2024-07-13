package de.takacick.onegirlboyblock.client.item.model;

import de.takacick.onegirlboyblock.client.shader.OneGirlBoyBlockLayers;
import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.joml.Vector3f;

public class ButterflyWingsItemModel extends SinglePartItemModel {

    public ButterflyWingsItemModel(ModelPart root) {
        super(root, OneGirlBoyBlockLayers::getBitCannonGlow);
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        AnimationHelper.animate(this, IDLE, (long) (((time + tickDelta) / 20f) * 1000f), 1.0f, new Vector3f(0, 0, 0));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData butterfly_wings = modelPartData.addChild("butterfly_wings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10.5F, 0.0F));

        ModelPartData left_wing = butterfly_wings.addChild("left_wing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 1.0F));

        ModelPartData left_wing_r1 = left_wing.addChild("left_wing_r1", ModelPartBuilder.create().uv(20, 0).cuboid(-5.5F, -10.0F, -1.0F, 11.0F, 20.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.5F, -1.5F, 1.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData right_wing = butterfly_wings.addChild("right_wing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 1.0F));

        ModelPartData right_wing_r1 = right_wing.addChild("right_wing_r1", ModelPartBuilder.create().uv(20, 0).mirrored().cuboid(-5.5F, -10.0F, -1.0F, 11.0F, 20.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(5.5F, -1.5F, 1.0F, 0.0F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    public static final Animation IDLE = Animation.Builder.create(0.5f).looping()
            .addBoneAnimation("left_wing",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 25f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_wing",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, -25f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();
}