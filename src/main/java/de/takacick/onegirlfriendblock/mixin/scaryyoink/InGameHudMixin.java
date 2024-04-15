package de.takacick.onegirlfriendblock.mixin.scaryyoink;

import de.takacick.onegirlfriendblock.registry.entity.living.SimpYoinkEntity;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "getRiddenEntity", at = @At("RETURN"), cancellable = true)
    public void getRiddenEntity(CallbackInfoReturnable<LivingEntity> info) {
        if (info.getReturnValue() instanceof SimpYoinkEntity) {
            info.setReturnValue(null);
        }
    }
}
