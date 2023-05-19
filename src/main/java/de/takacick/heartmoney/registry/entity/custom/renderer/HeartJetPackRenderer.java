package de.takacick.heartmoney.registry.entity.custom.renderer;

import de.takacick.heartmoney.registry.ItemRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

public class HeartJetPackRenderer<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    private final HeldItemRenderer heldItemRenderer;

    public HeartJetPackRenderer(FeatureRendererContext<T, M> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack itemStack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
        if (!itemStack.isOf(ItemRegistry.HEART_JET_PACK)) {
            return;
        }

        matrixStack.push();
        matrixStack.translate(0, -0.35, 0);
        if (livingEntity.isSneaking()) {
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(42.0F));
            matrixStack.translate(0.0D, 0.0D, -0.5D);
        }
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        matrixStack.scale(0.625f, -0.625f, -0.625f);
        heldItemRenderer.renderItem(livingEntity, itemStack, ModelTransformation.Mode.HEAD, false, matrixStack, vertexConsumerProvider, i);
        matrixStack.pop();
    }
}

