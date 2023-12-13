package de.takacick.upgradebody.client.model.parts;

import de.takacick.upgradebody.client.model.BodyEntityModel;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class HeadModel extends BodyPartModel {

    private final BodyPart bodyPart;
    private final ModelPart root;

    public HeadModel(BodyPart part, BodyPartManager bodyPartManager) {
        this.bodyPart = part;
        this.root = getModelData(part, bodyPartManager).getChild("head");
    }

    @Override
    public void setAngles(BodyEntityModel bodyEntityModel, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root.resetTransform();

        bodyEntityModel.copy(this.root, bodyPart.getInheritModelPart());
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getRoot() {
        return this.root;
    }

    private static ModelPart getModelData(BodyPart part, BodyPartManager bodyPartManager) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        float offsetY = 0;
        float pivotY = 24.0F - getPivotY(part, bodyPartManager.getBodyParts());

        if (bodyPartManager.getBodyParts().size() <= 1) {
            offsetY = 4;
            pivotY -= 4;
        }

        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F + offsetY, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                        .uv(32, 0).cuboid(-4.0F, -8.0F + offsetY, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)),
                ModelTransform.pivot(0.0F, pivotY, 0.0F));

        return TexturedModelData.of(modelData, 64, 64).createModel();
    }
}
