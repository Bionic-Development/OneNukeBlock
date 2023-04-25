package de.takacick.everythinghearts.registry.particles;

import de.takacick.everythinghearts.registry.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class HeartExplosionParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    HeartExplosionParticle(ClientWorld clientWorld, double d, double e, double f, double g, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);

        this.maxAge = 6 + this.random.nextInt(4);
        this.scale = 2.0f * (1.0f - (float) g * 0.5f);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);

        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(0xFF1313));
        float j = this.random.nextFloat() * 0.2f + 0.8f;
        this.red = vec3f.getX() * j;
        this.green = vec3f.getY() * j;
        this.blue = vec3f.getZ() * j;
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
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

        for (int i = 0; i < 2; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 1.0;
            double e = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 1.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 1.0;
            this.world.addParticle(ParticleRegistry.HEART, d, e, f, (this.x - d) * -0.5, (this.y - e) * -0.5, (this.z - f) * -0.5);
        }

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
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
            return new HeartExplosionParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}