package de.takacick.elementalblock.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;

public class LavaBionicEntityModel<T extends LivingEntity> extends PlayerEntityModel<T> {

    public LavaBionicEntityModel(ModelPart root, boolean thinArms) {
        super(root, thinArms);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("ear", ModelPartBuilder.create().uv(24, 0), ModelTransform.NONE);
        modelPartData.addChild("cloak", ModelPartBuilder.create().uv(0, 0), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(0, 17).cuboid(-3.0F, -13.0F, -3.0F, 6.0F, 5.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 48).mirrored().cuboid(-4.0F, -8.0F, -5.0F, 4.0F, 5.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        modelPartData.addChild("hat", ModelPartBuilder.create().uv(0, 0)
                .cuboid(0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), ModelTransform.pivot(0.0F, -3.5F, 0.0F));

        modelPartData.addChild("body", ModelPartBuilder.create().uv(40, 48).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData jacket = modelPartData.addChild("jacket", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(16, 32).mirrored().cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

        ModelPartData right_sleeve = modelPartData.addChild("right_sleeve", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 32).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

        ModelPartData left_sleeve = modelPartData.addChild("left_sleeve", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

        ModelPartData right_pants = modelPartData.addChild("right_pants", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(24, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

        ModelPartData left_pants = modelPartData.addChild("left_pants", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Iterables.concat(super.getBodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
    }
}
