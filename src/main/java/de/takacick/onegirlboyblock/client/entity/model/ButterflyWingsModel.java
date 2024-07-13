package de.takacick.onegirlboyblock.client.entity.model;

import de.takacick.utils.item.client.model.ItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class ButterflyWingsModel<T extends LivingEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    private final ModelPart butterflyWings;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public ButterflyWingsModel(ModelPart root) {
        this.root = root;
        this.butterflyWings = this.root.getChild("butterfly_wings");
        this.rightWing = this.butterflyWings.getChild("right_wing");
        this.leftWing  = this.butterflyWings.getChild("left_wing");
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        AnimationHelper.animate(this, IDLE, (long) ((h / 20f) * 1000f), 1.0f, new Vector3f(0, 0, 0));
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData butterfly_wings = modelPartData.addChild("butterfly_wings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10.5F, -1.0F));

        ModelPartData left_wing = butterfly_wings.addChild("left_wing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 1.0F));

        ModelPartData left_wing_r1 = left_wing.addChild("left_wing_r1", ModelPartBuilder.create().uv(20, 0).cuboid(-5.5F, -10.0F, -1.0F, 11.0F, 20.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.5F, -1.5F, 1.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData right_wing = butterfly_wings.addChild("right_wing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 1.0F));

        ModelPartData right_wing_r1 = right_wing.addChild("right_wing_r1", ModelPartBuilder.create().uv(20, 0).mirrored().cuboid(-5.5F, -10.0F, -1.0F, 11.0F, 20.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(5.5F, -1.5F, 1.0F, 0.0F, 3.1416F, 0.0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    public static final Animation IDLE = Animation.Builder.create(0.5f).looping()
            .addBoneAnimation("left_wing",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 15f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 45f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 15f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_wing",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, -15f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, -45f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, -15f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();
}