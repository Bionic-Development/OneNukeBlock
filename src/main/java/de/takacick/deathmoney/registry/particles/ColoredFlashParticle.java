package de.takacick.deathmoney.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class ColoredFlashParticle extends SpriteBillboardParticle {

    private final double size;

    ColoredFlashParticle(ClientWorld clientWorld, double d, double e, double f, double g) {
        super(clientWorld, d, e, f);
        this.maxAge = 4;
        this.size = g;
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
    public float getSize(float tickDelta) {
        return 7.1f * MathHelper.sin(((float) this.age + tickDelta - 1.0f) * 0.25f * (float) Math.PI) * (float) size;
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<ColoredParticleEffect> {

        @Override
        public Particle createParticle(ColoredParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ColoredFlashParticle flash = new ColoredFlashParticle(clientWorld, d, e, f, g);
            flash.setSprite(this.spriteProvider);
            flash.setColor(particleEffect.color().getX(), particleEffect.color().getY(), particleEffect.color().getZ());
            return flash;
        }
    }
}

