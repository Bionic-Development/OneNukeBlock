package de.takacick.onescaryblock.registry.entity.living.feature;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.entity.living.BloodManEntity;
import de.takacick.onescaryblock.registry.entity.living.model.BloodManEntityModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class BloodManFeatureRenderer extends FeatureRenderer<BloodManEntity, BloodManEntityModel> {

    public static final SpriteIdentifier TEXTURE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(OneScaryBlock.MOD_ID, "block/blood_flow"));

    public BloodManFeatureRenderer(FeatureRendererContext<BloodManEntity, BloodManEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, BloodManEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        BipedEntityModel<BloodManEntity> entityModel = getContextModel();
        entityModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
        this.getContextModel().copyBipedStateTo(entityModel);
        VertexConsumer vertexConsumer = TEXTURE.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(TEXTURE.getAtlasId())));

        entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.7f);
    }
}

