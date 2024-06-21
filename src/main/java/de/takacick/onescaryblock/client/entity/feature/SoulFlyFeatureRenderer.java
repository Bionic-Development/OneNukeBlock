package de.takacick.onescaryblock.client.entity.feature;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.PlayerProperties;
import de.takacick.onescaryblock.client.entity.model.SoulFlyModel;
import de.takacick.onescaryblock.client.shader.OneScaryBlockLayers;
import de.takacick.onescaryblock.client.shader.vertex.UvShiftVertexConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.function.Function;

public class SoulFlyFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = new Identifier(OneScaryBlock.MOD_ID, "textures/entity/soul_fly.png");
    public static final SpriteIdentifier SOUL_FLAME_SPRITE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(OneScaryBlock.MOD_ID, "entity/soul_fly_flames"));
    private final SoulFlyModel<T> soulFlyModel;
    private final SoulFlyModel<T> soulFlameModel;

    public SoulFlyFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);

        this.soulFlyModel = new SoulFlyModel<>(SoulFlyModel.getTexturedModelData().createModel());
        this.soulFlameModel = new SoulFlyModel<>(SoulFlyModel.getTexturedFireModelData().createModel());
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity instanceof PlayerProperties playerProperties && playerProperties.getSoulFragmentHelper().isRunning()) {
            float progress = playerProperties.getSoulFragmentHelper().getProgress(tickDelta);
            render(matrices, getContextModel(), vertexConsumers, animationProgress, light, progress);
        }
    }

    public void render(MatrixStack matrices, BipedEntityModel<?> bipedEntityModel, VertexConsumerProvider vertexConsumerProvider, float animationProgress, int light, float damageProgress) {
        boolean visible = bipedEntityModel.rightLeg.visible;
        bipedEntityModel.rightLeg.visible = false;
        bipedEntityModel.rightLeg.render(matrices, vertexConsumerProvider.getBuffer(bipedEntityModel.getLayer(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)), 0,
                OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f,
                1f);
        bipedEntityModel.rightLeg.visible = visible;

        matrices.scale(1.001f, 1.001f, 1.001f);

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        matrices.push();
        RenderSystem.applyModelViewMatrix();

        Vector3f color = Vec3d.unpackRgb(2138367).toVector3f();

        RenderSystem.setShaderColor(color.x(), color.y(), color.z(), 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.polygonOffset(-3.0f, -3.0f);
        RenderSystem.enablePolygonOffset();
        RenderSystem.disableCull();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        this.soulFlyModel.copyFromBipedState(this.getContextModel());

        this.soulFlyModel.render(matrices, bufferBuilder, light,
                OverlayTexture.DEFAULT_UV,
                color.x(), color.y(), color.z(),
                1f);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.enableCull();
        RenderSystem.polygonOffset(0.0f, 0.0f);
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        matrices.pop();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);

        VertexConsumer vertexConsumer = getVertexConsumer(vertexConsumerProvider, OneScaryBlockLayers::getSoulFlame);

        this.soulFlameModel.copyFromBipedState(this.getContextModel());
        this.soulFlameModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, color.x(), color.y(), color.z(), 1f);
    }

    public static VertexConsumer getVertexConsumer(VertexConsumerProvider vertexConsumers, Function<Identifier, RenderLayer> layerFunction) {
        return SOUL_FLAME_SPRITE.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(layerFunction.apply(SOUL_FLAME_SPRITE.getAtlasId())));
    }
}

