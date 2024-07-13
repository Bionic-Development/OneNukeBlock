package de.takacick.onegirlboyblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.joml.Vector3f;

public class GlitterSweepParticle
        extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    GlitterSweepParticle(ClientWorld world, double x, double y, double z, double d, double h, double i, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.spriteProvider = spriteProvider;
        this.maxAge = 4;

        float offset = this.random.nextFloat() * 0.1f + 0.9f;
        Vector3f color = GlitterParticle.COLORS.get(this.random.nextInt(GlitterParticle.COLORS.size())).mul(offset, new Vector3f(1f, 1f, 1f));
        setColor(color.x(), color.y(), color.z());

        this.scale = 1.0f - (float) d * 0.5f;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new GlitterSweepParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}