package de.takacick.emeraldmoney.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class EmeraldPoofParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected EmeraldPoofParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.gravityStrength = -0.1f;
        this.velocityMultiplier = 0.9f;
        this.spriteProvider = spriteProvider;
        this.velocityX = velocityX + (Math.random() * 2.0 - 1.0) * (double) 0.05f;
        this.velocityY = velocityY + (Math.random() * 2.0 - 1.0) * (double) 0.05f;
        this.velocityZ = velocityZ + (Math.random() * 2.0 - 1.0) * (double) 0.05f;
        this.scale = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 6.0f + 1.0f);
        this.maxAge = (int) (16.0 / ((double) this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.setSpriteForAge(spriteProvider);

        Vector3f vector3f = Vec3d.unpackRgb(0x17DD62).toVector3f();

        float brightness = 0.60f + world.getRandom().nextFloat() * 0.4f;

        this.red = vector3f.x() * brightness;
        this.green = vector3f.y() * brightness;
        this.blue = vector3f.z() * brightness;
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
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
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            EmeraldPoofParticle poofParticle = new EmeraldPoofParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            poofParticle.setSprite(this.spriteProvider);
            return poofParticle;
        }
    }
}

