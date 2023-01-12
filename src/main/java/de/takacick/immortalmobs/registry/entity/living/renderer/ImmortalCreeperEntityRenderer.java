package de.takacick.immortalmobs.registry.entity.living.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.living.ImmortalCreeperEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class ImmortalCreeperEntityRenderer
        extends MobEntityRenderer<ImmortalCreeperEntity, CreeperEntityModel<ImmortalCreeperEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/creeper/creeper.png");
    private static final Identifier EYES = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_creeper_eyes.png");

    public ImmortalCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CreeperEntityModel(context.getPart(EntityModelLayers.CREEPER)), 0.5f);
        this.addFeature(new ImmortalEyesFeatureRenderer<>(this, EYES));
        this.addFeature(new ImmortalFeatureRenderer(this));
    }

    @Override
    protected void scale(ImmortalCreeperEntity creeperEntity, MatrixStack matrixStack, float f) {
        float g = creeperEntity.getClientFuseTime(f);
        float h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.1f;
        g = MathHelper.clamp(g, 0.0f, 1.0f);
        g *= g;
        g *= g;
        float i = (1.0f + g * 0.4f) * h;
        float j = (1.0f + g * 0.1f) / h;
        matrixStack.scale(i, j, i);
    }

    @Override
    protected float getAnimationCounter(ImmortalCreeperEntity creeperEntity, float f) {
        float g = creeperEntity.getClientFuseTime(f);
        if ((int) (g * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return MathHelper.clamp(g, 0.5f, 1.0f);
    }

    @Override
    public Identifier getTexture(ImmortalCreeperEntity immortalCreeperEntity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ImmortalCreeperEntity immortalCreeperEntity, boolean showBody, boolean translucent, boolean showOutline) {
        return CustomLayers.IMMORTAL_CUTOUT.apply(getTexture(immortalCreeperEntity));
    }

    @Environment(value = EnvType.CLIENT)
    public static class ImmortalFeatureRenderer
            extends FeatureRenderer<ImmortalCreeperEntity, CreeperEntityModel<ImmortalCreeperEntity>> {

        private final CreeperEntityModel<ImmortalCreeperEntity> outerModel;
        private static final Identifier TEXTURE = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_creeper_armor.png");

        public ImmortalFeatureRenderer(FeatureRendererContext<ImmortalCreeperEntity, CreeperEntityModel<ImmortalCreeperEntity>> context) {
            super(context);
            this.outerModel = new CreeperEntityModel(CreeperEntityModel.getTexturedModelData(new Dilation(0.28f)).createModel());
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ImmortalCreeperEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
            getContextModel().copyStateTo(outerModel);
            outerModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
            outerModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

            float[] hs = SheepEntity.getRgbColor(DyeColor.PURPLE);
            float r = hs[0];
            float g = hs[1];
            float b = hs[2];

            //     VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(TEXTURE));
            //     outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), r, g, b, 1.0f);
            //    outerModel.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0f), r, g, b, 1.0f);


            float f = (float) ((Entity) entity).age + tickDelta;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEnergySwirl(TEXTURE, this.getEnergySwirlX(f) % 1.0f, f * 0.01f % 1.0f));
            outerModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            outerModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, r, g, b, 1.0f);
        }

        protected float getEnergySwirlX(float partialAge) {
            return partialAge * 0.01f;
        }
    }
}

