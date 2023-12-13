package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
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
                && playerProperties.isUpgrading()) {
            if (!playerProperties.getBodyPartManager().canUseArms()) {
                info.setReturnValue(false);
            } else if (playerProperties.hasBodyPart(BodyParts.CYBER_CHAINSAWS)
                    && this.player.getMainHandStack().isEmpty()) {
                playerProperties.setCyberChainsawTicks(4);
            }
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void handleBlockBreaking(boolean bl, CallbackInfo info) {
        if (this.player instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()) {
            if (!playerProperties.getBodyPartManager().canUseArms()) {
                info.cancel();
            } else if (playerProperties.hasBodyPart(BodyParts.CYBER_CHAINSAWS)
                    && this.player.getMainHandStack().isEmpty() && bl) {
                playerProperties.setCyberChainsawTicks(4);
            }
        }
    }
}
