package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Inject(method = "updateMouse", at = @At("HEAD"), cancellable = true)
    public void updateMouse(CallbackInfo info) {
        if (MinecraftClient.getInstance().getCameraEntity() instanceof PlayerProperties playerProperties) {
            if (playerProperties.getShockedTicks() > 0) {
                this.cursorDeltaX = 0.0;
                this.cursorDeltaY = 0.0;
                info.cancel();
            }
        }
    }

    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;", shift = At.Shift.AFTER), cancellable = true)
    public void onMouseScroll(CallbackInfo info) {
        if (MinecraftClient.getInstance().getCameraEntity() instanceof PlayerProperties playerProperties) {
            if (playerProperties.getShockedTicks() > 0) {
                info.cancel();
            }
        }
    }
}
