package de.takacick.onenukeblock.client.item.model;

import de.takacick.utils.item.client.model.SinglePartItemModel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import org.joml.Vector3f;

public class ExplosiveGummyBearItemModel extends SinglePartItemModel {

    private final ModelPart bone;
    private final ModelPart outerBone;
    private final ModelPart innerBone;

    public ExplosiveGummyBearItemModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
        this.outerBone = this.bone.getChild("outer_bone");
        this.innerBone = this.bone.getChild("inner_bone");
    }

    @Override
    public void setAngles(ItemStack itemStack, LivingEntity livingEntity, ModelTransformationMode modelTransformationMode, long time, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        AnimationHelper.animate(this, IDLE, (long) (((time + tickDelta) / 20f) * 1000f), 1.0f, new Vector3f(0, 0, 0));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 2.0F));

        ModelPartData outer_bone = bone.addChild("outer_bone", ModelPartBuilder.create().uv(41, 23).cuboid(-6.0F, -20.0F, -6.0F, 12.0F, 20.0F, 1.0F, new Dilation(0.2F))
                .uv(45, 0).cuboid(-6.0F, -20.0F, 1.0F, 12.0F, 19.0F, 1.0F, new Dilation(0.2F))
                .uv(0, 0).cuboid(-6.0F, -32.0F, -7.0F, 12.0F, 12.0F, 10.0F, new Dilation(0.201F))
                .uv(0, 23).cuboid(-7.0F, -20.0F, -5.0F, 14.0F, 20.0F, 6.0F, new Dilation(0.2F))
                .uv(0, 87).cuboid(-6.0F, -1.0F, -5.0F, 12.0F, 1.0F, 7.0F, new Dilation(0.199F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData outer_bone_r1 = outer_bone.addChild("outer_bone_r1", ModelPartBuilder.create().uv(58, 56).cuboid(-3.0F, -3.5F, -2.0F, 6.0F, 7.0F, 4.0F, new Dilation(0.2F)), ModelTransform.of(-6.0F, -30.5F, -2.0F, -0.1309F, 0.0873F, 0.0F));

        ModelPartData outer_bone_r2 = outer_bone.addChild("outer_bone_r2", ModelPartBuilder.create().uv(58, 56).mirrored().cuboid(-3.0F, -3.5F, -2.0F, 6.0F, 7.0F, 4.0F, new Dilation(0.2F)).mirrored(false), ModelTransform.of(6.0F, -30.5F, -2.0F, -0.1309F, -0.0873F, 0.0F));

        ModelPartData outer_bone_r3 = outer_bone.addChild("outer_bone_r3", ModelPartBuilder.create().uv(33, 45).mirrored().cuboid(-4.25F, -3.0F, -6.25F, 6.0F, 6.0F, 8.0F, new Dilation(0.2F)).mirrored(false)
                .uv(0, 0).cuboid(0.75F, -2.025F, 1.75F, 1.0F, 4.0F, 1.0F, new Dilation(0.1999F)), ModelTransform.of(6.25F, -2.975F, -2.75F, 0.0F, -0.0873F, 0.0F));

        ModelPartData outer_bone_r4 = outer_bone.addChild("outer_bone_r4", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.75F, -2.0F, 1.75F, 1.0F, 4.0F, 1.0F, new Dilation(0.1999F)).mirrored(false)
                .uv(33, 45).cuboid(-1.75F, -2.975F, -6.25F, 6.0F, 6.0F, 8.0F, new Dilation(0.2F)), ModelTransform.of(-6.25F, -3.0F, -2.75F, 0.0F, 0.0873F, 0.0F));

        ModelPartData outer_bone_r5 = outer_bone.addChild("outer_bone_r5", ModelPartBuilder.create().uv(0, 50).cuboid(-1.25F, -2.5F, -5.5F, 4.0F, 5.0F, 7.0F, new Dilation(0.2F))
                .uv(0, 0).cuboid(-1.25F, -2.0F, 1.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.1999F)), ModelTransform.of(-6.75F, -16.5F, -2.5F, 0.0873F, 0.1745F, 0.0F));

        ModelPartData outer_bone_r6 = outer_bone.addChild("outer_bone_r6", ModelPartBuilder.create().uv(0, 50).mirrored().cuboid(-2.75F, -2.5F, -5.5F, 4.0F, 5.0F, 7.0F, new Dilation(0.2F)).mirrored(false)
                .uv(0, 0).mirrored().cuboid(0.25F, -2.0F, 1.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.1999F)).mirrored(false), ModelTransform.of(6.75F, -16.5F, -2.5F, 0.0873F, -0.1745F, 0.0F));

        ModelPartData inner_bone = bone.addChild("inner_bone", ModelPartBuilder.create().uv(0, 23).cuboid(-7.0F, -20.0F, -5.0F, 14.0F, 20.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -32.0F, -7.0F, 12.0F, 12.0F, 10.0F, new Dilation(-0.3F))
                .uv(45, 0).cuboid(-6.0F, -20.0F, 1.0F, 12.0F, 19.0F, 1.0F, new Dilation(0.0F))
                .uv(41, 23).cuboid(-6.0F, -20.0F, -6.0F, 12.0F, 20.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 87).cuboid(-6.0F, -1.0F, -5.0F, 12.0F, 1.0F, 7.0F, new Dilation(-0.3F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData inner_bone_r1 = inner_bone.addChild("inner_bone_r1", ModelPartBuilder.create().uv(0, 50).mirrored().cuboid(-2.75F, -2.5F, -5.5F, 4.0F, 5.0F, 7.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(6.75F, -16.5F, -2.5F, 0.0873F, -0.1745F, 0.0F));

        ModelPartData inner_bone_r2 = inner_bone.addChild("inner_bone_r2", ModelPartBuilder.create().uv(0, 50).cuboid(-1.25F, -2.5F, -5.5F, 4.0F, 5.0F, 7.0F, new Dilation(-0.3F)), ModelTransform.of(-6.75F, -16.5F, -2.5F, 0.0873F, 0.1745F, 0.0F));

        ModelPartData inner_bone_r3 = inner_bone.addChild("inner_bone_r3", ModelPartBuilder.create().uv(33, 45).cuboid(-1.75F, -3.0F, -6.25F, 6.0F, 6.0F, 8.0F, new Dilation(-0.3F))
                .uv(0, 0).mirrored().cuboid(-1.75F, -2.025F, 1.75F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-6.25F, -2.975F, -2.75F, 0.0F, 0.0873F, 0.0F));

        ModelPartData inner_bone_r4 = inner_bone.addChild("inner_bone_r4", ModelPartBuilder.create().uv(0, 0).cuboid(0.75F, -2.0F, 1.75F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
                .uv(33, 45).mirrored().cuboid(-4.25F, -2.975F, -6.25F, 6.0F, 6.0F, 8.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(6.25F, -3.0F, -2.75F, 0.0F, -0.0873F, 0.0F));

        ModelPartData inner_bone_r5 = inner_bone.addChild("inner_bone_r5", ModelPartBuilder.create().uv(58, 56).mirrored().cuboid(-3.0F, -3.5F, -2.0F, 6.0F, 7.0F, 4.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(6.0F, -30.5F, -2.0F, -0.1309F, -0.0873F, 0.0F));

        ModelPartData inner_bone_r6 = inner_bone.addChild("inner_bone_r6", ModelPartBuilder.create().uv(58, 56).cuboid(-3.0F, -3.5F, -2.0F, 6.0F, 7.0F, 4.0F, new Dilation(-0.3F)), ModelTransform.of(-6.0F, -30.5F, -2.0F, -0.1309F, 0.0873F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        matrices.push();
        this.getPart().rotate(matrices);
        this.bone.rotate(matrices);
        int alpha = ColorHelper.Argb.getAlpha(color);

        this.innerBone.render(matrices, vertices, light, overlay, color);
        this.outerBone.render(matrices, vertices, light, overlay, ColorHelper.Argb.withAlpha((int) (alpha * 0.8), color));
        matrices.pop();
    }

    public static final Animation IDLE = Animation.Builder.create(0f).looping()
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(0.3f, 0.3f, 0.3f),
                                    Transformation.Interpolations.LINEAR))).build();
}