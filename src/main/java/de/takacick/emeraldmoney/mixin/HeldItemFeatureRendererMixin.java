package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.registry.ItemRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends FeatureRenderer<T, M> {

    @Shadow
    @Final
    private HeldItemRenderer heldItemRenderer;

    public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.VILLAGER_DRILLER)) {
            matrices.push();
            boolean bl = arm == Arm.LEFT;
            if (!transformationMode.equals(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
                ((ModelWithArms)this.getContextModel()).setArmAngle(arm, matrices);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
                matrices.translate((float)(bl ? -1 : 1) / 16.0f, 0.125f, -0.625f);
            }
            this.heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
            matrices.pop();
            info.cancel();
        }
    }
}

