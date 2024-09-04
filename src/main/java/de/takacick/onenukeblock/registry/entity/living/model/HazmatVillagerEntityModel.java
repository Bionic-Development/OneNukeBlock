package de.takacick.onenukeblock.registry.entity.living.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.util.math.MathHelper;

public class HazmatVillagerEntityModel<T extends Entity> extends SinglePartEntityModel<T> implements ModelWithHead, ModelWithHat {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public HazmatVillagerEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.0F))
                .uv(46, 37).cuboid(-4.0F, -6.0F, -5.0F, 8.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(34, 9).cuboid(-1.0F, -2.0F, -7.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(16, 38).cuboid(1.0F, -2.0F, -7.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(16, 38).mirrored().cuboid(-4.0F, -2.0F, -7.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(49, 10).cuboid(-1.0F, -4.0F, -6.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData arms = modelPartData.addChild("arms", ModelPartBuilder.create().uv(33, 0).cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(42, 45).cuboid(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 38).cuboid(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 19).cuboid(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(29, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(29, 15).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 12.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        boolean bl = false;
        if (entity instanceof MerchantEntity) {
            bl = ((MerchantEntity) entity).getHeadRollingTimeLeft() > 0;
        }
        this.head.yaw = headYaw * ((float) Math.PI / 180);
        this.head.pitch = headPitch * ((float) Math.PI / 180);
        if (bl) {
            this.head.roll = 0.3f * MathHelper.sin(0.45f * animationProgress);
            this.head.pitch = 0.4f;
        } else {
            this.head.roll = 0.0f;
        }
        this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance * 0.5f;
        this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float) Math.PI) * 1.4f * limbDistance * 0.5f;
        this.rightLeg.yaw = 0.0f;
        this.leftLeg.yaw = 0.0f;
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public void setHatVisible(boolean visible) {
        this.head.visible = visible;
    }

}

