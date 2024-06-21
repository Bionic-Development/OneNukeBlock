package de.takacick.onescaryblock.client.item.model;

import de.takacick.onescaryblock.OneScaryBlock;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

public class PhantomBlockItemModel extends ItemModel {

    public static final Identifier TEXTURE = new Identifier(OneScaryBlock.MOD_ID, "textures/entity/phantom_block.png");
    private final ModelPart root;

    public PhantomBlockItemModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucentCull);

        this.root = root;
    }

    @Override
    public void setAngles(LivingEntity livingEntity, ItemStack itemStack, long time, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        AnimationHelper.animate(this, ITEM_IDLE, (long) ((time + tickDelta) / 20 * 1000), 1.0f, new Vector3f());
    }

    public void setBlockAngles(BlockPos blockPos, long time, float tickDelta) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        AnimationHelper.animate(this, IDLE, (long) ((time + tickDelta) / 20 * 1000), 1.0f, new Vector3f());
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public Identifier getTexture(ItemStack itemStack) {
        return TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public static final Animation IDLE = Animation.Builder.create(1.25f).looping()
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.20834334f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.375f, AnimationHelper.createRotationalVector(4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5416766f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.625f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7916766f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9583434f, AnimationHelper.createRotationalVector(4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.0416767f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.125f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.2083433f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.125f, AnimationHelper.createScalingVector(1.2f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, AnimationHelper.createScalingVector(1.2f, 1f, 1.2f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1f, 1.2f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7083434f, AnimationHelper.createScalingVector(1.2f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7916766f, AnimationHelper.createScalingVector(1.2f, 1f, 1.2f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createScalingVector(1f, 1.2f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.9583434f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0416767f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR))).build();

    public static final Animation ITEM_IDLE = Animation.Builder.create(1.25f).looping()
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.125f, AnimationHelper.createTranslationalVector(1.5f, 2f, -1.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createTranslationalVector(-1f, 0f, 2f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createTranslationalVector(0f, 3.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.625f, AnimationHelper.createTranslationalVector(-2f, 1.5f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7916766f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.9167666f, AnimationHelper.createTranslationalVector(1.5f, 2f, -1.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0834333f, AnimationHelper.createTranslationalVector(-1f, 0f, 2f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.2083433f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.20834334f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.2916767f, AnimationHelper.createRotationalVector(4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.375f, AnimationHelper.createRotationalVector(4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.4583433f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5416766f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.625f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.7916766f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9583434f, AnimationHelper.createRotationalVector(4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.0416767f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, -4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.125f, AnimationHelper.createRotationalVector(-4.98f, -0.44f, 4.98f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.2083433f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("bone",
                    new Transformation(Transformation.Targets.SCALE,
                            new Keyframe(0f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.125f, AnimationHelper.createScalingVector(1.2f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.20834334f, AnimationHelper.createScalingVector(1.2f, 1f, 1.2f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.2916767f, AnimationHelper.createScalingVector(1f, 1.2f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.375f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.4583433f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7083434f, AnimationHelper.createScalingVector(1.2f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.7916766f, AnimationHelper.createScalingVector(1.2f, 1f, 1.2f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.875f, AnimationHelper.createScalingVector(1f, 1.2f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.9583434f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1.0416767f, AnimationHelper.createScalingVector(1f, 1f, 1f),
                                    Transformation.Interpolations.LINEAR))).build();
}
