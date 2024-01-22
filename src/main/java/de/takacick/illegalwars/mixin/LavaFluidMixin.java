package de.takacick.illegalwars.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.takacick.illegalwars.registry.ItemRegistry;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LavaFluid.class)
public abstract class LavaFluidMixin {

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
        if (!original && this.illegalwars$fluid != null && (this.illegalwars$fluid.equals(ItemRegistry.FLOWING_SLUDGE_LIQUID)
                || this.illegalwars$fluid.equals(ItemRegistry.STILL_SLUDGE_LIQUID))
                && getStill().isIn(FluidTags.LAVA)) {
            return true;
        }
        return original;
    }
}