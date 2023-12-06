package de.takacick.emeraldmoney.client.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

@Environment(value = EnvType.CLIENT)
public class VillagerRobeEntityModel<T extends LivingEntity> extends BipedEntityModel<T> {

    public VillagerRobeEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation) {
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0f);
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16), ModelTransform.pivot(-1.9f, 12.0f + 0, 0.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 16), ModelTransform.pivot(1.9f, 12.0f + 0, 0.0f));

        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 20).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, dilation.add(0.01f))
                .uv(0, 38).cuboid(-4.0F, -0.002F, -3.0F, 8.0F, 18.0F, 6.0F, dilation.add(0.52f)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(44, 22).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, dilation.add(0.4f)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(44, 22).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, dilation.add(0.4f)).mirrored(false), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

