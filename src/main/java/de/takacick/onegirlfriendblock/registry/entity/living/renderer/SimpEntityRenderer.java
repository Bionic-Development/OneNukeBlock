package de.takacick.onegirlfriendblock.registry.entity.living.renderer;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.entity.living.SimpEntity;
import de.takacick.onegirlfriendblock.registry.entity.living.model.SimpEntityModel;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SimpEntityRenderer extends MobEntityRenderer<SimpEntity, PlayerEntityModel<SimpEntity>> {
    private static final Identifier TEXTURE = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/simp.png");
    private static final Identifier KNIGHT = new Identifier(OneGirlfriendBlock.MOD_ID, "textures/entity/simp_knight.png");

    public SimpEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new SimpEntityModel(context.getModelLoader().getModelPart(EntityModelLayers.PLAYER), false), 0.3F);
        this.addFeature(new ArmorFeatureRenderer<>(this, new ArmorEntityModel<>(context.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)), new ArmorEntityModel<>(context.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
        this.addFeature(new StuckArrowsFeatureRenderer<>(context, this));
        this.addFeature(new HeadFeatureRenderer<>(this, context.getModelLoader(), context.getHeldItemRenderer()));
        this.addFeature(new ElytraFeatureRenderer<>(this, context.getModelLoader()));
        this.addFeature(new TridentRiptideFeatureRenderer<>(this, context.getModelLoader()));
    }

    @Override
    public void render(SimpEntity simpEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        setModelPose(simpEntity);
        super.render(simpEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private void setModelPose(SimpEntity simpEntity) {
        PlayerEntityModel<SimpEntity> playerEntityModel = this.getModel();
        if (simpEntity.isSpectator()) {
            playerEntityModel.setVisible(false);
            playerEntityModel.head.visible = true;
            playerEntityModel.hat.visible = true;
        } else {
            playerEntityModel.setVisible(true);
            playerEntityModel.sneaking = simpEntity.isInSneakingPose();
        }
    }

    public Identifier getTexture(SimpEntity simpEntity) {
        return simpEntity.isTamed() ? KNIGHT : TEXTURE;
    }

    @Override
    protected void scale(SimpEntity simpEntity, MatrixStack matrices, float amount) {
        matrices.scale(0.9375f, 0.9375f, 0.9375f);
    }

    public static class HeldItemFeatureRenderer<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms & ModelWithHead> extends net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer<T, M> {
        private final HeldItemRenderer heldItemRenderer;

        public HeldItemFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext, HeldItemRenderer heldItemRenderer) {
            super(featureRendererContext, heldItemRenderer);
            this.heldItemRenderer = heldItemRenderer;
        }

        protected void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
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
            heldItemRenderer.renderItem(entity, stack, ModelTransformationMode.HEAD, false, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}
