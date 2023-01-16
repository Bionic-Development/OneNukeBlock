package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.access.PlayerProperties;
import de.takacick.imagineanything.registry.item.HeadItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void doAttack(CallbackInfoReturnable<Boolean> info) {
        if (player instanceof PlayerProperties playerProperties) {
            ItemStack stack = player.getMainHandStack().getItem() instanceof HeadItem
                    ? player.getMainHandStack() : player.getOffHandStack();
            if (stack.getItem() instanceof HeadItem || playerProperties.getHeadRemovalState().isRunning()) {
                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void handleBlockBreaking(boolean bl, CallbackInfo info) {
        if (player instanceof PlayerProperties playerProperties) {
            ItemStack stack = player.getMainHandStack().getItem() instanceof HeadItem
                    ? player.getMainHandStack() : player.getOffHandStack();
            if (stack.getItem() instanceof HeadItem || playerProperties.getHeadRemovalState().isRunning()) {
                info.cancel();
            }
        }
    }

    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof PlayerProperties playerProperties && (playerProperties.hasTelekinesisFlight() || playerProperties.hasIronManForcefield())) {
            info.setReturnValue(true);
        }
    }
}
