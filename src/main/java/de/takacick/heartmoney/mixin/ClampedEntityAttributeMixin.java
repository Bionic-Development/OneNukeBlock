package de.takacick.heartmoney.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClampedEntityAttribute.class)
public class ClampedEntityAttributeMixin extends EntityAttribute {

    protected ClampedEntityAttributeMixin(String translationKey, double fallback) {
        super(translationKey, fallback);
    }

    @Inject(method = "getMaxValue", at = @At(value = "RETURN"), cancellable = true)
    public void getMaxValue(CallbackInfoReturnable<Double> info) {
        if (getTranslationKey().equalsIgnoreCase("attribute.name.generic.max_health")) {
            info.setReturnValue(Double.MAX_VALUE);
        }
    }

    @Inject(method = "clamp", at = @At(value = "HEAD"), cancellable = true)
    public void clamp(double value, CallbackInfoReturnable<Double> info) {
        if (getTranslationKey().equalsIgnoreCase("attribute.name.generic.max_health")) {
            info.setReturnValue(value);
        }
    }
}