package de.takacick.secretcraftbase.registry.entity.living.model;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.client.shaders.SecretCraftBaseLayers;
import de.takacick.secretcraftbase.registry.entity.living.SecretPigPoweredPortalEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class SecretPigPoweredPortalEntityModel<T extends SecretPigPoweredPortalEntity>
        extends PigEntityModel<T> {

    private static final SpriteIdentifier PORTAL = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(SecretCraftBase.MOD_ID, "entity/nether_portal"));

    private ModelPart head;
    private ModelPart upperHead;
    private ModelPart yaw;
    private ModelPart lever;
    private ModelPart handle;
    private ModelPart portal;
    private ModelPart topPortal;
    private ModelPart bottomPortal;

    private VertexConsumerProvider vertexConsumerProvider;

    private float height = 0f;

    public SecretPigPoweredPortalEntityModel(ModelPart root) {
        super(root);

        this.head = root.getChild("head");
        this.upperHead = this.head.getChild("upper_head");
        this.yaw = this.head.getChild("yaw");
        this.lever = root.getChild("body").getChild("lever");
        this.handle = this.lever.getChild("handle");

        this.portal = getPortalTexturedModelData().createModel().getChild("head");
        this.topPortal = this.portal.getChild("top_portal");
        this.bottomPortal = this.portal.getChild("bottom_portal");
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation) {
        ModelData modelData = QuadrupedEntityModel.getModelData(6, dilation);
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData body = modelPartData.getChild("body");

        ModelPartData lever = body.addChild("lever", ModelPartBuilder.create(), ModelTransform.pivot(13.0F, 0.0F, -8.0F));

        ModelPartData handle = lever.addChild("handle", ModelPartBuilder.create().uv(5, 42).cuboid(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-19.0F, -2.0F, 5.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData base = lever.addChild("base", ModelPartBuilder.create().uv(39, 35).cuboid(-21.0F, -5.0F, 1.0F, 3.0F, 6.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, -6.0F));

        ModelPartData upper_head = head.addChild("upper_head", ModelPartBuilder.create().uv(16, 16).cuboid(-2.0F, 0.5F, -2.75F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-4.0F, -3.5F, -1.75F, 8.0F, 7.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, -7.25F));

        ModelPartData yaw = head.addChild("yaw", ModelPartBuilder.create().uv(0, 50).cuboid(-12.0F, -1.0F, 3.0F, 8.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 3.0F, -11.0F));
        ModelPartData lower_head = head.addChild("lower_head", ModelPartBuilder.create().uv(0, 30).cuboid(-12.0F, -9.0F, -7.0F, 8.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 12.0F, -2.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();

        float height = entity.getMouthHeight(tickDelta);

        this.yaw.resetTransform();
        if (height > 0) {
            this.yaw.visible = true;
            this.yaw.scale(new Vector3f(0, entity.getMouthHeight(tickDelta) - 1, 0));
        } else {
            this.yaw.visible = false;
        }

        this.height = height;

        this.upperHead.resetTransform();
        this.upperHead.translate(new Vector3f(0, -height, 0));

        if (entity.isPowered()) {
            this.handle.yaw = 0.7854F;
        } else {
            this.handle.yaw = -0.7854F;
        }

        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        if (!this.child) {
            this.yaw.visible = false;
            this.getHeadParts().forEach(headPart -> headPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));
            this.yaw.visible = true;
            this.getBodyParts().forEach(bodyPart -> bodyPart.render(matrices, vertices, light, overlay, red, green, blue, alpha));

            if (this.height > 0) {
                matrices.push();
                this.head.rotate(matrices);
                matrices.push();
                for (int i = 0; i < this.height / 16; i++) {
                    float mouthHeight = Math.min(this.height - i * 16, 16);
                    this.yaw.resetTransform();
                    this.yaw.scale(new Vector3f(0, mouthHeight - 1, 0));
                    this.yaw.render(matrices, vertices, light, overlay, red, green, blue, alpha);

                    matrices.translate(0, -mouthHeight / 16f, 0);
                }
                matrices.pop();

                RenderLayer renderLayer = SecretCraftBaseLayers.getEntityNetherPortal(PORTAL.getAtlasId());

                VertexConsumer vertexConsumer = PORTAL.getSprite().getTextureSpecificVertexConsumer(vertexConsumerProvider.getBuffer(renderLayer));

                for (int i = 0; i < this.height / 8; i++) {
                    float mouthHeight = Math.min(this.height - i * 8, 8);

                    if (mouthHeight > 6) {
                        this.topPortal.render(matrices, vertexConsumer, 15728880, overlay, red, green, blue, alpha);
                    }
                    this.bottomPortal.render(matrices, vertexConsumer, 15728880, overlay, red, green, blue, alpha);

                    matrices.translate(0, -mouthHeight / 16f, 0);
                }

                matrices.pop();
            }

        } else {
            super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }

    public void setVertexConsumerProvider(VertexConsumerProvider vertexConsumerProvider) {
        this.vertexConsumerProvider = vertexConsumerProvider;
    }

    public static TexturedModelData getPortalTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, -6.0F));

        ModelPartData top_portal = head.addChild("top_portal", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -8.0F, -12.0F, 7.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 3.0F, 3.975F));

        ModelPartData bottom_portal = head.addChild("bottom_portal", ModelPartBuilder.create().uv(0, 4).cuboid(-6.0F, -3.0F, 0.0F, 7.0F, 4.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 2.0F, -8.025F));
        return TexturedModelData.of(modelData, 16, 8);
    }
}

