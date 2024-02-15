package de.takacick.secretgirlbase.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AscendingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

@Environment(value = EnvType.CLIENT)
public class ColoredBubbleDustParticle
        extends AscendingParticle {

    protected ColoredBubbleDustParticle(ClientWorld world, Vector3f color, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.7f, 0.6f, 0.7f, velocityX, velocityY + (double) 0.15f, velocityZ, scaleMultiplier, spriteProvider, 0.5f, 7, 0.5f, false);
        float f = (float) Math.random() * 0.2f;
        this.red = MathHelper.clamp(color.x() - f, 0f, 1f);
        this.green = MathHelper.clamp(color.y() - f, 0f, 1f);
        this.blue = MathHelper.clamp(color.z() - f, 0f, 1f);
        this.ascending = false;
    }

    @Override
    public void tick() {
        this.gravityStrength = 0.88f * this.gravityStrength;
        this.velocityMultiplier = 0.92f * this.velocityMultiplier;
        super.tick();
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<ColoredParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(ColoredParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new ColoredBubbleDustParticle(clientWorld, particleEffect.color(), d, e, f, g, h, i, 1.0f, this.spriteProvider);
        }
    }
}

