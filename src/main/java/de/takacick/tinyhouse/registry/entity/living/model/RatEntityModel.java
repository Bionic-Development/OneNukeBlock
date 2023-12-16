package de.takacick.tinyhouse.registry.entity.living.model;

import de.takacick.tinyhouse.registry.entity.living.RatEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class RatEntityModel<T extends RatEntity>
        extends SinglePartEntityModel<T> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightFrontLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart tail;
    private final ModelPart hindTail;

    public RatEntityModel(ModelPart root) {
        this.root = root.getChild("body");
        this.head = this.root.getChild("head");
        this.rightFrontLeg = this.root.getChild("right_front_leg");
        this.rightHindLeg = this.root.getChild("right_hind_leg");
        this.leftFrontLeg = this.root.getChild("left_front_leg");
        this.leftHindLeg = this.root.getChild("left_hind_leg");
        this.tail = this.root.getChild("tail");
        this.hindTail = this.tail.getChild("tail_hind");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, 0.0F, -3.5F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 20.0F, 1.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(15, 11).cuboid(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(19, 25).cuboid(1.5F, -2.5F, -1.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
                .uv(16, 25).cuboid(-2.5F, -2.5F, -1.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 1.5F, -3.5F));

        ModelPartData right_front_leg = body.addChild("right_front_leg", ModelPartBuilder.create().uv(7, 25).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.0F, 4.0F, -2.5F));

        ModelPartData left_front_leg = body.addChild("left_front_leg", ModelPartBuilder.create().uv(7, 25).cuboid(0.0F, 0.0F, -2.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, 4.0F, -2.5F));

        ModelPartData right_hind_leg = body.addChild("right_hind_leg", ModelPartBuilder.create().uv(22, 19).mirrored().cuboid(-2.0F, 1.0F, -2.0F, 2.0F, 0.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 25).mirrored().cuboid(-1.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.0F, 3.0F, 1.5F));

        ModelPartData left_hind_leg = body.addChild("left_hind_leg", ModelPartBuilder.create().uv(22, 19).cuboid(0.0F, 1.0F, -2.0F, 2.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 25).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(3.0F, 3.0F, 1.5F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(11, 19).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 2.5F));

        ModelPartData tail_hind = tail.addChild("tail_hind", ModelPartBuilder.create().uv(0, 19).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 3.0F));
        return TexturedModelData.of(modelData, 48, 48);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root.resetTransform();

        this.head.pitch = headPitch * ((float) Math.PI / 180);
        this.head.yaw = headYaw * ((float) Math.PI / 180);

        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();

        this.root.pitch = -(entity.getBodyPitch(tickDelta)) * ((float) Math.PI / 180);

        float k = 1f;

        this.tail.yaw = MathHelper.cos(animationProgress * 0.9f + (float) 1 * 0.15f * (float) Math.PI) * (float) Math.PI * 0.05f * (float) (1 + Math.abs(1 - 2)) * Math.max(limbDistance, 0.1f);
        this.hindTail.yaw = MathHelper.cos(animationProgress * 0.9f + (float) 2 * 0.15f * (float) Math.PI) * (float) Math.PI * 0.05f * (float) (1 + Math.abs(2 - 2)) * Math.max(limbDistance, 0.1f);

        this.rightFrontLeg.pitch = (MathHelper.cos(limbAngle * 0.8662f) + 1) * -0.5f * 1.4f * limbDistance / k;
        this.leftFrontLeg.pitch = (MathHelper.cos(limbAngle * 0.8662f + (float) Math.PI) + 1) * -0.5f * 1.4f * limbDistance / k;

        this.rightHindLeg.pitch = this.leftFrontLeg.pitch;
        this.leftHindLeg.pitch = this.rightFrontLeg.pitch;
    }
}

