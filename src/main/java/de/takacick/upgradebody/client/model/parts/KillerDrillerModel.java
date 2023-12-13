package de.takacick.upgradebody.client.model.parts;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.client.model.BodyEntityModel;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class KillerDrillerModel extends BodyPartModel {

    public static final Identifier TEXTURE = new Identifier(UpgradeBody.MOD_ID, "textures/entity/bodyparts/killer_driller.png");

    private final BodyPart bodyPart;
    private final ModelPart root;
    private final ModelPart drill;

    public KillerDrillerModel(BodyPart part, BodyPartManager bodyPartManager) {
        this.bodyPart = part;
        this.root = getModelData(part, bodyPartManager).getChild("head");
        this.drill = this.root.getChild("drill");
    }

    @Override
    public void setAngles(BodyEntityModel bodyEntityModel, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.root.resetTransform();

        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();

        this.drill.yaw = (livingEntity.age + tickDelta) * ((float) Math.PI / 180) * 30;
        bodyEntityModel.copy(this.root, bodyPart.getInheritModelPart());
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return TEXTURE;
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

        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-4.0F, -8.0F + offsetY, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                        .uv(0, 26).cuboid(-5.0F, -6.0F + offsetY, -5.0F, 5.0F, 3.0F, 4.0F, new Dilation(0.0F))
                        .uv(0, 34).cuboid(-5.0F, -6.0F + offsetY, -1.0F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, pivotY, 0.0F));

        ModelPartData drill = head.addChild("drill", ModelPartBuilder.create().
                        uv(0, 17).cuboid(-3.0F, -2.0F  + offsetY, -3.0F, 6.0F, 2.0F, 6.0F, new Dilation(0.0F))
                        .uv(19, 26).cuboid(-2.0F, -4.0F + offsetY, -2.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                        .uv(9, 34).cuboid(-1.0F, -6.0F + offsetY, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                        .uv(33, 0).cuboid(-4.0F, -1.0F + offsetY, -4.0F, 8.0F, 0.0F, 8.0F, new Dilation(0.0F))
                        .uv(25, 17).cuboid(-3.0F, -3.0F + offsetY, -3.0F, 6.0F, 0.0F, 6.0F, new Dilation(0.0F))
                        .uv(36, 26).cuboid(-2.0F, -5.0F + offsetY, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        return TexturedModelData.of(modelData, 80, 80).createModel();
    }
}
