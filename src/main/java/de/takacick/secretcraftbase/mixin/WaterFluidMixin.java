package de.takacick.secretcraftbase.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.takacick.secretcraftbase.registry.ItemRegistry;
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
    private Fluid illegalwars$fluid;

    @Inject(method = "matchesType", at = @At("HEAD"))
    public void matchesType(Fluid fluid, CallbackInfoReturnable<Boolean> info) {
        this.illegalwars$fluid = fluid;
    }

    @ModifyReturnValue(method = "matchesType", at = @At("RETURN"))
    public boolean matchesType(boolean original) {
        if (!original && this.illegalwars$fluid != null && (this.illegalwars$fluid.equals(ItemRegistry.FLOWING_MAGIC_WELL_WATER)
                || this.illegalwars$fluid.equals(ItemRegistry.STILL_MAGIC_WELL_WATER))
                && getStill().isIn(FluidTags.WATER)) {
            return true;
        }
        return original;
    }
}