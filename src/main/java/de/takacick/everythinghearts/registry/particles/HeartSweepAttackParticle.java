package de.takacick.everythinghearts.registry.particles;

import de.takacick.everythinghearts.registry.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class HeartSweepAttackParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    HeartSweepAttackParticle(ClientWorld world, double x, double y, double z, double d, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.spriteProvider = spriteProvider;
        this.maxAge = 4;

        this.scale = 1.0f - (float) d * 0.5f;

        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(0xFF1313));
        this.red = vec3f.getX();
        this.green = vec3f.getY();
        this.blue = vec3f.getZ();

        this.setSpriteForAge(spriteProvider);
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

        if (world.getRandom().nextDouble() <= 0.5) {
            world.addParticle(ParticleRegistry.HEART, x + world.getRandom().nextGaussian() * 0.35, y - 0.02, z + world.getRandom().nextGaussian() * 0.35, 0, -0.1, 0);
        }
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new HeartSweepAttackParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}