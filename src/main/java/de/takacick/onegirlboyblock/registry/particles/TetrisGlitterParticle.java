package de.takacick.onegirlboyblock.registry.particles;

import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

@Environment(value = EnvType.CLIENT)
public class TetrisGlitterParticle extends AnimatedParticle {

    private final float rotationSpeed;
    private final TetrisEntity.Variant tetris;

    TetrisGlitterParticle(ClientWorld world, int variant, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);
        this.velocityMultiplier = 1f;
        this.gravityStrength = 0.03f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.25f;
        this.maxAge = 11 + this.random.nextInt(4);
        this.setSpriteForAge(spriteProvider);
        this.collidesWithWorld = false;

        float offset = this.random.nextFloat() * 0.1f + 0.9f;

        int length = TetrisEntity.Variant.values().length;
        this.tetris = variant < length && variant >= 0 ? TetrisEntity.Variant.byId(variant) : TetrisEntity.Variant.byId(this.random.nextInt(length));

        Vector3f color = Vec3d.unpackRgb(this.tetris.getColor()).toVector3f().mul(offset, new Vector3f(1f, 1f, 1f));
        setColor(color.x(), color.y(), color.z());

        this.rotationSpeed = ((float) Math.random() - 0.5f) * 0.1f;
        this.angle = (float) Math.random() * ((float) Math.PI * 2);
        this.prevAngle = this.angle;
    }

    @Override
    public void tick() {
        this.prevAngle = this.angle;
        this.angle += (float) Math.PI * this.rotationSpeed * 2.0f;

        super.tick();
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<TetrisParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(TetrisParticleEffect tetrisParticleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new TetrisGlitterParticle(clientWorld, tetrisParticleEffect.tetrisVariant(), d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
