package de.takacick.secretgirlbase.mixin.leadcuffs;

import de.takacick.secretgirlbase.access.LeadCuffProperties;
import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
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
        if (this.player != null) {
            if (this.player instanceof LeadCuffProperties leadCuffProperties
                    && leadCuffProperties.isLeadCuffed()
                    || this.player.getVehicle() instanceof FireworkTimeBombEntity) {
                if (this.player.getVehicle() instanceof FireworkTimeBombEntity) {
                    this.player.swingHand(Hand.MAIN_HAND);
                }
                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void doItemUse(CallbackInfo info) {
        if (this.player != null) {
            if (this.player instanceof LeadCuffProperties leadCuffProperties
                    && leadCuffProperties.isLeadCuffed() || this.player.getVehicle() instanceof FireworkTimeBombEntity) {
                info.cancel();
            }
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void handleBlockBreaking(boolean bl, CallbackInfo info) {
        if (this.player != null) {
            if (this.player instanceof LeadCuffProperties leadCuffProperties && leadCuffProperties.isLeadCuffed()
                    || this.player.getVehicle() instanceof FireworkTimeBombEntity) {
                info.cancel();
            }
        }
    }
}
