package de.takacick.onesuperblock.registry.entity.living.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.TintableAnimalModel;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.math.MathHelper;

public class ProtoEntityModel<T extends WolfEntity>
        extends TintableAnimalModel<T> {

    private final ModelPart realHead;
    private final ModelPart torso;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    private final ModelPart tail;
    private final ModelPart neck;

    public ProtoEntityModel(ModelPart root) {
        this.realHead = root.getChild("head");
        this.torso = root.getChild("body");
        this.neck = root.getChild("neck");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.tail = root.getChild("tail");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData neck = modelPartData.addChild("neck", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, -6.0F));

        ModelPartData neck_r1 = neck.addChild("neck_r1", ModelPartBuilder.create().uv(17, 26).cuboid(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.4586F, -0.3785F, -0.829F, 0.0F, 0.0F));

        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 13).cuboid(-3.0F, -4.75F, -4.3F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(18, 57).cuboid(-3.0F, -6.75F, 0.7F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(18, 53).cuboid(1.0F, -6.75F, 0.7F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 26).cuboid(-1.5F, -1.7656F, -8.3F, 3.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 11.25F, -8.0F));

        ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create().uv(0, 53).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(27, 44).cuboid(-1.0F, 0.0F, 3.0F, 2.0F, 5.0F, 2.0F, new Dilation(0.0F))
                .uv(9, 53).cuboid(-1.0F, 3.0F, 1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 7.7F, 1.5708F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(25, 0).cuboid(-3.0F, -0.3F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F))
                .uv(25, 13).cuboid(-2.0F, -2.3F, -3.0F, 4.0F, 2.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -8.3F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 14.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        ModelPartData right_hind_leg = modelPartData.addChild("right_hind_leg", ModelPartBuilder.create().uv(18, 44).cuboid(0.0F, 2.0F, -2.3F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 35).cuboid(0.0F, -1.0F, -2.3F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 16.0F, 7.1F));

        ModelPartData left_hind_leg = modelPartData.addChild("left_hind_leg", ModelPartBuilder.create().uv(33, 35).cuboid(0.0F, 2.0F, -2.3F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(22, 35).cuboid(0.0F, -1.0F, -2.3F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(1.5F, 16.0F, 7.1F));

        ModelPartData right_front_leg = modelPartData.addChild("right_front_leg", ModelPartBuilder.create().uv(18, 44).cuboid(-1.0F, 2.0F, -2.3F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 35).cuboid(-1.0F, -1.0F, -2.3F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 16.0F, -4.1F));

        ModelPartData left_front_leg = modelPartData.addChild("left_front_leg", ModelPartBuilder.create().uv(33, 35).cuboid(1.0F, 2.0F, -2.3F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(22, 35).cuboid(1.0F, -1.0F, -2.3F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, 16.0F, -4.1F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.realHead);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.torso, this.rightHindLeg, this.leftHindLeg, this.rightFrontLeg, this.leftFrontLeg, this.tail, this.neck);
    }

    @Override
    public void animateModel(T wolfEntity, float f, float g, float h) {
        this.tail.yaw = wolfEntity.hasAngerTime() ? 0.0f : MathHelper.cos(f * 0.6662f) * 0.4f * g;

        this.torso.pitch = 1.5707964f;
        this.neck.pitch = -1.5708F;

        this.rightHindLeg.pitch = MathHelper.cos(f * 0.6662f) * 1.4f * g;
        this.leftHindLeg.pitch = MathHelper.cos(f * 0.6662f + (float) Math.PI) * 1.4f * g;
        this.rightFrontLeg.pitch = MathHelper.cos(f * 0.6662f + (float) Math.PI) * 1.4f * g;
        this.leftFrontLeg.pitch = MathHelper.cos(f * 0.6662f) * 1.4f * g;

        this.realHead.roll = wolfEntity.getBegAnimationProgress(h) + wolfEntity.getShakeAnimationProgress(h, 0.0f);
        this.neck.roll = wolfEntity.getShakeAnimationProgress(h, -0.08f);
        this.torso.roll = wolfEntity.getShakeAnimationProgress(h, -0.16f);
        this.tail.roll = wolfEntity.getShakeAnimationProgress(h, -0.2f);

        if (wolfEntity.isInSittingPose()) {
            this.torso.setPivot(0.0F, 18.0F, 0.0F);
            this.torso.pitch = 0.7853982F;
            this.neck.pitch = -1.5708F;
            this.tail.setPivot(-1.0F, 21.0F, 6.0F);
            this.rightHindLeg.setPivot(-2.5F, 22.7F, 2.0F);
            this.rightHindLeg.pitch = 4.712389F;
            this.leftHindLeg.setPivot(0.5F, 22.7F, 2.0F);
            this.leftHindLeg.pitch = 4.712389F;
            this.rightFrontLeg.pitch = 5.811947F;
            this.rightFrontLeg.setPivot(-2.49F, 17.0F, -4.0F);
            this.leftFrontLeg.pitch = 5.811947F;
            this.leftFrontLeg.setPivot(0.51F, 17.0F, -4.0F);
        } else {
            this.torso.setPivot(0.0F, 14.0F, 2.0F);
            this.torso.pitch = 1.5707964F;
            this.neck.pitch = -this.torso.pitch;
            this.tail.setPivot(-1.0F, 12.0F, 8.0F);
            this.rightHindLeg.setPivot(-2.5F, 16.0F, 7.0F);
            this.leftHindLeg.setPivot(0.5F, 16.0F, 7.0F);
            this.rightFrontLeg.setPivot(-2.5F, 16.0F, -4.0F);
            this.leftFrontLeg.setPivot(0.5F, 16.0F, -4.0F);
            this.rightHindLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g;
            this.leftHindLeg.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * g;
            this.rightFrontLeg.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * g;
            this.leftFrontLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g;
        }

    }


    @Override
    public void setAngles(T wolfEntity, float f, float g, float h, float i, float j) {
        this.realHead.pitch = j * ((float) Math.PI / 180);
        this.realHead.yaw = i * ((float) Math.PI / 180);
        this.tail.pitch = h;
    }
}
