package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SleepingChatScreen.class)
public class SleepingChatScreenMixin {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/SleepingChatScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", shift = At.Shift.BEFORE), cancellable = true)
    private void init(CallbackInfo info) {
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        if (playerEntity != null && playerEntity.getSleepingPosition().isPresent()) {
            if (playerEntity.getWorld().getBlockState(playerEntity.getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED)) {
                info.cancel();
            }
        }
    }

    @Inject(method = "stopSleeping", at = @At("HEAD"), cancellable = true)
    private void stopSleeping(CallbackInfo info) {
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        if (playerEntity != null && playerEntity.getSleepingPosition().isPresent()) {
            if (playerEntity.getWorld().getBlockState(playerEntity.getSleepingPosition().get()).isOf(ItemRegistry.IMAGINED_INFINITY_TRAPPED_BED)) {
                info.cancel();
            }
        }
    }
}
