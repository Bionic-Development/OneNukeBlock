package de.takacick.raidbase.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TransparentDustParticle extends AnimatedParticle {

    TransparentDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);

        this.velocityX = velocityX * 0.5;
        this.velocityY = velocityY * 0.5;
        this.velocityZ = velocityZ * 0.5;

        this.collidesWithWorld = true;

        this.scale *= 2.97499995f;
        int i = (int) (20.0 / (Math.random() * 0.8 + 0.2));
        this.maxAge = (int) Math.max((float) i * 0.9f, 1.0f);
        this.setSpriteForAge(spriteProvider);
        this.angle = (float) Math.random() * ((float) Math.PI * 2);
        this.prevAngle = this.angle;
        this.alpha = 0.5f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp(((float) this.age + tickDelta) / (float) this.maxAge * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Override
    public int getBrightness(float tint) {
        BlockPos blockPos = BlockPos.ofFloored(this.x, this.y, this.z);
        if (this.world.isChunkLoaded(blockPos)) {
            return WorldRenderer.getLightmapCoordinates(this.world, blockPos);
        }
        return 0;
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<ColoredParticleEffect> {

        @Override
        public Particle createParticle(ColoredParticleEffect parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            TransparentDustParticle dustParticle = new TransparentDustParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            dustParticle.setColor(parameters.color().x(), parameters.color().y(), parameters.color().z());
            return dustParticle;
        }
    }
}

