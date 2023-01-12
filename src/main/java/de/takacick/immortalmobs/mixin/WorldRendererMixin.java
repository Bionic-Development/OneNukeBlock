package de.takacick.immortalmobs.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos pos, int data, CallbackInfo info) {

        if (eventId == 831298212) {
            for (int k = 0; k < 15; ++k) {
                world.addParticle(ParticleTypes.TOTEM_OF_UNDYING,
                        pos.getX() + 0.5f + 0.6 * world.getRandom().nextGaussian(),
                        pos.getY() + 0.5f + 0.6 * world.getRandom().nextGaussian(),
                        pos.getZ() + 0.5f + 0.6 * world.getRandom().nextGaussian(), world.getRandom().nextGaussian() * 0.1, world.getRandom().nextDouble() * 0.1, world.getRandom().nextGaussian() * 0.1);
            }
            info.cancel();
        }
    }
}
