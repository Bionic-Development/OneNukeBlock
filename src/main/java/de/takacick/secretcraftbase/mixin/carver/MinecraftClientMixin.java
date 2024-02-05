package de.takacick.secretcraftbase.mixin.carver;

import de.takacick.secretcraftbase.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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
        if (this.player instanceof PlayerProperties playerProperties
                && playerProperties.getHeartRemovalState().isRunning()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void handleBlockBreaking(boolean bl, CallbackInfo info) {
        if (this.player instanceof PlayerProperties playerProperties
                && playerProperties.getHeartRemovalState().isRunning()) {
            info.cancel();
        }
    }


    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void doItemUse(CallbackInfo info) {
        if (this.player instanceof PlayerProperties playerProperties
                && playerProperties.getHeartRemovalState().isRunning()) {
            info.cancel();
        }
    }
}
