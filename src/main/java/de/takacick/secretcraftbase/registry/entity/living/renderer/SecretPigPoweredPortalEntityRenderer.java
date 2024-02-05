package de.takacick.secretcraftbase.registry.entity.living.renderer;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.registry.entity.living.SecretPigPoweredPortalEntity;
import de.takacick.secretcraftbase.registry.entity.living.model.SecretPigPoweredPortalEntityModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class SecretPigPoweredPortalEntityRenderer extends MobEntityRenderer<SecretPigPoweredPortalEntity, PigEntityModel<SecretPigPoweredPortalEntity>> {
    private static final Identifier TEXTURE = new Identifier(SecretCraftBase.MOD_ID, "textures/entity/secret_pig_powered_portal.png");

    public SecretPigPoweredPortalEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SecretPigPoweredPortalEntityModel<>(SecretPigPoweredPortalEntityModel.getTexturedModelData(Dilation.NONE).createModel()), 0.7f);
        this.addFeature(new SaddleFeatureRenderer<>(this, new PigEntityModel<>(context.getPart(EntityModelLayers.PIG_SADDLE)), new Identifier("textures/entity/pig/pig_saddle.png")));
    }

    @Override
    public void render(SecretPigPoweredPortalEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
       if (this.model instanceof SecretPigPoweredPortalEntityModel<SecretPigPoweredPortalEntity> entityModel) {
            entityModel.setVertexConsumerProvider(vertexConsumerProvider);
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(SecretPigPoweredPortalEntity secretPigPoweredPortalEntity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(SecretPigPoweredPortalEntity secretPigPoweredPortalEntity, Frustum frustum, double d, double e, double f) {
        float tickDelta = MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta();
        if (secretPigPoweredPortalEntity.getMouthHeight(tickDelta) > 0) {
            return true;
        }

        return super.shouldRender(secretPigPoweredPortalEntity, frustum, d, e, f);
    }
}


