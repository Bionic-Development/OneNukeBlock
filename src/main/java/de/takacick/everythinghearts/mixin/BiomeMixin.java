package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.EverythingHeartsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {

    @Inject(method = "getSkyColor", at = @At(value = "RETURN"), cancellable = true)
    private void getSkyColor(CallbackInfoReturnable<Integer> info) {
        float f = EverythingHeartsClient.getHeartRainGradient(MinecraftClient.getInstance().getTickDelta());
        if (f > 0.4) {
            info.setReturnValue(0xFF1313);
        }
    }

    @Inject(method = "getFogColor", at = @At(value = "RETURN"), cancellable = true)
    private void getFogColor(CallbackInfoReturnable<Integer> info) {
        float f = EverythingHeartsClient.getHeartRainGradient(MinecraftClient.getInstance().getTickDelta());
        if (f > 0.4) {
            info.setReturnValue(0xFF1313);
        }
    }
}