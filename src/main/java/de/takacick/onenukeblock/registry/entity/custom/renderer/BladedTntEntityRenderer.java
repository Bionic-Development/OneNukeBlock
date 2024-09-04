package de.takacick.onenukeblock.registry.entity.custom.renderer;

import de.takacick.onenukeblock.client.item.model.BladedTntBlockItemModel;
import de.takacick.onenukeblock.client.item.renderer.BladedTntBlockItemRenderer;
import de.takacick.onenukeblock.registry.entity.custom.BladedTntEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class BladedTntEntityRenderer extends EntityRenderer<BladedTntEntity> {

    public static final Identifier TEXTURE = BladedTntBlockItemRenderer.TEXTURE;

    private final BladedTntBlockItemModel bladedTntBlockItemModel;

    public BladedTntEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.bladedTntBlockItemModel = new BladedTntBlockItemModel(BladedTntBlockItemModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(BladedTntEntity bladedTntEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(0, -1.501f, 0);
        int fuse = bladedTntEntity.getFuse();
        if ((float) fuse - g + 1.0f < 10.0f) {
            float h = 1.0f - ((float) fuse - g + 1.0f) / 10.0f;
            h = MathHelper.clamp(h, 0.0f, 1.0f);
            h *= h;
            h *= h;
            float k = 1.0f + h * 0.3f;
            matrixStack.scale(k, k, k);
        }

        RenderLayer renderLayer = this.bladedTntBlockItemModel.getLayer(TEXTURE);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);

        boolean flash = fuse / 5 % 2 == 0;
        int uv = flash ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : OverlayTexture.DEFAULT_UV;
        this.bladedTntBlockItemModel.render(matrixStack, vertexConsumer, i, uv);
        matrixStack.pop();
        super.render(bladedTntEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(BladedTntEntity bladedTntEntity) {
        return TEXTURE;
    }
}

