package de.takacick.onenukeblock.registry.block.entity.renderer;

import de.takacick.onenukeblock.client.item.model.BladedTntBlockItemModel;
import de.takacick.onenukeblock.client.item.renderer.BladedTntBlockItemRenderer;
import de.takacick.onenukeblock.registry.block.entity.BladedTntBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BladedTntBlockEntityRenderer implements BlockEntityRenderer<BladedTntBlockEntity> {

    public static final Identifier TEXTURE = BladedTntBlockItemRenderer.TEXTURE;

    private final BladedTntBlockItemModel bladedTntBlockItemModel;

    public BladedTntBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.bladedTntBlockItemModel = new BladedTntBlockItemModel(BladedTntBlockItemModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(BladedTntBlockEntity bladedTntBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(-0.5, -1.501f, 0.5);

        RenderLayer renderLayer = this.bladedTntBlockItemModel.getLayer(TEXTURE);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
        this.bladedTntBlockItemModel.render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
    }
}

