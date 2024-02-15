package de.takacick.secretgirlbase.registry.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.secretgirlbase.client.shaders.SecretGirlBaseLayers;
import de.takacick.secretgirlbase.client.shaders.vertex.TranslucentVertexConsumer;
import de.takacick.secretgirlbase.registry.block.entity.MagicDisappearingPlatformBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.util.SortedSet;

public class MagicDisappearingPlatformBlockEntityRenderer implements BlockEntityRenderer<MagicDisappearingPlatformBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public MagicDisappearingPlatformBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    public void render(MagicDisappearingPlatformBlockEntity magicDisappearingPlatformBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider x, int light, int j) {
        if (!magicDisappearingPlatformBlockEntity.isOwner()) {
            return;
        }
        float alpha = magicDisappearingPlatformBlockEntity.getAlpha(f);
        if (alpha <= 0) {
            return;
        }

        BlockState blockState = Blocks.WHITE_CONCRETE.getDefaultState();

        long l = blockState.getRenderingSeed(magicDisappearingPlatformBlockEntity.getPos());
        matrixStack.push();

        float size = 3f;
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(size, 1, size);
        matrixStack.translate(-0.5, -0.5, -0.5);

        MatrixStack.Entry entry = matrixStack.peek();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        VertexConsumerProvider.Immediate consumers = VertexConsumerProvider.immediate(tessellator.getBuffer());

        SortedSet<BlockBreakingInfo> set = MinecraftClient.getInstance().worldRenderer.blockBreakingProgressions
                .get(magicDisappearingPlatformBlockEntity.getPos().asLong());

        VertexConsumerProvider vertexConsumerProvider;
        if (set != null && set.last().getStage() >= 0) {
            OverlayVertexConsumer vertexConsumer = new OverlayVertexConsumer(MinecraftClient.getInstance()
                    .getBufferBuilders().getEffectVertexConsumers().getBuffer(ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(set.last().getStage())),
                    entry.getPositionMatrix(), entry.getNormalMatrix(), 1.0f);

            vertexConsumerProvider = layer -> {
                VertexConsumer vertexConsumer2 = consumers.getBuffer(layer);
                if (layer.hasCrumbling()) {
                    return VertexConsumers.union(vertexConsumer, vertexConsumer2);
                }
                return vertexConsumer2;
            };
        } else {
            vertexConsumerProvider = consumers;
        }

        VertexConsumer vertexConsumer;

        if (alpha < 1f) {
            TranslucentVertexConsumer translucentVertexConsumer = new TranslucentVertexConsumer(vertexConsumerProvider
                    .getBuffer(SecretGirlBaseLayers.getPoppyTranslucent()));
            translucentVertexConsumer.setAlpha(alpha);

            vertexConsumer = translucentVertexConsumer;
        } else {
            vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getSolid());
        }
        BakedModel bakedModel = this.blockRenderManager.getModels().getModel(blockState);
        this.blockRenderManager.getModelRenderer().render(magicDisappearingPlatformBlockEntity.getWorld(),
                bakedModel, blockState, magicDisappearingPlatformBlockEntity.getPos(),
                matrixStack, vertexConsumer, false, magicDisappearingPlatformBlockEntity.getWorld().getRandom(),
                Math.min(l, 7), OverlayTexture.DEFAULT_UV);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_EQUAL);
        consumers.draw();
        tessellator.getBuffer().clear();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance() * 2;
    }

    @Override
    public boolean rendersOutsideBoundingBox(MagicDisappearingPlatformBlockEntity magicDisappearingPlatformBlockEntity) {
        return true;
    }
}
