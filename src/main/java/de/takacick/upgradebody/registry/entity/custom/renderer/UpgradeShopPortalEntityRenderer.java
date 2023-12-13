package de.takacick.upgradebody.registry.entity.custom.renderer;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.UpgradeBodyClient;
import de.takacick.upgradebody.registry.entity.custom.UpgradeShopPortalEntity;
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
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class UpgradeShopPortalEntityRenderer extends EntityRenderer<UpgradeShopPortalEntity> {

    public UpgradeShopPortalEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLight(UpgradeShopPortalEntity upgradeShopPortalEntity, BlockPos blockPos) {
        return super.getBlockLight(upgradeShopPortalEntity, blockPos);
    }

    @Override
    public void render(UpgradeShopPortalEntity upgradeShopPortalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0, 1.5, 0);
        matrixStack.push();

        float progress = upgradeShopPortalEntity.getProgress(g);

        matrixStack.scale(2f * progress, 2f * progress, 2f * progress);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(upgradeShopPortalEntity.getYaw()));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(720 * progress));
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(g + upgradeShopPortalEntity.age));

        matrixStack.translate(-0.5, -0.5, -0.5);
        BakedModel bakedModel = MinecraftClient.getInstance().getItemRenderer()
                .getModels().getModelManager().getModel(new ModelIdentifier(UpgradeBody.MOD_ID, "upgrade_shop_portal_border", "inventory"));
        renderBakedItemQuads(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getTranslucent()), bakedModel.getQuads(null, null,
                upgradeShopPortalEntity.getEntityWorld().getRandom()), i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        matrixStack.translate(-0.5, -0.5, -0.5);
        bakedModel = MinecraftClient.getInstance().getItemRenderer()
                .getModels().getModelManager().getModel(new ModelIdentifier(UpgradeBody.MOD_ID, "upgrade_shop_portal_inside", "inventory"));
        renderBakedItemQuads(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getTranslucent()), bakedModel.getQuads(null, null,
                upgradeShopPortalEntity.getEntityWorld().getRandom()), i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
        matrixStack.pop();
        super.render(upgradeShopPortalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(UpgradeShopPortalEntity upgradeShopPortalEntity) {
        return PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;
    }

    public static void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, int light, int overlay) {
        Vector3f vector3f = Vec3d.unpackRgb(UpgradeBodyClient.getColor()).toVector3f();

        MatrixStack.Entry entry = matrices.peek();

        for (BakedQuad bakedQuad : quads) {
            if (bakedQuad.hasColor()) {
                vertices.quad(entry, bakedQuad, vector3f.x(), vector3f.y(), vector3f.z(), 15728880, overlay);
            } else {
                vertices.quad(entry, bakedQuad, 1f, 1f, 1f, light, overlay);
            }
        }
    }
}