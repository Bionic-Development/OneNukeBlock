package de.takacick.raidbase.mixin;

import de.takacick.raidbase.registry.ItemRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {

    @Shadow
    public abstract Fluid getStill();

    @Inject(method = "matchesType", at = @At("RETURN"), cancellable = true)
    public void matchesType(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValue() && ((fluid.equals(ItemRegistry.FLOWING_LIGHTNING_WATER)
                || fluid.equals(ItemRegistry.STILL_LIGHTNING_WATER))
                && this.getStill().isIn(FluidTags.WATER))) {
            info.setReturnValue(true);
        }
    }
}