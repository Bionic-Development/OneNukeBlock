package de.takacick.onesuperblock.registry.entity.living.renderer;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.superitems.client.CustomLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.WardenEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SuperFiedWardenEntityRenderer extends MobEntityRenderer<WardenEntity, WardenEntityModel<WardenEntity>> {

    private static final Identifier TEXTURE = new Identifier(OneSuperBlock.MOD_ID, "textures/entity/super_fied_warden.png");

    public SuperFiedWardenEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WardenEntityModel<>(ctx.getPart(EntityModelLayers.WARDEN)), 0.9f);
        addFeature(new RainbowFeatureRenderer(this));
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(WardenEntity wardenEntity, boolean showBody, boolean translucent, boolean showOutline) {
        Identifier identifier = this.getTexture(wardenEntity);
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

    @Override
    protected void scale(WardenEntity wardenEntity, MatrixStack matrices, float amount) {
        matrices.scale(0.8f, 0.8f, 0.8f);
        super.scale(wardenEntity, matrices, amount);
    }

    @Override
    public Identifier getTexture(WardenEntity wardenEntity) {
        return TEXTURE;
    }

    public static class RainbowFeatureRenderer extends FeatureRenderer<WardenEntity, WardenEntityModel<WardenEntity>> {

        public RainbowFeatureRenderer(FeatureRendererContext<WardenEntity, WardenEntityModel<WardenEntity>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, WardenEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            getContextModel().render(matrices, vertexConsumers.getBuffer(SuperRenderLayers.RAINBOW_ENTITY_GLOW.apply(getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.35f);
        }
    }
}

