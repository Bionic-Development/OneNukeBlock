package de.takacick.upgradebody.client.model.parts;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.client.model.BodyEntityModel;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TankTracksModel extends BodyPartModel {

    public static final Identifier TEXTURE = new Identifier(UpgradeBody.MOD_ID, "textures/entity/bodyparts/tank_tracks.png");

    private final BodyPart bodyPart;
    private final ModelPart root;
    private final ModelPart frontWheel;
    private final ModelPart centerWheel;
    private final ModelPart hindWheel;

    public TankTracksModel(BodyPart part, BodyPartManager bodyPartManager) {
        this.bodyPart = part;
        this.root = getModelData(part, bodyPartManager).getChild("body");
        this.frontWheel = this.root.getChild("front_wheel");
        this.centerWheel = this.root.getChild("center_wheel");
        this.hindWheel = this.root.getChild("hind_wheel");
    }

    @Override
    public void setAngles(BodyEntityModel bodyEntityModel, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        frontWheel.pitch = limbAngle * 0.3662f;
        centerWheel.pitch = limbAngle * 0.3662f;
        hindWheel.pitch = limbAngle * 0.3662f;
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
        float offsetY = 0;
        float pivotY = getPivotY(part, bodyPartManager.getBodyParts());

        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(28, 25).mirrored().cuboid(6.8F, 7.0F + offsetY, -8.5F, 1.0F, 5.0F, 17.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 0).cuboid(-3.0F, 7.0F + offsetY, -8.5F, 10.0F, 5.0F, 17.0F, new Dilation(0.0F))
                .uv(28, 25).cuboid(-4.0F, 7.0F + offsetY, -8.5F, 1.0F, 5.0F, 17.0F, new Dilation(0.0F))
                .uv(9, 38).cuboid(-0.1F, 0.0F + offsetY, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 12.0F - pivotY, 0.0F));

        ModelPartData front_wheel = body.addChild("front_wheel", ModelPartBuilder.create().uv(0, 24).mirrored().cuboid(4.9F, -1.5F, -1.5F + offsetY, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 24).cuboid(-5.9F, -1.5F + offsetY, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, 9.5F, -5.0F));

        ModelPartData center_wheel = body.addChild("center_wheel", ModelPartBuilder.create().uv(0, 24).mirrored().cuboid(4.9F, -1.5F + offsetY, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 24).cuboid(-5.9F, -1.5F + offsetY, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, 9.5F, 0.0F));

        ModelPartData hind_wheel = body.addChild("hind_wheel", ModelPartBuilder.create().uv(0, 24).mirrored().cuboid(4.9F, -1.5F + offsetY, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 24).cuboid(-5.9F, -1.5F + offsetY, -1.5F, 1.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, 9.5F, 5.0F));

        return TexturedModelData.of(modelData, 64, 64).createModel();
    }
}
