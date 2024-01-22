package de.takacick.illegalwars.registry.particles;

import de.takacick.illegalwars.registry.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector3f;

public class ColoredExplosionEmitterParticle
        extends NoRenderParticle {
    private int age_;
    private final int maxAge_;
    private final Vector3f color;

    ColoredExplosionEmitterParticle(ClientWorld clientWorld, Vector3f color, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.maxAge_ = 8;
        this.color = color;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 6; ++i) {
            double d = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double e = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            double f = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
            this.world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_EXPLOSION, this.color), true, d, e, f, (float) this.age_ / (float) this.maxAge_, 0.0, 0.0);
        }
        ++this.age_;
        if (this.age_ == this.maxAge_) {
            this.markDead();
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<ColoredParticleEffect> {
        @Override
        public Particle createParticle(ColoredParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new ColoredExplosionEmitterParticle(clientWorld, particleEffect.color(), d, e, f);
        }
    }
}

