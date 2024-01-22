package de.takacick.illegalwars.registry.entity.living.renderer;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.entity.living.CyberWardenSecurityEntity;
import de.takacick.illegalwars.registry.entity.living.model.CyberWardenSecurityEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CyberWardenSecurityEntityRenderer
        extends MobEntityRenderer<CyberWardenSecurityEntity, CyberWardenSecurityEntityModel<CyberWardenSecurityEntity>> {

    private static final Identifier TEXTURE = new Identifier(IllegalWars.MOD_ID, "textures/entity/cyber_warden_security.png");

    public CyberWardenSecurityEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CyberWardenSecurityEntityModel<>(CyberWardenSecurityEntityModel.getTexturedModelData().createModel()), 0.9f);
        this.addFeature(new CyberWardenSecurityGlowFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(CyberWardenSecurityEntity cyberWardenSecurityEntity) {
        return TEXTURE;
    }

    @Environment(value = EnvType.CLIENT)
    public static class CyberWardenSecurityGlowFeatureRenderer<T extends CyberWardenSecurityEntity>
            extends EyesFeatureRenderer<T, CyberWardenSecurityEntityModel<T>> {
        private static final RenderLayer SKIN = RenderLayer.getEyes(new Identifier(IllegalWars.MOD_ID, "textures/entity/cyber_warden_security_glow.png"));

        public CyberWardenSecurityGlowFeatureRenderer(FeatureRendererContext<T, CyberWardenSecurityEntityModel<T>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            float progress = entity.getChargeProgress(tickDelta);

            if (Math.sin(entity.age) + progress >= 0) {
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.getEyesTexture());
                ((Model) this.getContextModel()).render(matrices, vertexConsumer, 0xF00000, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1f);
            }
        }

        @Override
        public RenderLayer getEyesTexture() {
            return SKIN;
        }
    }


}

