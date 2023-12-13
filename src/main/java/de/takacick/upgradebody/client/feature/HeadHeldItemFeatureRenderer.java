package de.takacick.upgradebody.client.feature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

@Environment(value = EnvType.CLIENT)
public class HeadHeldItemFeatureRenderer
        <T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>>
        extends FeatureRenderer<T, M> {
    private final HeldItemRenderer heldItemRenderer;

    public HeadHeldItemFeatureRenderer(FeatureRendererContext<T, M> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        float m;
        boolean bl = entity.isSleeping();
        boolean bl2 = entity.isBaby();
        matrixStack.push();
        if (bl2) {
            matrixStack.scale(0.75f, 0.75f, 0.75f);
            matrixStack.translate(0.0f, 0.5f, 0.209375f);
        }
        matrixStack.translate(this.getContextModel().head.pivotX / 16.0f, this.getContextModel().head.pivotY / 16.0f, this.getContextModel().head.pivotZ / 16.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(headYaw));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(headPitch));
        matrixStack.translate(0f, -0.135f, -0.6f);

        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        if(itemStack.getItem() instanceof BlockItem) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            matrixStack.translate(0f, -0.535f, 0.1f);
        }

        if (bl) {
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0f));
        }
        this.heldItemRenderer.renderItem(entity, itemStack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumers, light);
        matrixStack.pop();
    }
}

