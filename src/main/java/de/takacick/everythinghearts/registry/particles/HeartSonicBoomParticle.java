package de.takacick.everythinghearts.registry.particles;

import de.takacick.everythinghearts.registry.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class HeartSonicBoomParticle extends ExplosionLargeParticle {
    protected HeartSonicBoomParticle(ClientWorld clientWorld, double d, double e, double f, double g, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, spriteProvider);
        this.maxAge = 16;
        this.scale = 1.5f;

        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(0xFF1313));
         float j = this.random.nextFloat() * 0.2f + 0.8f;
        this.red = vec3f.getX() * j;
        this.green = vec3f.getY() * j;
        this.blue = vec3f.getZ() * j;

        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        if (world.getRandom().nextDouble() <= 0.5) {
            world.addParticle(ParticleRegistry.HEART, x + world.getRandom().nextGaussian() * 0.5, y + world.getRandom().nextGaussian() * 0.5, z + world.getRandom().nextGaussian()  + world.getRandom().nextGaussian() * 0.5,  world.getRandom().nextGaussian() * 0.5,  world.getRandom().nextGaussian() * 0.5,  + world.getRandom().nextGaussian() * 0.5);
        }
        super.tick();
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
            return new HeartSonicBoomParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}

