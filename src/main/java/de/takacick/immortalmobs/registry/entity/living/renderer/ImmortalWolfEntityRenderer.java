package de.takacick.immortalmobs.registry.entity.living.renderer;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.entity.living.ImmortalWolfEntity;
import de.takacick.immortalmobs.registry.entity.living.model.ImmortalWolfEntityModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ImmortalWolfEntityRenderer
        extends MobEntityRenderer<ImmortalWolfEntity, ImmortalWolfEntityModel<ImmortalWolfEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/wolf/wolf.png");
    private static final Identifier EYES = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_wolf_eyes.png");

    public ImmortalWolfEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ImmortalWolfEntityModel<>(context.getPart(EntityModelLayers.WOLF)), 0.5f);
        this.addFeature(new ImmortalEyesFeatureRenderer<>(this, EYES));
        this.addFeature(new WolfCollarFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(ImmortalWolfEntity immortalWolfEntity) {
        return TEXTURE;
    }

    @Override
    protected float getAnimationProgress(ImmortalWolfEntity immortalWolfEntity, float f) {
        return immortalWolfEntity.getTailAngle();
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ImmortalWolfEntity immortalWolfEntity, boolean showBody, boolean translucent, boolean showOutline) {
        return CustomLayers.IMMORTAL_CUTOUT.apply(getTexture(immortalWolfEntity));
    }

    @Environment(value = EnvType.CLIENT)
    public static class WolfCollarFeatureRenderer
            extends FeatureRenderer<ImmortalWolfEntity, ImmortalWolfEntityModel<ImmortalWolfEntity>> {
        private static final Identifier SKIN = new Identifier(ImmortalMobs.MOD_ID, "textures/entity/immortal_wolf_collar.png");

        public WolfCollarFeatureRenderer(FeatureRendererContext<ImmortalWolfEntity, ImmortalWolfEntityModel<ImmortalWolfEntity>> featureRendererContext) {
            super(featureRendererContext);
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ImmortalWolfEntity wolfEntity, float f, float g, float h, float j, float k, float l) {
            if (!wolfEntity.isTamed() || wolfEntity.isInvisible()) {
                return;
            }
            float[] fs = wolfEntity.getCollarColor().getColorComponents();
            renderModel(this.getContextModel(), SKIN, matrixStack, vertexConsumerProvider, i, wolfEntity, fs[0], fs[1], fs[2]);
        }

        protected static <T extends LivingEntity> void renderModel(EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float red, float green, float blue) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEyes(texture));
            model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);
        }
    }
}

