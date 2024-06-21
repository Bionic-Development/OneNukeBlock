package de.takacick.onescaryblock.registry.block.entity.renderer;

import de.takacick.onescaryblock.client.item.model.PhantomBlockItemModel;
import de.takacick.onescaryblock.registry.block.entity.PhantomBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class PhantomBlockEntityRenderer implements BlockEntityRenderer<PhantomBlockEntity> {

    private final PhantomBlockItemModel phantomBlockItemModel;

    public PhantomBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.phantomBlockItemModel = new PhantomBlockItemModel(PhantomBlockItemModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(PhantomBlockEntity phantomBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {

        float progress = Math.max((((phantomBlockEntity.getAge() + tickDelta) / phantomBlockEntity.getMaxAge()) - 0.9f) * 4f, 0f);
        progress = ((float) (Math.sin(((progress * 8.5) + 1.5) * Math.PI) + 1f) * 0.5f) * progress * 0.5f;

        this.phantomBlockItemModel.setBlockAngles(
                phantomBlockEntity.getPos(),
                phantomBlockEntity.getAge() + Math.abs(phantomBlockEntity.getPos().asLong()) % 1000,
                tickDelta
        );

        matrixStack.push();
        float scale = 1f + progress * 0.6f;
        matrixStack.scale(-scale, -scale, scale);
        matrixStack.translate(-0.5, -1.501f, 0.5);

        RenderLayer renderLayer = this.phantomBlockItemModel.getLayer(PhantomBlockItemModel.TEXTURE);

        this.phantomBlockItemModel.render(matrixStack,
                vertexConsumerProvider.getBuffer(renderLayer),
                i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f - progress * 0.6f);
        matrixStack.pop();
    }
}

