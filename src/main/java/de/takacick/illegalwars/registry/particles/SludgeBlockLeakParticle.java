package de.takacick.illegalwars.registry.particles;

import de.takacick.illegalwars.registry.ItemRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

@Environment(value = EnvType.CLIENT)
public class SludgeBlockLeakParticle
        extends SpriteBillboardParticle {
    private final Fluid fluid;
    protected boolean obsidianTear;

    SludgeBlockLeakParticle(ClientWorld world, double x, double y, double z, Fluid fluid) {
        super(world, x, y, z);
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.gravityStrength = 0.06f;
        this.fluid = fluid;
    }

    protected Fluid getFluid() {
        return this.fluid;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getBrightness(float tint) {
        if (this.obsidianTear) {
            return 240;
        }
        return super.getBrightness(tint);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.updateAge();
        if (this.dead) {
            return;
        }
        this.velocityY -= this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.updateVelocity();
        if (this.dead) {
            return;
        }
        this.velocityX *= 0.98f;
        this.velocityY *= 0.98f;
        this.velocityZ *= 0.98f;
        if (this.fluid == Fluids.EMPTY) {
            return;
        }
        BlockPos blockPos = BlockPos.ofFloored(this.x, this.y, this.z);
        FluidState fluidState = this.world.getFluidState(blockPos);
        if (fluidState.getFluid() == this.fluid && this.y < (double) ((float) blockPos.getY() + fluidState.getHeight(this.world, blockPos))) {
            this.markDead();
        }
    }

    protected void updateAge() {
        if (this.maxAge-- <= 0) {
            this.markDead();
        }
    }

    protected void updateVelocity() {
    }

    public static final Vector3f COLOR = Vec3d.unpackRgb(0x2D1E16).toVector3f();

    public static SpriteBillboardParticle createDrippingSludge(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        return new DrippingSludge(world, x, y, z, ItemRegistry.STILL_SLUDGE_LIQUID, ParticleRegistry.FALLING_SLUDGE);
    }

    public static SpriteBillboardParticle createFallingSludge(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        ContinuousFalling blockLeakParticle = new ContinuousFalling(world, x, y, z, ItemRegistry.STILL_SLUDGE_LIQUID, ParticleRegistry.LANDING_SLUDGE);
        blockLeakParticle.setColor(COLOR.x(), COLOR.y(), COLOR.z());
        return blockLeakParticle;
    }

    public static SpriteBillboardParticle createLandingSludge(DefaultParticleType type, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        Landing blockLeakParticle = new Landing(world, x, y, z, ItemRegistry.STILL_SLUDGE_LIQUID);
        blockLeakParticle.setColor(COLOR.x(), COLOR.y(), COLOR.z());
        return blockLeakParticle;
    }

    @Environment(value = EnvType.CLIENT)
    static class Dripping
            extends SludgeBlockLeakParticle {
        private final ParticleEffect nextParticle;

        Dripping(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle) {
            super(world, x, y, z, fluid);
            this.nextParticle = nextParticle;
            this.gravityStrength *= 0.02f;
            this.maxAge = 40;
        }

        @Override
        protected void updateAge() {
            if (this.maxAge-- <= 0) {
                this.markDead();
                this.world.addParticle(this.nextParticle, this.x, this.y, this.z, this.velocityX, this.velocityY, this.velocityZ);
            }
        }

        @Override
        protected void updateVelocity() {
            this.velocityX *= 0.02;
            this.velocityY *= 0.02;
            this.velocityZ *= 0.02;
        }
    }

    @Environment(value = EnvType.CLIENT)
    static class ContinuousFalling
            extends Falling {
        protected final ParticleEffect nextParticle;

        ContinuousFalling(ClientWorld world, double x, double y, double z, Fluid fluid, ParticleEffect nextParticle) {
            super(world, x, y, z, fluid);
            this.nextParticle = nextParticle;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.markDead();
                this.world.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Environment(value = EnvType.CLIENT)
    static class DrippingSludge
            extends Dripping {
        DrippingSludge(ClientWorld clientWorld, double d, double e, double f, Fluid fluid, ParticleEffect particleEffect) {
            super(clientWorld, d, e, f, fluid, particleEffect);
            setColor(COLOR.x(), COLOR.y(), COLOR.z());
        }
    }

    @Environment(value = EnvType.CLIENT)
    static class Landing
            extends SludgeBlockLeakParticle {
        Landing(ClientWorld clientWorld, double d, double e, double f, Fluid fluid) {
            super(clientWorld, d, e, f, fluid);
            this.maxAge = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        }
    }

    @Environment(value = EnvType.CLIENT)
    static class Falling
            extends SludgeBlockLeakParticle {
        Falling(ClientWorld clientWorld, double d, double e, double f, Fluid fluid) {
            this(clientWorld, d, e, f, fluid, (int) (64.0 / (Math.random() * 0.8 + 0.2)));
        }

        Falling(ClientWorld world, double x, double y, double z, Fluid fluid, int maxAge) {
            super(world, x, y, z, fluid);
            this.maxAge = maxAge;
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.markDead();
            }
        }
    }
}

