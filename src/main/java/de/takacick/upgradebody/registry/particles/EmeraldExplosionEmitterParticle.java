package de.takacick.upgradebody.registry.particles;

import de.takacick.upgradebody.registry.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class EmeraldExplosionEmitterParticle
        extends NoRenderParticle {
    private int age_;
    private final int maxAge_;

    EmeraldExplosionEmitterParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.maxAge_ = 8;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 6; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double e = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            this.world.addParticle(ParticleRegistry.EMERALD_EXPLOSION, d, e, f, (float) this.age_ / (float) this.maxAge_, 0.0, 0.0);
        }
        ++this.age_;
        if (this.age_ == this.maxAge_) {
            this.markDead();
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EmeraldExplosionEmitterParticle(clientWorld, d, e, f);
        }
    }
}
