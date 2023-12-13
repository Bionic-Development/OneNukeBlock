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

import java.util.HashMap;
import java.util.stream.IntStream;

public class CyberChainsawsModel extends BodyPartModel {

    public static final Identifier TEXTURE = new Identifier(UpgradeBody.MOD_ID, "textures/entity/bodyparts/cyber_chainsaw/cyber_chainsaw_1.png");
    private static final HashMap<Integer, Identifier> TEXTURES = new HashMap<>();

    static {
        IntStream.rangeClosed(0, 6).forEach(value -> {
            TEXTURES.put(value, new Identifier(UpgradeBody.MOD_ID, "textures/entity/bodyparts/cyber_chainsaw/cyber_chainsaw_" + (value + 1) + ".png"));
        });
    }

    private final BodyPart bodyPart;
    private final ModelPart root;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private boolean animatedChainsaws = false;
    private int textureId = 0;

    public CyberChainsawsModel(BodyPart part, BodyPartManager bodyPartManager) {
        this.bodyPart = part;
        this.root = getModelData(part, bodyPartManager);
        this.right_arm = this.root.getChild("right_arm");
        this.left_arm = this.root.getChild("left_arm");
    }

    @Override
    public void setAngles(BodyEntityModel bodyEntityModel, AbstractClientPlayerEntity livingEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        PlayerProperties playerProperties = (PlayerProperties) livingEntity;
        this.right_arm.resetTransform();
        this.left_arm.resetTransform();
        bodyEntityModel.copy(this.right_arm, "right_arm");
        bodyEntityModel.copy(this.left_arm, "left_arm");
        if (playerProperties.isUsingCyberSlice()) {
            this.right_arm.pitch = -70f * ((float) Math.PI / 180);
            this.right_arm.yaw = -10.5f * ((float) Math.PI / 180);
            this.left_arm.pitch = -70f * ((float) Math.PI / 180);
            this.left_arm.yaw = 10.5f * ((float) Math.PI / 180);
        }

        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();

        this.animatedChainsaws = playerProperties.hasCyberChainsawAnimation();

        if (this.animatedChainsaws) {
            this.textureId = (int) (tickDelta * 6f);
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }

    @Override
    public Identifier getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity) {
        return TEXTURES.getOrDefault(this.textureId, TEXTURE);
    }

    public ModelPart getRightArm() {
        return this.right_arm;
    }

    public ModelPart getLeftArm() {
        return this.left_arm;
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

        modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(19, 33).cuboid(0.0F, 8.0F + offsetY, -2.0F, 2.0F, 11.0F, 3.0F, new Dilation(0.0F))
                        .uv(0, 0).cuboid(1.0F, 7.0F + offsetY, -4.0F, 0.0F, 14.0F, 6.0F, new Dilation(0.0F))
                        .uv(13, 0).cuboid(-1.0F, -2.0F + offsetY, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                        .uv(0, 21).cuboid(-1.5F, 7.0F + offsetY, -2.5F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F))
                        .uv(30, 33).cuboid(3.5F, 8.0F + offsetY, -1.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                        .uv(21, 21).cuboid(1.0F, -3.0F + offsetY, -3.0F, 3.0F, 5.0F, 6.0F, new Dilation(0.0F))
                        .uv(0, 33).cuboid(1.0F, 3.0F + offsetY, -3.0F, 3.0F, 1.0F, 6.0F, new Dilation(0.0F)),
                ModelTransform.pivot(6.0F, 13.0F - pivotY, 0.0F));

        modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(19, 33).mirrored().cuboid(-2.0F, 8.0F + offsetY, -2.0F, 2.0F, 11.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(0, 0).mirrored().cuboid(-1.0F, 7.0F + offsetY, -4.0F, 0.0F, 14.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(13, 0).mirrored().cuboid(-3.0F, -2.0F + offsetY, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(0, 21).mirrored().cuboid(-3.5F, 7.0F + offsetY, -2.5F, 5.0F, 4.0F, 5.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(30, 33).mirrored().cuboid(-5.5F, 8.0F + offsetY, -1.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(21, 21).mirrored().cuboid(-4.0F, -3.0F + offsetY, -3.0F, 3.0F, 5.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
                        .uv(0, 33).cuboid(-4.0F, 3.0F, -3.0F, 3.0F, 1.0F, 6.0F, new Dilation(0.0F)),
                ModelTransform.pivot(-6.0F, 13.0F - pivotY, 0.0F));
        return TexturedModelData.of(modelData, 48, 48).createModel();
    }
}
