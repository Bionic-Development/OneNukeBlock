package de.takacick.heartmoney.registry.entity.custom.renderer;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.entity.custom.HeartShopPortalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HeartShopPortalEntityRenderer extends EntityRenderer<HeartShopPortalEntity> {

    public HeartShopPortalEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(HeartShopPortalEntity heartShopPortalEntity, BlockPos blockPos) {
        return MathHelper.clamp(super.getBlockLight(heartShopPortalEntity, blockPos) + 7, 13, 15);
    }

    @Override
    public void render(HeartShopPortalEntity heartShopPortalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        float progress = heartShopPortalEntity.getScaleProgress(g);

        matrixStack.scale(6 * progress, 6 * progress, 6 * progress);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(heartShopPortalEntity.getYaw()));
        matrixStack.translate(-0.5, 1f - progress, -0.5);
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer()
                .getModels().getModelManager().getModel(new ModelIdentifier(HeartMoney.MOD_ID + ":heart_shop_portal#inventory"));
        renderBakedItemQuads(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getTranslucent()), bakedModel.getQuads(null, null, heartShopPortalEntity.getEntityWorld().getRandom()), i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
        super.render(heartShopPortalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(HeartShopPortalEntity heartShopPortalEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    public static void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, int light, int overlay) {
        MatrixStack.Entry entry = matrices.peek();

        for (BakedQuad bakedQuad : quads) {
            vertices.quad(entry, bakedQuad, 1f, 1f, 1f, light, overlay);
        }
    }
}