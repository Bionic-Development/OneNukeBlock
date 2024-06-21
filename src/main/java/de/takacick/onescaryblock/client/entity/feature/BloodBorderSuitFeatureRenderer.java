package de.takacick.onescaryblock.client.entity.feature;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.BloodBorderProperties;
import de.takacick.onescaryblock.client.shader.vertex.UvShiftVertexConsumer;
import de.takacick.onescaryblock.registry.item.BloodBorderSuit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class BloodBorderSuitFeatureRenderer<T extends LivingEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {

    public static final Identifier TEXTURE = new Identifier(OneScaryBlock.MOD_ID, "textures/entity/blood_border_suit.png");

    public BloodBorderSuitFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof BloodBorderSuit) {
            float progress = 0f;
            if (entity instanceof BloodBorderProperties bloodBorderProperties) {
                progress = bloodBorderProperties.getBloodBorderSuitHelper().getProgress(tickDelta);
            }

            render(matrices, getContextModel(), vertexConsumers, animationProgress, light, progress);
        }
    }

    public static void render(MatrixStack matrices, BipedEntityModel<?> bipedEntityModel, VertexConsumerProvider vertexConsumerProvider, float animationProgress, int light, float damageProgress) {
        boolean visible = bipedEntityModel.rightArm.visible;
        bipedEntityModel.rightArm.visible = false;
        bipedEntityModel.rightArm.render(matrices, vertexConsumerProvider.getBuffer(bipedEntityModel.getLayer(TEXTURE)), 0,
                OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f,
                1f);
        bipedEntityModel.rightArm.visible = visible;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        matrices.push();
        RenderSystem.applyModelViewMatrix();


        float[] color = lerpColor(Vec3d.unpackRgb(0x820A0A).toVector3f(), Vec3d.unpackRgb(2138367).toVector3f(), damageProgress);

        RenderSystem.setShaderColor(color[0], color[1], color[2], 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.polygonOffset(-3.0f, -3.0f);
        RenderSystem.enablePolygonOffset();
        RenderSystem.disableCull();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        matrices.scale(1.001f, 1.001f, 1.001f);

        bipedEntityModel.render(matrices, new UvShiftVertexConsumer(bufferBuilder, 0, animationProgress * 0.01f), light,
                OverlayTexture.DEFAULT_UV,
                color[0], color[1], color[2],
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
    }

    public static void renderModel(MatrixStack matrices, ModelPart model, VertexConsumerProvider vertexConsumerProvider, float animationProgress, int light, float damageProgress) {
        boolean visible = model.visible;
        model.visible = false;
        model.render(matrices, vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), 0,
                OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        model.visible = visible;

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        matrices.push();
        RenderSystem.applyModelViewMatrix();

        float[] color = lerpColor(Vec3d.unpackRgb(0x820A0A).toVector3f(), Vec3d.unpackRgb(2138367).toVector3f(), damageProgress);

        RenderSystem.setShaderColor(color[0], color[1], color[2], 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.polygonOffset(-3.0f, -3.0f);
        RenderSystem.enablePolygonOffset();
        RenderSystem.disableCull();

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        matrices.scale(1.001f, 1.001f, 1.001f);

        model.render(matrices, new UvShiftVertexConsumer(bufferBuilder, 0, animationProgress * 0.01f), light,
                OverlayTexture.DEFAULT_UV,
                color[0], color[1], color[2],
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
    }

    public static float[] lerpColor(Vector3f start, Vector3f end, float progress) {
        float r = start.x() * (1 - progress) + end.x() * progress;
        float g = start.y() * (1 - progress) + end.y() * progress;
        float b = start.z() * (1 - progress) + end.z() * progress;

        return new float[]{r, g, b};
    }
}

