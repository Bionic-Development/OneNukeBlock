package de.takacick.onenukeblock.registry.particles.water;

import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.joml.Vector3f;

public class NuclearWaterSplashParticle extends RainSplashParticle {
    NuclearWaterSplashParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        this.gravityStrength = 0.04f;
        if (h == 0.0 && (g != 0.0 || i != 0.0)) {
            this.velocityX = g;
            this.velocityY = 0.1;
            this.velocityZ = i;
        }
        Vector3f color = NuclearWaterFluid.COLOR;
        this.setColor(color.x(), color.y(), color.z());
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
            NuclearWaterSplashParticle nuclearWaterSplashParticle = new NuclearWaterSplashParticle(clientWorld, d, e, f, g, h, i);
            nuclearWaterSplashParticle.setSprite(this.spriteProvider);
            return nuclearWaterSplashParticle;
        }
    }
}