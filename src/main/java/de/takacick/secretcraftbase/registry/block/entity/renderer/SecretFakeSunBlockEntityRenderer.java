package de.takacick.secretcraftbase.registry.block.entity.renderer;

import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.registry.block.entity.SecretFakeSunBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;

public class SecretFakeSunBlockEntityRenderer implements BlockEntityRenderer<SecretFakeSunBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public SecretFakeSunBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = ctx.getRenderManager();
    }

    public void render(SecretFakeSunBlockEntity secretFakeSunBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int j) {
        if (!secretFakeSunBlockEntity.isOwner()) {
            return;
        }
        float size = 9f;

        BlockState blockState = ItemRegistry.SECRET_FAKE_SUN.getDefaultState();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getCutoutMipped());
        BakedModel bakedModel = blockRenderManager.getModels().getModel(blockState);
        long l = blockState.getRenderingSeed(secretFakeSunBlockEntity.getPos());
        matrixStack.push();

        matrixStack.translate(0.5, 0.99999, 0.5);
        matrixStack.scale(size, size, size);
        matrixStack.translate(-0.5, 0, -0.5);

        blockRenderManager.getModelRenderer().render(secretFakeSunBlockEntity.getWorld(), bakedModel, blockState, secretFakeSunBlockEntity.getPos(),
                matrixStack, vertexConsumer, false, secretFakeSunBlockEntity.getWorld().getRandom(), Math.min(l, 7), OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance() * 2;
    }

    @Override
    public boolean rendersOutsideBoundingBox(SecretFakeSunBlockEntity secretFakeSunBlockEntity) {
        return true;
    }
}
