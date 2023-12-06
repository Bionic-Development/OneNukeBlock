package de.takacick.emeraldmoney.registry.entity.custom.renderer;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.emeraldmoney.registry.entity.custom.model.EmeraldShopPortalEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.List;

@Environment(EnvType.CLIENT)
public class EmeraldShopPortalEntityRenderer extends EntityRenderer<EmeraldShopPortalEntity> {

    private static final Identifier TEXTURE = new Identifier(EmeraldMoney.MOD_ID, "textures/entity/emerald_shop_portal.png");
    private static final SpriteIdentifier PORTAL = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(EmeraldMoney.MOD_ID, "entity/emerald_portal"));

    private final EmeraldShopPortalEntityModel<EmeraldShopPortalEntity> model;

    public EmeraldShopPortalEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new EmeraldShopPortalEntityModel<>();
    }

    @Override
    protected int getBlockLight(EmeraldShopPortalEntity emeraldShopPortalEntity, BlockPos blockPos) {
        return MathHelper.clamp(super.getBlockLight(emeraldShopPortalEntity, blockPos) + 7, 13, 15);
    }

    @Override
    public void render(EmeraldShopPortalEntity emeraldShopPortalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        float progress = emeraldShopPortalEntity.getProgress(g);

        if (progress != 1f) {
            float shake = (float) (Math.cos(emeraldShopPortalEntity.age * 3.25 * Math.PI * (double) 0.4f));
        //    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(shake * 2));
        }

        matrixStack.translate(0, -8 * (1 - progress), 0);
        matrixStack.scale(-0.75f, -0.75f, 0.75f);
        matrixStack.translate(0, -1.501f, 0);
        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(emeraldShopPortalEntity.getYaw()));

        model.render(matrixStack, vertexConsumerProvider.getBuffer(model.getLayer(getTexture(emeraldShopPortalEntity))), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        RenderLayer renderLayer = RenderLayer.getEntityTranslucent(PORTAL.getAtlasId());

        model.getPortal().render(matrixStack, PORTAL.getSprite().getTextureSpecificVertexConsumer(vertexConsumerProvider.getBuffer(renderLayer)), i, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

        matrixStack.pop();
        super.render(emeraldShopPortalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(EmeraldShopPortalEntity diamondShopPortalEntity) {
        return TEXTURE;
    }

    public static void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, int light, int overlay) {
        MatrixStack.Entry entry = matrices.peek();

        for (BakedQuad bakedQuad : quads) {
            vertices.quad(entry, bakedQuad, 1f, 1f, 1f, light, overlay);
        }
    }
}