package de.takacick.elementalblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

public class MagicLavaParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;
    private static final List<Integer> colors = Arrays.asList(0xDE4100, 0xF2A82E, 0xE35800);

    MagicLavaParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityMultiplier = 0.96f;
        this.spriteProvider = spriteProvider;
        this.scale *= 0.55f;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
        this.maxAge += 30;

        Vector3f vector3f = Vec3d.unpackRgb(colors.get(world.getRandom().nextInt(colors.size()))).toVector3f();

        this.setColor(vector3f.x(), vector3f.y(), vector3f.z());
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getBrightness(float tint) {
        float f = ((float)this.age + tint) / (float)this.maxAge;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        int i = super.getBrightness(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int)(f * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    public void tick() {
        super.tick();
        setAlpha(Math.max(0f, 1f - (float) age / (float) getMaxAge()));

        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<DefaultParticleType> {

        @Override
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new MagicLavaParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

