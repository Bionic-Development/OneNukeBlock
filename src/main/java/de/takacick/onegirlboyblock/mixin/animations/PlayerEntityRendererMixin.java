package de.takacick.onegirlboyblock.mixin.animations;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import de.takacick.onegirlboyblock.client.entity.feature.AnimatedItemHoldingFeatureRenderer;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import de.takacick.onegirlboyblock.registry.item.HandheldItem;
import de.takacick.onegirlboyblock.utils.ArmHelper;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new AnimatedItemHoldingFeatureRenderer(this));
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"))
    private void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (abstractClientPlayerEntity.isUsingItem() && (abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.BIT_CANNON) ||abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.INFERNO_HAIR_DRYER))) {
            abstractClientPlayerEntity.bodyYaw = abstractClientPlayerEntity.headYaw;
            abstractClientPlayerEntity.prevBodyYaw = abstractClientPlayerEntity.prevHeadYaw;
        }
    }

    @Inject(method = "getPositionOffset(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    private void render(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, CallbackInfoReturnable<Vec3d> info) {
        if(abstractClientPlayerEntity.getVehicle() instanceof TurboBoardEntity) {
            info.setReturnValue(new Vec3d(0, 0.225, 0));
        }
    }

    @Inject(method = "renderArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/PlayerEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER))
    private void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info, @Share("armPitch") LocalFloatRef armPitch) {
        Arm armType = Arm.RIGHT;
        if (!arm.equals(this.model.rightArm)) {
            armType = Arm.LEFT;
        }
        ItemStack itemStack = ArmHelper.getArmStack(player, armType);
        if (itemStack.getItem() instanceof HandheldItem handheldItem && handheldItem.keepFirstPersonPitch(player, itemStack, armType)) {
            if (armType == Arm.RIGHT ? handheldItem.showRightArm(player, itemStack, armType) : handheldItem.showLeftArm(player, itemStack, armType)) {
                armPitch.set(arm.pitch);
            }
        } else {
            armType = armType.getOpposite();
            itemStack = ArmHelper.getArmStack(player, armType);
            if (itemStack.getItem() instanceof HandheldItem handheldItem && handheldItem.keepFirstPersonPitch(player, itemStack, armType)) {
                if (armType == Arm.RIGHT ? handheldItem.showRightArm(player, itemStack, armType) : handheldItem.showLeftArm(player, itemStack, armType)) {
                    armPitch.set(arm.pitch);
                }
            }
        }
    }

    @Inject(method = "renderArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", shift = At.Shift.BEFORE, ordinal = 0))
    private void resetArmPitch(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info, @Share("armPitch") LocalFloatRef armPitch) {
        if (armPitch.get() != 0f) {
            arm.pitch = armPitch.get();
            armPitch.set(sleeve.pitch);
        }
    }

    @Inject(method = "renderArm",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", shift = At.Shift.BEFORE, ordinal = 1))
    private void resetSleevePitch(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo info, @Share("armPitch") LocalFloatRef armPitch) {
        if (armPitch.get() != 0f) {
            sleeve.pitch = armPitch.get();
            armPitch.set(0f);
        }
    }
}