package de.takacick.illegalwars.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.takacick.illegalwars.registry.block.SludgeLiquidBlock;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract double getFluidHeight(TagKey<Fluid> fluid);

    @Shadow
    public abstract Box getBoundingBox();

    @Shadow
    public abstract World getWorld();

    @Shadow
    protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;

    @Inject(method = "setOnFireFromLava", at = @At("HEAD"), cancellable = true)
    public void setOnFireFromLava(CallbackInfo info) {
        if (getFluidHeight(SludgeLiquidBlock.SLUDGE) > 0.0f) {
            info.cancel();
        }
    }

    @Inject(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;", shift = At.Shift.BEFORE))
    public void updateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> info) {
        if (tag.equals(FluidTags.LAVA)) {
            Box box = this.getBoundingBox().contract(0.001);
            int i = MathHelper.floor(box.minX);
            int j = MathHelper.ceil(box.maxX);
            int k = MathHelper.floor(box.minY);
            int l = MathHelper.ceil(box.maxY);
            int m = MathHelper.floor(box.minZ);
            int n = MathHelper.ceil(box.maxZ);
            double d = 0.0;
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (int p = i; p < j; ++p) {
                for (int q = k; q < l; ++q) {
                    for (int r = m; r < n; ++r) {
                        double e;
                        mutable.set(p, q, r);
                        FluidState fluidState = this.getWorld().getFluidState(mutable);
                        if (!fluidState.isIn(SludgeLiquidBlock.SLUDGE) || !((e = ((float) q + fluidState.getHeight(this.getWorld(), mutable))) >= box.minY))
                            continue;
                        d = Math.max(e - box.minY, d);
                    }
                }
            }

            this.fluidHeight.put(SludgeLiquidBlock.SLUDGE, d);
        }
    }


    @ModifyReturnValue(method = "isWet", at = @At("RETURN"))
    public boolean isWet(boolean original) {
        if (getFluidHeight(SludgeLiquidBlock.SLUDGE) > 0.0f) {
            return true;
        }
        return original;
    }
}