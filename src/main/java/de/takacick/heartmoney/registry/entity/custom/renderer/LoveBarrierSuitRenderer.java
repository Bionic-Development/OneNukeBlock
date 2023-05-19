package de.takacick.heartmoney.registry.entity.custom.renderer;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.ItemRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;

public class LoveBarrierSuitRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public static final ModelPart LOVE_BARRIER_MODEL = getModelData(new Dilation(0.5f), 0.0f).getRoot().createPart(64, 32);
    public static final Identifier LOVE_BARRIER_SUIT = new Identifier(HeartMoney.MOD_ID, "textures/entity/love_barrier_suit.png");
    private final BipedEntityModel<AbstractClientPlayerEntity> bipedEntityModel;

    public LoveBarrierSuitRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext, BipedEntityModel<AbstractClientPlayerEntity> bipedEntityModel) {
        super(featureRendererContext);
        this.bipedEntityModel = bipedEntityModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.LOVE_BARRIER_SUIT)) {
            return;
        }

        float f = (float) entity.age + tickDelta;
        BipedEntityModel<AbstractClientPlayerEntity> entityModel = bipedEntityModel;
        entityModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
        bipedEntityModel.hat.visible = false;
        this.getContextModel().setAttributes(entityModel);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(this.getEnergySwirlTexture(), this.getEnergySwirlX(f) % 1.0f, f * 0.01f % 1.0f));
        entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
    }

    protected float getEnergySwirlX(float partialAge) {
        return partialAge * 0.01f;
    }

    protected Identifier getEnergySwirlTexture() {
        return LOVE_BARRIER_SUIT;
    }


    public static ModelData getModelData(Dilation dilation, float pivotOffsetY) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, dilation), ModelTransform.pivot(0.0f, 0.0f + pivotOffsetY, 0.0f));
        modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 16).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, dilation), ModelTransform.pivot(0.0f, 0.0f + pivotOffsetY, 0.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation), ModelTransform.pivot(-5.0f, 2.0f + pivotOffsetY, 0.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation), ModelTransform.pivot(5.0f, 2.0f + pivotOffsetY, 0.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation), ModelTransform.pivot(-1.9f, 12.0f + pivotOffsetY, 0.0f));
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, dilation), ModelTransform.pivot(1.9f, 12.0f + pivotOffsetY, 0.0f));
        return modelData;
    }
}

