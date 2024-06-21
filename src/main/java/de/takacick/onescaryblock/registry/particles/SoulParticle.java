package de.takacick.onescaryblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;

public class SoulParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    private SoulParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.scale(1.5f);
        this.setSpriteForAge(spriteProvider);

        this.velocityMultiplier = 1f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.gravityStrength = 0;
        this.collidesWithWorld = false;
        this.scale *= 0.75f;
        this.maxAge = 15 + this.random.nextInt(5);
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        Vec3d velocity = new Vec3d(this.velocityX, this.velocityY, this.velocityZ);
        velocity = velocity.add(this.random.nextGaussian() * 0.1, this.random.nextGaussian() * 0.1, this.random.nextGaussian() * 0.1);
        this.velocityX = velocity.getX();
        this.velocityY = velocity.getY();
        this.velocityZ = velocity.getZ();

        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new SoulParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}