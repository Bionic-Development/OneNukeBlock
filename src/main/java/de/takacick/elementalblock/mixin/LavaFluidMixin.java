package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.LavaFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {

    @Inject(method = "matchesType", at = @At("RETURN"), cancellable = true)
    public void matchesType(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue() && ((fluid.equals(ItemRegistry.FLOWING_LAVA)
                || fluid.equals(ItemRegistry.LAVA)) && ((Object) this == Fluids.LAVA || (Object) this == Fluids.FLOWING_LAVA))) {
            info.setReturnValue(true);
        }
    }
}