package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.registry.ItemRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow
    protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);

    @Shadow
    public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    @Inject(method = "getUsingItemHandRenderType", at = @At("HEAD"), cancellable = true)
    private static void getUsingItemHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> info) {
        if (player.getMainHandStack().isOf(ItemRegistry.IMMORTAL_CANNON) || player.getOffHandStack().isOf(ItemRegistry.IMMORTAL_CANNON))  {
            info.setReturnValue(HeldItemRenderer.HandRenderType.RENDER_MAIN_HAND_ONLY);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {

        if (item.isOf(ItemRegistry.IMMORTAL_CANNON)) {
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            matrices.push();
            int i;
            boolean bl2 = CrossbowItem.isCharged(item);
            boolean bl3 = arm == Arm.RIGHT;
            int n = i = bl3 ? 1 : -1;

            float f = -0.4f * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
            float g = 0.2f * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2));
            float h = -0.2f * MathHelper.sin(swingProgress * (float) Math.PI);
            matrices.translate((float) i * f, g, h);
            this.applyEquipOffset(matrices, arm, equipProgress);

            if (bl) {
                matrices.translate((float) i * -0.641864f, 0.0, 0.0);
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) i * 10.0f));
            }

            this.renderItem(player, item, bl3 ? ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND : ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND, !bl3, matrices, vertexConsumers, light);
            info.cancel();
        }
    }
}