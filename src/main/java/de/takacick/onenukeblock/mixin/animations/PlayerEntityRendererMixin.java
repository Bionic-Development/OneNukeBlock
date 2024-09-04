package de.takacick.onenukeblock.mixin.animations;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import de.takacick.onenukeblock.client.entity.feature.AnimatedItemHoldingFeatureRenderer;
import de.takacick.onenukeblock.registry.item.HandheldItem;
import de.takacick.onenukeblock.utils.ArmHelper;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
        this.addFeature(new AnimatedItemHoldingFeatureRenderer(this));
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