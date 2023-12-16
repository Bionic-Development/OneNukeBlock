package de.takacick.tinyhouse.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class BloodSplashParticle extends RainSplashParticle {
    BloodSplashParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        this.gravityStrength = 0.04f;
        if (h == 0.0 && (g != 0.0 || i != 0.0)) {
            this.velocityX = g;
            this.velocityY = 0.1;
            this.velocityZ = i;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            BloodSplashParticle bloodSplashParticle = new BloodSplashParticle(clientWorld, d, e, f, g, h, i);
            bloodSplashParticle.setSprite(this.spriteProvider);
            bloodSplashParticle.setColor(0.50980392156f, 0.03921568627f, 0.03921568627f);
            return bloodSplashParticle;
        }
    }
}