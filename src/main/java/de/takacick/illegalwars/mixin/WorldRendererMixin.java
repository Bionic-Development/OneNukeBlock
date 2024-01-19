package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.registry.block.entity.PieLauncherBlockEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 634123) {

            if (data == 0) {
                if (this.world.getBlockEntity(blockPos) instanceof PieLauncherBlockEntity pieLauncherBlockEntity) {
                    pieLauncherBlockEntity.getShootState().start(pieLauncherBlockEntity.getAge());
                }
            }

            info.cancel();
        }
    }

}
