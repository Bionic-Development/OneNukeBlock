package de.takacick.onenukeblock.registry.block.entity.renderer;

import de.takacick.onenukeblock.client.item.model.SkylandTntBlockItemModel;
import de.takacick.onenukeblock.client.item.renderer.SkylandTntBlockItemRenderer;
import de.takacick.onenukeblock.registry.block.entity.SkylandTntBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SkylandTntBlockEntityRenderer implements BlockEntityRenderer<SkylandTntBlockEntity> {

    public static final Identifier TEXTURE = SkylandTntBlockItemRenderer.TEXTURE;

    private final SkylandTntBlockItemModel skylandTntBlockItemModel;

    public SkylandTntBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.skylandTntBlockItemModel = new SkylandTntBlockItemModel(SkylandTntBlockItemModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(SkylandTntBlockEntity skylandTntBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        matrixStack.push();
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(-0.5, -1.501f, 0.5);

        RenderLayer renderLayer = this.skylandTntBlockItemModel.getLayer(TEXTURE);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
        this.skylandTntBlockItemModel.render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();
    }
}

