package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.access.PiglinProperties;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPiglinEntity.class)
public abstract class AbstractPiglinEntityMixin {

    @Inject(method = "shouldZombify", at = @At("HEAD"), cancellable = true)
    public void shouldZombify(CallbackInfoReturnable<Boolean> info) {
        if (this instanceof PiglinProperties piglinProperties && piglinProperties.isUsingPiglinGoldTurret()) {
            info.setReturnValue(false);
        }
    }
}
