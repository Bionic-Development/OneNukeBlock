package de.takacick.onescaryblock.mixin.bloodfluid;

import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.onescaryblock.registry.block.fluid.BloodFluid;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract double getFluidHeight(TagKey<Fluid> fluid);


    @Shadow
    public abstract boolean isRegionUnloaded();

    @Shadow
    public abstract Box getBoundingBox();

    @Shadow
    public abstract boolean isPushedByFluids();

    @Shadow
    public abstract World getWorld();

    @Shadow
    protected Object2DoubleMap<TagKey<Fluid>> fluidHeight;

    @Shadow
    private EntityDimensions dimensions;

    @Shadow
    public abstract double getX();

    @Shadow
    @Final
    protected Random random;

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    @Nullable
    public abstract LivingEntity getControllingPassenger();

    @Shadow
    public abstract void emitGameEvent(GameEvent event);

    @Inject(method = "onSwimmingStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(D)I", shift = At.Shift.BEFORE), cancellable = true)
    public void onSwimmingStart(CallbackInfo info) {
        if (getFluidHeight(BloodFluid.BLOOD) <= 0) {
            return;
        }

        Entity entity = Objects.requireNonNullElse(this.getControllingPassenger(), (Entity) (Object) this);
        Vec3d vec3d = entity.getVelocity();

        double e;
        double d;
        float h = MathHelper.floor(this.getY());
        int i = 0;
        while ((float) i < 1.0f + this.dimensions.width * 20.0f) {
            d = (this.random.nextDouble() * 2.0 - 1.0) * (double) this.dimensions.width;
            e = (this.random.nextDouble() * 2.0 - 1.0) * (double) this.dimensions.width;
            this.getWorld().addParticle(ParticleRegistry.BLOOD_SPLASH, this.getX() + d, h + 1.0f, this.getZ() + e, vec3d.x, vec3d.y, vec3d.z);
            ++i;
        }
        this.emitGameEvent(GameEvent.SPLASH);
        info.cancel();
    }

    @Inject(method = "checkWaterState", at = @At("HEAD"))
    public void checkWaterState(CallbackInfo info) {
        this.updateFluidHeight(BloodFluid.BLOOD);
    }

    @Unique
    public boolean updateFluidHeight(TagKey<Fluid> tag) {
        if (this.isRegionUnloaded()) {
            return false;
        }
        Box box = this.getBoundingBox().contract(0.001);
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.maxY);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        double d = 0.0;
        boolean bl = this.isPushedByFluids();
        boolean bl2 = false;
        Vec3d vec3d = Vec3d.ZERO;
        int o = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int p = i; p < j; ++p) {
            for (int q = k; q < l; ++q) {
                for (int r = m; r < n; ++r) {
                    double e;
                    mutable.set(p, q, r);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (!fluidState.isIn(tag) || !((e = (double) ((float) q + fluidState.getHeight(this.getWorld(), mutable))) >= box.minY))
                        continue;
                    bl2 = true;
                    d = Math.max(e - box.minY, d);
                    if (!bl) continue;
                    Vec3d vec3d2 = fluidState.getVelocity(this.getWorld(), mutable);
                    if (d < 0.4) {
                        vec3d2 = vec3d2.multiply(d);
                    }
                    vec3d = vec3d.add(vec3d2);
                    ++o;
                }
            }
        }

        this.fluidHeight.put(tag, d);
        return bl2;
    }
}

