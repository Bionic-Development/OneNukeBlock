package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {

    @Inject(method = "matchesType", at = @At("RETURN"), cancellable = true)
    public void matchesType(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue() && ((fluid.equals(ItemRegistry.FLOWING_WATER)
                || fluid.equals(ItemRegistry.WATER) || (fluid.equals(ItemRegistry.TSUNAMIC_FLOWING_WATER)
                || fluid.equals(ItemRegistry.TSUNAMIC_WATER))) && ((Object) this == Fluids.WATER || (Object) this == Fluids.FLOWING_WATER))) {
            info.setReturnValue(true);
        }
    }
}