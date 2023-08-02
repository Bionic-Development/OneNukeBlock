package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo info) {
        if (MinecraftClient.getInstance().getCameraEntity() instanceof PlayerProperties playerProperties
                && (playerProperties.getShockedTicks() > 0)) {
            this.pressingForward = false;
            this.pressingBack = false;
            this.pressingLeft = false;
            this.pressingRight = false;
            this.movementForward = 0f;
            this.movementSideways = 0f;
            this.jumping = false;
            this.sneaking = false;
            info.cancel();
        }
    }
}

