package de.takacick.onesuperblock.registry.entity.living.renderer;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.onesuperblock.registry.entity.living.model.ProtoEntityModel;
import de.takacick.superitems.client.CustomLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SuperProtoEntityRenderer extends MobEntityRenderer<WolfEntity, ProtoEntityModel<WolfEntity>> {

    private static final Identifier TEXTURE = new Identifier(OneSuperBlock.MOD_ID, "textures/entity/proto.png");

    public SuperProtoEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ProtoEntityModel<>(ProtoEntityModel.getTexturedModelData().createModel()), 0.5f);
        addFeature(new RainbowFeatureRenderer(this));
    }

    @Override
    protected float getAnimationProgress(WolfEntity wolfEntity, float f) {
        return wolfEntity.getTailAngle();
    }

    @Override
    public Identifier getTexture(WolfEntity wolfEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(WolfEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.getTexture(entity);
        if (translucent) {
            return RenderLayer.getItemEntityTranslucentCull(identifier);
        }
        if (showBody) {
            return CustomLayers.IMMORTAL_CUTOUT.apply(identifier);
        }
        if (showOutline) {
            return RenderLayer.getOutline(identifier);
        }
        return null;
    }

    public static class RainbowFeatureRenderer extends FeatureRenderer<WolfEntity, ProtoEntityModel<WolfEntity>> {

        public RainbowFeatureRenderer(FeatureRendererContext<WolfEntity, ProtoEntityModel<WolfEntity>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, WolfEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            getContextModel().render(matrices, vertexConsumers.getBuffer(SuperRenderLayers.RAINBOW_ENTITY_GLOW.apply(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.35f);
        }
    }
}

