package de.takacick.secretgirlbase.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class MagicFlowerParticle extends SpriteBillboardParticle {

    protected MagicFlowerParticle(ClientWorld world, double x, double y, double z, double g, double h, double i, SpriteProvider spriteProvider) {
        super(world, x, y, z, g, h, i);
        this.setSprite(spriteProvider.getSprite(this.random.nextInt(12), 12));
        this.maxAge = world.getRandom().nextBetween(60, 150);
        this.velocityMultiplier = 0.99f;
        this.gravityStrength = 0;
        this.collidesWithWorld = false;
        this.scale = this.random.nextBoolean() ? 0.05f : 0.075f;

        this.setBoundingBoxSpacing(this.scale, this.scale);
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        this.velocityX *= this.velocityMultiplier;
        this.velocityY *= this.velocityMultiplier;
        this.velocityZ *= this.velocityMultiplier;
        this.age = Math.min(this.age + 1, this.maxAge);

        if (this.age >= this.maxAge) {
            this.markDead();
        }

        super.tick();
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        setAlpha(Math.max(1f - (age + tickDelta) / ((float) getMaxAge()), 0f));

        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<DefaultParticleType> {

        @Override
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new MagicFlowerParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

