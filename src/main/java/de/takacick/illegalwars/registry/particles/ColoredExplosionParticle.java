package de.takacick.illegalwars.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class ColoredExplosionParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    ColoredExplosionParticle(ClientWorld clientWorld, Vector3f color, double d, double e, double f, double g, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);

        this.maxAge = 6 + this.random.nextInt(4);
        this.scale = 2.0f * (1.0f - (float) g * 0.5f);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);

        float offset = world.getRandom().nextFloat() * 0.15f;

        Vector3f vector3f = color
                .add(offset, offset, offset);
        setColor(MathHelper.clamp(vector3f.x(), 0f, 1f), MathHelper.clamp(vector3f.y(), 0f, 1f), MathHelper.clamp(vector3f.z(), 0f, 1f));
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
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
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
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
            return new ColoredExplosionParticle(clientWorld, particleEffect.color(), d, e, f, g, this.spriteProvider);
        }
    }
}