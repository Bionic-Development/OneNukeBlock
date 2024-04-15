package de.takacick.onegirlfriendblock.client.renderer;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class HoldingOneBlockRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public static final Identifier TEXTURE = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/girlfriend.png");
    public static final ModelPart HOLDING_BLOCK =  getTexturedModelData().createModel();;
    private final ModelPart holdingBlock;

    public HoldingOneBlockRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
        super(featureRendererContext);
        this.holdingBlock = getTexturedModelData().createModel();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity instanceof PlayerProperties playerProperties && playerProperties.hasOneGirlfriendBlock()) {
            RenderLayer renderLayer;

            MinecraftClient minecraftClient = MinecraftClient.getInstance();

            boolean bl = this.isVisible(entity);
            boolean bl2 = !bl && !entity.isInvisibleTo(minecraftClient.player);
            boolean bl3 = minecraftClient.hasOutline(entity);

            renderLayer = getRenderLayer(bl, bl2, bl3);

            if (renderLayer != null) {
                this.holdingBlock.copyTransform(this.getContextModel().rightArm);
                this.holdingBlock.pitch = (-00f + 37.5f + this.getContextModel().rightArm.pitch) * ((float) Math.PI / 180);
                this.holdingBlock.yaw = 0f;
                this.holdingBlock.roll = 0f;
                this.holdingBlock.pivotY -= 8.5f;
                this.holdingBlock.pivotX += 2.5f;
                this.holdingBlock.pivotZ -= 6.5f;
                this.holdingBlock.render(matrices, vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            }
        }
    }

    @Nullable
    protected RenderLayer getRenderLayer(boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = TEXTURE;
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        }
        if (showBody) {
            return this.getContextModel().getLayer(identifier);
        }
        if (showOutline) {
            return RenderLayer.getOutline(identifier);
        }
        return null;
    }

    protected boolean isVisible(AbstractClientPlayerEntity entity) {
        return !entity.isInvisible();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.of(0.0F, 2.5f, 0.0F, -1.5708F, -1.5708F, 0.0F));

        ModelPartData head = bone.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -14.0F, 3.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 0).cuboid(-12.0F, -14.0F, 3.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.2F, -2.0F, 0.5F));

        ModelPartData body = bone.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-12.0F, -6.0F, 5.5F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 32).cuboid(-12.0F, -6.0F, 5.5F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.2F, -2.0F, 0.0F));

        ModelPartData right_arm = bone.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.2F, -8.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 32).cuboid(-2.2F, -8.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-12.6F, 0.0F, 7.5F));

        ModelPartData left_arm = bone.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(-1.2F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-2.8F, -6.0F, 7.5F));

        ModelPartData left_leg = bone.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-10.0F, -6.0F, 5.5F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 48).cuboid(-10.0F, -6.0F, 5.5F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(2.1F, 10.0F, 0.0F));

        ModelPartData right_leg = bone.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0F, -6.0F, 5.5F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-10.0F, -6.0F, 5.5F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.7F, 10.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
}

