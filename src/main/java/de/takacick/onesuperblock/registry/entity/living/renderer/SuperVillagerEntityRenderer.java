package de.takacick.onesuperblock.registry.entity.living.renderer;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.client.render.SuperRenderLayers;
import de.takacick.onesuperblock.registry.entity.living.SuperVillagerEntity;
import de.takacick.superitems.client.CustomLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.VillagerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class SuperVillagerEntityRenderer extends MobEntityRenderer<SuperVillagerEntity, VillagerResemblingModel<SuperVillagerEntity>> {
    private static final Identifier TEXTURE = new Identifier("textures/entity/villager/villager.png");

    public SuperVillagerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new VillagerResemblingModel<>(context.getPart(EntityModelLayers.VILLAGER)), 0.5f);
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
        this.addFeature(new VillagerClothingFeatureRenderer<>(this));
        this.addFeature(new VillagerHeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(SuperVillagerEntity superVillagerEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(SuperVillagerEntity superVillagerEntity, MatrixStack matrixStack, float f) {
        float g = 0.9375f;
        if (superVillagerEntity.isBaby()) {
            g *= 0.5f;
            this.shadowRadius = 0.25f;
        } else {
            this.shadowRadius = 0.5f;
        }
        matrixStack.scale(g, g, g);
    }


    @Environment(value = EnvType.CLIENT)
    public static class VillagerClothingFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
            extends FeatureRenderer<T, M> {

        private static final Identifier TEXTURE = new Identifier(OneSuperBlock.MOD_ID, "textures/entity/super_villager_clothing.png");

        public VillagerClothingFeatureRenderer(FeatureRendererContext<T, M> context) {
            super(context);
        }

        @Override
        public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
            if (livingEntity.isInvisible()) {
                return;
            }

            M entityModel = this.getContextModel();
            ((ModelWithHat) entityModel).setHatVisible(false);
            renderModel(entityModel, TEXTURE, matrixStack, vertexConsumerProvider, i, livingEntity, 1.0f, 1.0f, 1.0f);
            ((ModelWithHat) entityModel).setHatVisible(true);
        }

        protected static <T extends LivingEntity> void renderModel(EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float red, float green, float blue) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(texture));
            model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);
            vertexConsumer = vertexConsumers.getBuffer(SuperRenderLayers.RAINBOW_ENTITY_GLOW.apply(texture));
            model.render(matrices, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0.0f), red, green, blue, 1.0f);
        }
    }
}

