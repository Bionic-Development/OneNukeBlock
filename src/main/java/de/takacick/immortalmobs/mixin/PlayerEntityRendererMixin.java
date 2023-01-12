package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.access.PlayerEntityRendererAccessor;
import de.takacick.immortalmobs.registry.ItemRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
@Implements({@Interface(iface = PlayerEntityRendererAccessor.class, prefix = "immortalmobs$")})
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private boolean immortalmobs$slim = false;

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;<init>(Lnet/minecraft/client/render/entity/feature/FeatureRendererContext;Lnet/minecraft/client/render/entity/model/BipedEntityModel;Lnet/minecraft/client/render/entity/model/BipedEntityModel;)V", shift = At.Shift.BEFORE))
    private void init(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.immortalmobs$slim = slim;
    }

    @Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
    private static void getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> info) {
        ItemStack itemStack = player.getActiveItem();

        if (itemStack.isOf(ItemRegistry.SUPER_SHEAR_SAW)) {
            info.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        } else if (player.getStackInHand(hand).isOf(ItemRegistry.IMMORTAL_CANNON)) {
            info.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
        }
    }

    public boolean immortalmobs$isSlim() {
        return immortalmobs$slim;
    }
}