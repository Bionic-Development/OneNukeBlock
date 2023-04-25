package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow
    private double cursorDeltaY;

    @Shadow
    private double cursorDeltaX;

    @Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getMouseSensitivity()Lnet/minecraft/client/option/SimpleOption;", shift = At.Shift.AFTER), cancellable = true)
    public void updateMouse(CallbackInfo info) {
        if (MinecraftClient.getInstance().player instanceof PlayerProperties playerProperties && playerProperties.getHeartTransformTicks() > 0) {
            this.cursorDeltaY = 0;
            this.cursorDeltaX = 10f;
        }
    }
}

