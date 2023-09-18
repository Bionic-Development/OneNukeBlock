package de.takacick.onesuperblock.registry.entity.living.renderer;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.onesuperblock.registry.entity.living.SuperWitherEntity;
import de.takacick.onesuperblock.registry.entity.living.model.SuperWitherEntityModel;
import de.takacick.superitems.client.CustomLayers;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SuperWitherEntityRenderer extends MobEntityRenderer<SuperWitherEntity, SuperWitherEntityModel<SuperWitherEntity>> {
    public static final SpriteIdentifier TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(OneSuperBlock.MOD_ID, "entity/super_wither"));

    public SuperWitherEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SuperWitherEntityModel<>(SuperWitherEntityModel.getTexturedModelData(Dilation.NONE).createModel()), 1.0f);
        addFeature(new RainbowFeatureRenderer(this));
    }

    @Override
    protected int getBlockLight(SuperWitherEntity witherEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(SuperWitherEntity witherEntity) {
        return TEXTURE.getAtlasId();
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(SuperWitherEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.getTexture(entity);
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        }
        if (showBody) {
            return this.model.getLayer(identifier);
        }
        if (showOutline) {
            return RenderLayer.getOutline(identifier);
        }
        return null;
    }

    public static VertexConsumer getVertexConsumer(VertexConsumerProvider vertexConsumers, RenderLayer renderLayer) {
        return TEXTURE.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(renderLayer));
    }

    @Override
    protected void scale(SuperWitherEntity witherEntity, MatrixStack matrixStack, float f) {
        float g = 2.0f;
        int i = witherEntity.getInvulnerableTimer();
        if (i > 0) {
            g -= ((float) i - f) / 220.0f * 0.5f;
        }
        matrixStack.scale(g, g, g);
    }

    public static class RainbowFeatureRenderer extends FeatureRenderer<SuperWitherEntity, SuperWitherEntityModel<SuperWitherEntity>> {
        private static final Identifier TEXTURE = new Identifier(OneSuperBlock.MOD_ID, "textures/entity/super_wither_command_block.png");
        private static final Identifier ARMOR = new Identifier(OneSuperBlock.MOD_ID, "textures/entity/super_wither_armor.png");
        private final SuperWitherEntityModel<SuperWitherEntity> armor = new SuperWitherEntityModel<>(SuperWitherEntityModel.getTexturedModelData(new Dilation(0.02f)).createModel());

        public RainbowFeatureRenderer(FeatureRendererContext<SuperWitherEntity, SuperWitherEntityModel<SuperWitherEntity>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SuperWitherEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            if (entity.shouldRenderOverlay()) {
                armor.animateModel(entity, limbAngle, limbDistance, tickDelta);
                this.getContextModel().copyStateTo(armor);
                armor.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
                armor.render(matrices, getVertexConsumer(vertexConsumers, SuperRenderLayers.RAINBOW_ENTITY_GLOW.apply(ARMOR)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.55f);
            }

            getContextModel().transformCommandBlock(matrices);

            getContextModel().getCommandBlock().render(matrices, vertexConsumers.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

            getContextModel().getCommandBlock().render(matrices, vertexConsumers.getBuffer(SuperRenderLayers.RAINBOW_ENTITY.apply(TEXTURE)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.85f);
        }
    }
}

