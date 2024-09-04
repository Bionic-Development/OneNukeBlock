package de.takacick.onenukeblock.registry.particles.water;

import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class NuclearCurrentDownParticle extends SpriteBillboardParticle {

    private float accelerationAngle;

    NuclearCurrentDownParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
        this.maxAge = (int) (Math.random() * 60.0) + 30;
        this.collidesWithWorld = false;
        this.velocityX = 0.0;
        this.velocityY = -0.05;
        this.velocityZ = 0.0;
        this.setBoundingBoxSpacing(0.02f, 0.02f);
        this.scale *= this.random.nextFloat() * 0.6f + 0.2f;
        this.gravityStrength = 0.002f;
        Vector3f color = NuclearWaterFluid.COLOR;
        this.setColor(color.x(), color.y(), color.z());
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        float f = 0.6f;
        this.velocityX += (0.6f * MathHelper.cos(this.accelerationAngle));
        this.velocityZ += (0.6f * MathHelper.sin(this.accelerationAngle));
        this.velocityX *= 0.07;
        this.velocityZ *= 0.07;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        if (!this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isIn(FluidTags.WATER) || this.onGround) {
            this.markDead();
        }
        this.accelerationAngle += 0.08f;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            NuclearCurrentDownParticle currentDownParticle = new NuclearCurrentDownParticle(clientWorld, d, e, f);
            currentDownParticle.setSprite(this.spriteProvider);
            return currentDownParticle;
        }
    }
}

