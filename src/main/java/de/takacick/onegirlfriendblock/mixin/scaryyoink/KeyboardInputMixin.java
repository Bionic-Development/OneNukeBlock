package de.takacick.onegirlfriendblock.mixin.scaryyoink;

import de.takacick.onegirlfriendblock.registry.entity.living.SimpYoinkEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(boolean slowDown, float f, CallbackInfo info) {
        if (MinecraftClient.getInstance().player != null
                && MinecraftClient.getInstance().player.getVehicle() instanceof SimpYoinkEntity) {
            sneaking = false;
        }
    }
}