package de.takacick.illegalwars.registry.entity.living.model;

import de.takacick.illegalwars.registry.entity.living.SharkEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

@Environment(value = EnvType.CLIENT)
public class SharkEntityModel<T extends SharkEntity>
        extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart tailFin;
    private final ModelPart rightFin;
    private final ModelPart leftFin;
    private final ModelPart lowerYaw;

    public SharkEntityModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.tail = this.body.getChild(EntityModelPartNames.TAIL);
        this.tailFin = this.tail.getChild(EntityModelPartNames.TAIL_FIN);
        this.rightFin = this.body.getChild(EntityModelPartNames.RIGHT_FIN);
        this.leftFin = this.body.getChild(EntityModelPartNames.LEFT_FIN);
        this.lowerYaw = this.body.getChild(EntityModelPartNames.HEAD).getChild("lower_yaw");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -7.0F, 0.0F, 8.0F, 7.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, -3.0F));

        ModelPartData body_r1 = body.addChild("body_r1", ModelPartBuilder.create().uv(51, 24).cuboid(0.0F, -6.0F, 4.0F, 0.0F, 7.0F, 11.0F, new Dilation(0.0F))
                .uv(19, 62).cuboid(-1.0F, -6.0F, 4.0F, 2.0F, 4.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 3.0F, 0.8727F, 0.0F, 0.0F));

        ModelPartData head = body.addChild("head", ModelPartBuilder.create().uv(58, 48).cuboid(-4.0F, -7.0F, -4.0F, 8.0F, 7.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 21).cuboid(-4.5F, -7.0F, -13.0F, 9.0F, 4.0F, 9.0F, new Dilation(0.0F))
                .uv(30, 0).cuboid(-4.5F, -3.0F, -13.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData lower_yaw = head.addChild("lower_yaw", ModelPartBuilder.create(), ModelTransform.of(0.0F, -2.0F, -4.2F, -0.5672F, 0.0F, 0.0F));

        ModelPartData head_r1 = lower_yaw.addChild("head_r1", ModelPartBuilder.create().uv(44, 12).cuboid(-4.0F, -1.0F, -9.0F, 8.0F, 2.0F, 9.0F, new Dilation(0.0F))
                .uv(0, 50).cuboid(-4.0F, 1.0F, -9.0F, 8.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, -0.5F, 0.6109F, 0.0F, 0.0F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create().uv(31, 35).cuboid(-2.0F, -2.5F, -1.0F, 4.0F, 5.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.5F, 14.0F));

        ModelPartData tail_fin = tail.addChild("tail_fin", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.7802F, 5.3557F));

        ModelPartData tail_fin_r1 = tail_fin.addChild("tail_fin_r1", ModelPartBuilder.create().uv(0, 21).cuboid(0.0F, 7.0F, 15.0F, 0.0F, 13.0F, 15.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, 12.0F, 15.0F, 2.0F, 5.0F, 4.0F, new Dilation(0.0F))
                .uv(35, 52).cuboid(-1.0F, 7.0F, 16.0F, 2.0F, 5.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 4.2802F, -16.3557F, 0.6981F, 0.0F, 0.0F));

        ModelPartData left_fin = body.addChild("left_fin", ModelPartBuilder.create().uv(31, 67).cuboid(0.0F, -4.0F, -1.5F, 1.0F, 4.0F, 7.0F, new Dilation(0.0F))
                .uv(49, 60).cuboid(1.0F, -4.0F, -1.5F, 0.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -2.0F, 5.0F, 0.9599F, 0.0F, 1.8675F));

        ModelPartData right_fin = body.addChild("right_fin", ModelPartBuilder.create().uv(67, 0).cuboid(-1.0F, -4.0F, -1.5F, 1.0F, 4.0F, 7.0F, new Dilation(0.0F))
                .uv(0, 62).cuboid(-1.0F, -4.0F, -1.5F, 0.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, -2.0F, 5.0F, 0.9599F, 0.0F, -1.8675F));
        return TexturedModelData.of(modelData, 96, 96);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.body.pitch = headPitch * ((float) Math.PI / 180);
        this.body.yaw = headYaw * ((float) Math.PI / 180);

        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
        this.lowerYaw.pitch += entity.getLowerYaw(tickDelta) * ((float) Math.PI / 180);

        if (entity.guiRendering) {
            this.lowerYaw.pitch += 0.30f * ((MathHelper.cos(animationProgress * 0.3f) + 1f) * 0.5f);
            this.body.pitch += -0.05f - 0.05f * MathHelper.cos(animationProgress * 0.3f);

            this.rightFin.pitch = -0.1f * MathHelper.cos(animationProgress * 0.3f);
            this.leftFin.pitch = -0.1f * MathHelper.cos(animationProgress * 0.3f);

            this.tail.yaw = -0.1f * MathHelper.cos(animationProgress * 0.3f);
            this.tailFin.yaw = -0.2f * MathHelper.cos(animationProgress * 0.3f);
        }

        if (entity.getVelocity().horizontalLengthSquared() > 1.0E-7) {
            this.body.pitch += -0.05f - 0.05f * MathHelper.cos(animationProgress * 0.3f);

            this.rightFin.pitch = -0.1f * MathHelper.cos(animationProgress * 0.3f);
            this.leftFin.pitch = -0.1f * MathHelper.cos(animationProgress * 0.3f);

            this.tail.yaw = -0.1f * MathHelper.cos(animationProgress * 0.3f);
            this.tailFin.yaw = -0.2f * MathHelper.cos(animationProgress * 0.3f);
        }
    }
}

