package de.takacick.onenukeblock.registry.block.entity.renderer;

import de.takacick.onenukeblock.client.item.model.OneNukeBlockItemModel;
import de.takacick.onenukeblock.client.item.renderer.OneNukeBlockItemRenderer;
import de.takacick.onenukeblock.registry.block.entity.NukeOneBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class NukeOneBlockBlockEntityRenderer implements BlockEntityRenderer<NukeOneBlockEntity> {

    public static final Identifier TEXTURE = OneNukeBlockItemRenderer.TEXTURE;

    private final OneNukeBlockItemModel nukeBlockItemModel;

    public NukeOneBlockBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.nukeBlockItemModel = new OneNukeBlockItemModel(OneNukeBlockItemModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(NukeOneBlockEntity nukeOneBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        matrixStack.push();
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(-0.5, -1.501f, 0.5);
        float fuse = nukeOneBlockEntity.isIgnited() ? Math.max(nukeOneBlockEntity.getFuse() - tickDelta, 0) : 0;

        RenderLayer renderLayer = this.nukeBlockItemModel.getLayer(TEXTURE);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
        this.nukeBlockItemModel.setAngles(nukeOneBlockEntity, fuse, tickDelta);
        this.nukeBlockItemModel.setBaseVisible(false);
        this.nukeBlockItemModel.render(matrixStack, vertexConsumer, light, overlay);
        this.nukeBlockItemModel.setBaseVisible(true);

        boolean flash = (int) fuse / 5 % 2 == 0 && nukeOneBlockEntity.isIgnited();
        int uv = flash ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : overlay;

        this.nukeBlockItemModel.renderTnt(matrixStack, vertexConsumer, light, uv, -1);
        if(nukeOneBlockEntity.isIgnited()) {
            this.nukeBlockItemModel.renderLights(matrixStack, vertexConsumerProvider);
        }
        matrixStack.pop();
    }
}

