package de.takacick.deathmoney.registry.entity.custom.renderer;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.entity.custom.DeathShopPortalEntity;
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
public class DeathShopPortalEntityRenderer extends EntityRenderer<DeathShopPortalEntity> {

    public DeathShopPortalEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(DeathShopPortalEntity deathShopPortalEntity, BlockPos blockPos) {
        return MathHelper.clamp(super.getBlockLight(deathShopPortalEntity, blockPos) + 7, 13, 15);
    }

    @Override
    public void render(DeathShopPortalEntity deathShopPortalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();

        float progress = deathShopPortalEntity.getProgress(g);

        if (progress != 1f) {
            float shake = (float) (Math.cos(deathShopPortalEntity.age * 3.25 * Math.PI * (double) 0.4f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(shake * 4));
        }

        matrixStack.scale(2, 2, 2);
        matrixStack.translate(0, 0.07, 0);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(deathShopPortalEntity.getYaw()));
        matrixStack.translate(-0.5, -2.3125 * (1 - progress), -0.5);
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer()
                .getModels().getModelManager().getModel(new ModelIdentifier(DeathMoney.MOD_ID + ":death_shop_portal#inventory"));
        renderBakedItemQuads(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getTranslucent()), bakedModel.getQuads(null, null, deathShopPortalEntity.getEntityWorld().getRandom()), i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
        super.render(deathShopPortalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(DeathShopPortalEntity deathShopPortalEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    public static void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, int light, int overlay) {
        MatrixStack.Entry entry = matrices.peek();

        for (BakedQuad bakedQuad : quads) {
            vertices.quad(entry, bakedQuad, 1f, 1f, 1f, light, overlay);
        }
    }
}