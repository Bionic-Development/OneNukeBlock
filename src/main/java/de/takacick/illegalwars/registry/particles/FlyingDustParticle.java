package de.takacick.illegalwars.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector3f;

public class FlyingDustParticle extends AnimatedParticle {

    FlyingDustParticle(ClientWorld world, Vector3f color, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);

        this.velocityX = velocityX * 0.5;
        this.velocityY = velocityY * 0.5;
        this.velocityZ = velocityZ * 0.5;

        this.collidesWithWorld = true;
        this.gravityStrength = 0;

        this.scale *= 2.97499995f;
        int i = (int) (20.0 / (Math.random() * 0.8 + 0.2));
        this.maxAge = (int) Math.max((float) i * 0.7f, 1.0f);
        this.setSpriteForAge(spriteProvider);
        this.angle = (float) Math.random() * ((float) Math.PI * 2);
        this.prevAngle = this.angle;
        setColor(color.x(), color.y(), color.z());
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float tickDelta) {
        return (1 - (age + tickDelta) / ((float) getMaxAge())) * this.scale;
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<ColoredParticleEffect> {

        @Override
        public Particle createParticle(ColoredParticleEffect parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new FlyingDustParticle(clientWorld, parameters.color(), d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

