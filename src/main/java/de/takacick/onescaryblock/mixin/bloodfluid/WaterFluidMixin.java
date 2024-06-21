package de.takacick.onescaryblock.mixin.bloodfluid;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.takacick.onescaryblock.registry.ItemRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public abstract class WaterFluidMixin {
    @Shadow
    public abstract Fluid getStill();

    @Unique
    private Fluid onescaryblock$fluid;

    @Inject(method = "matchesType", at = @At("HEAD"))
    public void matchesType(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        this.onescaryblock$fluid = fluid;
    }

    @ModifyReturnValue(method = "matchesType", at = @At("RETURN"))
    public boolean matchesType(boolean original) {
        if (!original && this.onescaryblock$fluid != null && (this.onescaryblock$fluid.equals(ItemRegistry.FLOWING_BLOOD)
                || this.onescaryblock$fluid.equals(ItemRegistry.STILL_BLOOD))
                && getStill().isIn(FluidTags.WATER)) {
            return true;
        }
        return original;
    }
}