package de.takacick.heartmoney.registry.entity.living.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class GirlfriendEntityModel<T extends LivingEntity>
        extends BipedEntityModel<T> {

    private final List<ModelPart> parts;
    private final boolean thinArms;

    public GirlfriendEntityModel(ModelPart root, boolean thinArms) {
        super(root, RenderLayer::getEntityTranslucent);
        this.thinArms = thinArms;
        this.parts = root.traverse().filter(part -> !part.isEmpty()).collect(ImmutableList.toImmutableList());
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(48, 64).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create().uv(56, 60).cuboid(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 34).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 50).cuboid(-3.5F, 1.0F, -4.0F, 7.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(33, 0).cuboid(-6.0F, 8.0F, -2.0F, 12.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body_r1 = body.addChild("body_r1", ModelPartBuilder.create().uv(19, 50).cuboid(-0.1F, -0.4F, -1.5F, 2.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 5.0F, -0.1F, 0.0F, 0.0F, 0.5672F));

        ModelPartData body_r2 = body.addChild("body_r2", ModelPartBuilder.create().uv(30, 50).cuboid(-1.9F, -0.4F, -1.5F, 2.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 5.0F, -0.1F, 0.0F, 0.0F, -0.5672F));

        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(38, 34).cuboid(-2.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(25, 34).cuboid(-1.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(21, 17).cuboid(-4.0F, 0.0F, -2.0F, 6.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 17).cuboid(-2.0F, 0.0F, -2.0F, 6.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        return TexturedModelData.of(modelData, 80, 80);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of());
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        ModelPart modelPart = this.getArm(arm);
        if (this.thinArms) {
            float f = 0.5f * (float) (arm == Arm.RIGHT ? 1 : -1);
            modelPart.pivotX += f;
            modelPart.rotate(matrices);
            modelPart.pivotX -= f;
        } else {
            modelPart.rotate(matrices);
        }
    }

    public ModelPart getRandomPart(Random random) {
        return this.parts.get(random.nextInt(this.parts.size()));
    }
}

