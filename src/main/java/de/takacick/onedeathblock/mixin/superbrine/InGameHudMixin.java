package de.takacick.onedeathblock.mixin.superbrine;

import de.takacick.onedeathblock.registry.entity.living.SuperbrineEntity;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    protected abstract LivingEntity getRiddenEntity();

    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    public void renderMountHealth(MatrixStack matrices, CallbackInfo info) {
        if (getRiddenEntity() instanceof SuperbrineEntity) {
            info.cancel();
        }
    }
}
