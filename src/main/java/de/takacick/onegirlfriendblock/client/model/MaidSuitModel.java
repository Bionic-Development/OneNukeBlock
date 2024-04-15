package de.takacick.onegirlfriendblock.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;

public class MaidSuitModel extends PlayerEntityModel<AbstractClientPlayerEntity> {

    public MaidSuitModel(ModelPart root) {
        super(root, false);
        this.child = false;
    }

    public void refreshOverlayState() {
        this.leftPants.copyTransform(this.leftLeg);
        this.rightPants.copyTransform(this.rightLeg);
        this.leftSleeve.copyTransform(this.leftArm);
        this.rightSleeve.copyTransform(this.rightArm);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = PlayerEntityModel.getTexturedModelData(new Dilation(0.01f), false);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation.NONE.add(0.51F)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation.NONE.add(0.7F)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        return TexturedModelData.of(modelData, 64, 64);
    }
}
