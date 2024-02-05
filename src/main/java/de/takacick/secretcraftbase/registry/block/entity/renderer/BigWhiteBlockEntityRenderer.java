package de.takacick.secretcraftbase.registry.block.entity.renderer;

import de.takacick.secretcraftbase.registry.block.entity.BigWhiteBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

public class BigWhiteBlockEntityRenderer implements BlockEntityRenderer<BigWhiteBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public BigWhiteBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    public void render(BigWhiteBlockEntity bigWhiteBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        if (!bigWhiteBlockEntity.isOwner()) {
            return;
        }
        float size = 3f;

        BlockState blockState = Blocks.WHITE_CONCRETE.getDefaultState();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getCutoutMipped());
        BakedModel bakedModel = blockRenderManager.getModels().getModel(blockState);
        long l = blockState.getRenderingSeed(bigWhiteBlockEntity.getPos());
        matrixStack.push();

        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.scale(size, size, size);
        matrixStack.translate(-0.5, -0.5, -0.5);

        blockRenderManager.getModelRenderer().render(bigWhiteBlockEntity.getWorld(), bakedModel, blockState, bigWhiteBlockEntity.getPos(),
                matrixStack, vertexConsumer, false, bigWhiteBlockEntity.getWorld().getRandom(), Math.min(l, 7), OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance() * 2;
    }

    @Override
    public boolean rendersOutsideBoundingBox(BigWhiteBlockEntity bigWhiteBlockEntity) {
        return true;
    }
}
