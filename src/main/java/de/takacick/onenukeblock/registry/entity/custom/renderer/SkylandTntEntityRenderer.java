package de.takacick.onenukeblock.registry.entity.custom.renderer;

import de.takacick.onenukeblock.client.item.model.SkylandTntBlockItemModel;
import de.takacick.onenukeblock.client.item.renderer.SkylandTntBlockItemRenderer;
import de.takacick.onenukeblock.registry.entity.custom.SkylandTntEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SkylandTntEntityRenderer extends EntityRenderer<SkylandTntEntity> {

    public static final Identifier TEXTURE = SkylandTntBlockItemRenderer.TEXTURE;

    private final SkylandTntBlockItemModel skylandTntBlockItemModel;

    public SkylandTntEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.skylandTntBlockItemModel = new SkylandTntBlockItemModel(SkylandTntBlockItemModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(SkylandTntEntity skylandTntEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1f, -1f, 1f);
        matrixStack.translate(0, -1.501f, 0);
        int fuse = skylandTntEntity.getFuse();
        if ((float) fuse - g + 1.0f < 10.0f) {
            float h = 1.0f - ((float) fuse - g + 1.0f) / 10.0f;
            h = MathHelper.clamp(h, 0.0f, 1.0f);
            h *= h;
            h *= h;
            float k = 1.0f + h * 0.3f;
            matrixStack.scale(k, k, k);
        }

        RenderLayer renderLayer = this.skylandTntBlockItemModel.getLayer(TEXTURE);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);

        boolean flash = fuse / 5 % 2 == 0;
        int uv = flash ? OverlayTexture.packUv(OverlayTexture.getU(1.0f), 10) : OverlayTexture.DEFAULT_UV;
        this.skylandTntBlockItemModel.render(matrixStack, vertexConsumer, i, uv);
        matrixStack.pop();
        super.render(skylandTntEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(SkylandTntEntity skylandTntEntity) {
        return TEXTURE;
    }
}

