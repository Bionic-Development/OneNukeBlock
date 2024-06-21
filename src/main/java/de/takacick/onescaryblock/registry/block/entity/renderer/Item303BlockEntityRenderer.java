package de.takacick.onescaryblock.registry.block.entity.renderer;

import de.takacick.onescaryblock.registry.block.entity.Item303BlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

public class Item303BlockEntityRenderer implements BlockEntityRenderer<Item303BlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public Item303BlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    @Override
    public void render(Item303BlockEntity item303BlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();

        Random rotation = Random.create(item303BlockEntity.getAge() + 300 + item303BlockEntity.getPos().asLong());

        rotation.nextDouble();

        for (int x = 0; x < rotation.nextInt(10) + 1; x++) {
            Random random = Random.create((rotation.nextDouble() <= 0.2 ? System.nanoTime() : item303BlockEntity.getAge()) + x + item303BlockEntity.getPos().asLong());
            int finalX = x;
            Registries.BLOCK.getRandom(random).ifPresent(blockReference -> {
                Block block = blockReference.value();
                BlockState blockState = block.getStateManager().getStates().get(random.nextInt(block.getStateManager().getStates().size()));
                RenderLayer renderLayer = RenderLayers.getBlockLayer(blockState);

                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);

                matrixStack.push();
                if (finalX != 0) {
                    matrixStack.translate(0.5, 0, 0.5);

                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float) rotation.nextGaussian() * 0.3f));
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) rotation.nextGaussian() * 0.3f));
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotation((float) rotation.nextGaussian() * 0.3f));
                    matrixStack.translate(-0.5, 0, -0.5);
                    matrixStack.scale((float) (1 + rotation.nextGaussian() * 0.2f), (float) (1 + rotation.nextGaussian() * 0.2f), (float) (1 + rotation.nextGaussian() * 0.2f));
                }
                this.blockRenderManager.renderBlock(blockState,
                        item303BlockEntity.getPos(), item303BlockEntity.getWorld(),
                        matrixStack, vertexConsumer,
                        false, Random.create());
                matrixStack.pop();
            });
        }

        matrixStack.pop();
    }
}

