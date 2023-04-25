package de.takacick.everythinghearts.registry.entity.living.renderer;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.registry.entity.living.RevivedPlayerEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class RevivedPlayerEntityRenderer extends MobEntityRenderer<RevivedPlayerEntity, PlayerEntityModel<RevivedPlayerEntity>> {
    private static final Identifier TEXTURE = new Identifier(EverythingHearts.MOD_ID, "textures/entity/revived_player.png");

    public RevivedPlayerEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel<>(context.getModelLoader().getModelPart(EntityModelLayers.PLAYER), false), 0.3F);

        this.addFeature(new ArmorFeatureRenderer<>(this, new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)), new BipedEntityModel<>(context.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR))));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new StuckArrowsFeatureRenderer<>(context, this));
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer<>(this, context.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer<>(this, context.getModelLoader()));
    }

    public Identifier getTexture(RevivedPlayerEntity revivedPlayerEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(RevivedPlayerEntity revivedPlayerEntity, MatrixStack matrices, float amount) {
        matrices.scale(0.9375f, 0.9375f, 0.9375f);
    }

    public static class HeldItemFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms & ModelWithHead> extends net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer<T, M> {
        private final HeldItemRenderer heldItemRenderer;

        public HeldItemFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, HeldItemRenderer heldItemRenderer) {
            super(featureRendererContext, heldItemRenderer);
            this.heldItemRenderer = heldItemRenderer;
        }

        protected void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
            if (stack.isOf(Items.SPYGLASS) && entity.getActiveItem() == stack && entity.handSwingTicks == 0) {
                this.renderSpyglass(entity, stack, arm, matrices, vertexConsumers, light);
            } else {
                super.renderItem(entity, stack, transformationMode, arm, matrices, vertexConsumers, light);
            }
        }

        private void renderSpyglass(LivingEntity entity, ItemStack stack, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
            matrices.push();
            ModelPart modelPart = this.getContextModel().getHead();
            float f = modelPart.pitch;
            modelPart.pitch = MathHelper.clamp(modelPart.pitch, -0.5235988F, 1.5707964F);
            modelPart.rotate(matrices);
            modelPart.pitch = f;
            HeadFeatureRenderer.translate(matrices, false);
            boolean bl = arm == Arm.LEFT;
            matrices.translate((bl ? -2.5F : 2.5F) / 16.0F, -0.0625D, 0.0D);
            heldItemRenderer.renderItem(entity, stack, ModelTransformation.Mode.HEAD, false, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}
