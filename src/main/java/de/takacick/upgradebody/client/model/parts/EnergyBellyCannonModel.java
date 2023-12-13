package de.takacick.upgradebody.client.model.parts;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.client.model.BodyEntityModel;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import de.takacick.upgradebody.registry.bodypart.BodyPartManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EnergyBellyCannonModel extends BodyPartModel {

    public static final Identifier TEXTURE = new Identifier(UpgradeBody.MOD_ID, "textures/entity/bodyparts/energy_belly_cannon.png");

    private final BodyPart bodyPart;
    private final ModelPart root;
    private final ModelPart belly;

    public EnergyBellyCannonModel(BodyPart part, BodyPartManager bodyPartManager) {
        this.bodyPart = part;
        this.root = getModelData(part, bodyPartManager).getChild("body");
        this.belly = this.root.getChild("belly");
    }

    @Override
    public void setAngles(BodyEntityModel bodyEntityModel, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.belly.resetTransform();
        if (livingEntity instanceof PlayerProperties playerProperties
                && playerProperties.isUsingEnergyBellyBlast()) {

            float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();

            this.belly.roll = (playerProperties.getEnergyBellyBlastUsageTicks() + tickDelta) * ((float) Math.PI / 180) * 30;
        }
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

    public static ModelPart getModelData(BodyPart part, BodyPartManager bodyPartManager) {
        float offsetY = 0;
        float pivotY = getPivotY(part, bodyPartManager.getBodyParts());

        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -12.0F + offsetY, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                        .uv(16, 16).cuboid(-5.0F, -13.0F + offsetY, -3.0F, 2.0F, 3.0F, 6.0F, new Dilation(0.0F))
                        .uv(13, 17).cuboid(-5.0F, -1.0F + offsetY, -3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                        .uv(13, 17).mirrored().cuboid(3.0F, -1.0F + offsetY, -3.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(13, 17).cuboid(-5.0F, -1.0F + offsetY, 1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                        .uv(13, 17).mirrored().cuboid(3.0F, -1.0F + offsetY, 1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(16, 16).mirrored().cuboid(3.0F, -13.0F + offsetY, -3.0F, 2.0F, 3.0F, 6.0F, new Dilation(0.0F)).mirrored(false),
                ModelTransform.pivot(0.0F, 24.0F - pivotY, 0.0F));

        ModelPartData belly = body.addChild("belly", ModelPartBuilder.create().uv(0, 17).cuboid(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new Dilation(0.0F))
                .uv(18, 27).cuboid(-3.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(9, 24).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(18, 27).mirrored().cuboid(2.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(9, 24).mirrored().cuboid(-1.0F, 2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, -8.0F, -3.0F));
        return TexturedModelData.of(modelData, 32, 32).createModel();
    }
}
