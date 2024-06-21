package de.takacick.onescaryblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.FireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;

public class SmokeParticle
        extends FireSmokeParticle {

    private final Vec3d start;
    private final Vec3d end;
    private Vec3d prevOffset;

    private SmokeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, 1.5f, spriteProvider);
        this.ascending = false;

        this.collidesWithWorld = false;
        this.maxAge *= 0.25;

        this.gravityStrength = 1f;
        this.velocityMultiplier = 0.8f;

        this.start = new Vec3d(this.x, this.y, this.z).add(velocityX, velocityY, velocityZ);
        this.end = new Vec3d(this.x, this.y, this.z);
        this.prevOffset = new Vec3d(this.x, this.y, this.z).add(velocityX, velocityY, velocityZ);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        float progress = (this.age + tickDelta) / getMaxAge();
        Vec3d offset = this.start.subtract(this.end).multiply(1f - progress).add(this.end);
        this.x = offset.getX();
        this.y = offset.getY();
        this.z = offset.getZ();

        this.prevPosX = this.prevOffset.getX();
        this.prevPosY = this.prevOffset.getY();
        this.prevPosZ = this.prevOffset.getZ();
        this.prevOffset = offset;

        super.buildGeometry(vertexConsumer, camera, tickDelta);
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
            return new SmokeParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

