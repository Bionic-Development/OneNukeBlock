package de.takacick.onenukeblock.mixin.diamondhazmatarmor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClampedEntityAttribute.class)
public class ClampedEntityAttributeMixin extends EntityAttribute {

    @Shadow @Final private double minValue;

    @Shadow
    public double getMaxValue() {
        return 0;
    }

    @Unique
    private boolean onenukeblock$armorAttribute = false;

    protected ClampedEntityAttributeMixin(String translationKey, double fallback) {
        super(translationKey, fallback);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void init(String translationKey, double fallback, double min, double max, CallbackInfo info) {
        if (getTranslationKey().equalsIgnoreCase("attribute.name.generic.armor")) {
            this.onenukeblock$armorAttribute = true;
        }
    }

    @ModifyReturnValue(method = "getMaxValue", at = @At(value = "RETURN"))
    public double modifyMaxValue(double original) {
        if (this.onenukeblock$armorAttribute) {
            return Math.max(original, 46);
        }
        return original;
    }

    @ModifyExpressionValue(method = "clamp", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D"))
    public double clamp(double original, @Local(argsOnly = true) double value) {
        if (this.onenukeblock$armorAttribute) {
            return Math.max(original, MathHelper.clamp(value, this.minValue, getMaxValue()));
        }
        return original;
    }
}