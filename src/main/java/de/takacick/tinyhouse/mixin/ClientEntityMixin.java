package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.LivingProperties;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ClientEntityMixin {

    @Inject(method = "isFrozen", at = @At("HEAD"), cancellable = true)
    public void isFrozen(CallbackInfoReturnable<Boolean> info) {
        if (this instanceof LivingProperties livingProperties && livingProperties.hasFrozenBody()) {
            info.setReturnValue(true);
        }
    }
}