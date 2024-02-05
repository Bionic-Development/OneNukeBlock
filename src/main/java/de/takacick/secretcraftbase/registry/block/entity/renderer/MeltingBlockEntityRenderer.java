package de.takacick.secretcraftbase.registry.block.entity.renderer;

import de.takacick.secretcraftbase.client.shaders.vertex.TranslucentVertexConsumer;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.block.entity.MeltingBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.random.Random;

public class MeltingBlockEntityRenderer implements BlockEntityRenderer<MeltingBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public MeltingBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(MeltingBlockEntity meltingBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        if (meltingBlockEntity.getBlockState() != null) {
            RenderLayer renderLayer = RenderLayer.getTranslucent();

            TranslucentVertexConsumer translucentVertexConsumer = new TranslucentVertexConsumer(vertexConsumerProvider.getBuffer(renderLayer));
            translucentVertexConsumer.setAlpha(1 - meltingBlockEntity.getMeltingProgress(tickDelta));

            this.blockRenderManager.renderBlock(meltingBlockEntity.getBlockState(),
                    meltingBlockEntity.getPos(), meltingBlockEntity.getWorld(),
                    matrixStack, translucentVertexConsumer,
                    false, Random.create());

            if (meltingBlockEntity.getBlockState().isIn(BlockTags.DIAMOND_ORES)) {
                BlockState blockState = ItemRegistry.DIAMOND_ORE_CHUNKS.getDefaultState();

                renderLayer = RenderLayers.getBlockLayer(blockState);

                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);

                this.blockRenderManager.renderBlock(blockState,
                        meltingBlockEntity.getPos(), meltingBlockEntity.getWorld(),
                        matrixStack, vertexConsumer,
                        false, Random.create());
            }
        }
    }
}

