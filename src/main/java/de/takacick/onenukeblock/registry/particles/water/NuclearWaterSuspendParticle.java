package de.takacick.onenukeblock.registry.particles.water;

import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.joml.Vector3f;

public class NuclearWaterSuspendParticle extends SpriteBillboardParticle {
    NuclearWaterSuspendParticle(ClientWorld world, SpriteProvider spriteProvider, double x, double y, double z) {
        super(world, x, y - 0.125, z);
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.setSprite(spriteProvider);
        this.scale *= this.random.nextFloat() * 0.6f + 0.2f;
        this.maxAge = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        this.collidesWithWorld = false;
        this.velocityMultiplier = 1.0f;
        this.gravityStrength = 0.0f;
        Vector3f color = NuclearWaterFluid.COLOR;
        this.setColor(color.x(), color.y(), color.z());
    }

    NuclearWaterSuspendParticle(ClientWorld world, SpriteProvider spriteProvider, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y - 0.125, z, velocityX, velocityY, velocityZ);
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.setSprite(spriteProvider);
        this.scale *= this.random.nextFloat() * 0.6f + 0.6f;
        this.maxAge = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        this.collidesWithWorld = false;
        this.velocityMultiplier = 1.0f;
        this.gravityStrength = 0.0f;
        Vector3f color = NuclearWaterFluid.COLOR;
        this.setColor(color.x(), color.y(), color.z());
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
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
            NuclearWaterSuspendParticle nuclearWaterSuspendParticle = new NuclearWaterSuspendParticle(clientWorld, this.spriteProvider, d, e, f);
            return nuclearWaterSuspendParticle;
        }
    }
}