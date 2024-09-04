package de.takacick.onenukeblock.mixin.nuclearwater;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onenukeblock.registry.ItemRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {
    @Shadow
    public abstract Fluid getStill();

    @ModifyReturnValue(method = "matchesType", at = @At("RETURN"))
    public boolean matchesType(boolean original, @Local(argsOnly = true) Fluid fluid) {
        if (!original) {
            if ((fluid.equals(ItemRegistry.FLOWING_NUCLEAR_WATER)
                    || fluid.equals(ItemRegistry.STILL_NUCLEAR_WATER))
                    && getStill().isIn(FluidTags.WATER)) {
                return true;
            }
        }
        return original;
    }
}