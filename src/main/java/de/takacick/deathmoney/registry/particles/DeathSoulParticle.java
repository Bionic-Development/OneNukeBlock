package de.takacick.deathmoney.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class DeathSoulParticle
        extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    private DeathSoulParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.scale(1.5f);
        this.setSpriteForAge(spriteProvider);

        this.velocityMultiplier = 0.99f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.gravityStrength = 0;
        this.collidesWithWorld = false;
        this.scale *= 0.75f;
        this.maxAge = 60 + this.random.nextInt(12);
        this.setSpriteForAge(spriteProvider);

        Vec3f color = new Vec3f(Vec3d.unpackRgb(0x4DFF17));
        this.setColor(color.getX(), color.getY(), color.getZ());
    }

    @Override
    public int getBrightness(float tint) {
        return super.getBrightness(tint);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
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
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            DeathSoulParticle deathSoulParticle = new DeathSoulParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            deathSoulParticle.setAlpha(1.0f);
            return deathSoulParticle;
        }
    }
}