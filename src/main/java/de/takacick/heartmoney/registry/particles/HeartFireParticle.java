package de.takacick.heartmoney.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class HeartFireParticle extends AbstractSlowingParticle {
    private static final List<Integer> colors = new ArrayList<>();

    static {
        colors.add(0xFF1313);
        colors.add(0xBB1313);
    }

    HeartFireParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(colors.get(world.getRandom().nextInt(colors.size()))));
        float j = this.random.nextFloat() * 0.6f + 0.4f;
        this.red = vec3f.getX() * j;
        this.green = vec3f.getY() * j;
        this.blue = vec3f.getZ() * j;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public float getSize(float tickDelta) {
        float f = ((float) this.age + tickDelta) / (float) this.maxAge;
        return this.scale * (1.0f - f * f * 0.5f);
    }

    @Override
    public int getBrightness(float tint) {
        float f = ((float) this.age + tint) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        int i = super.getBrightness(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int) (f * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            HeartFireParticle heartFireParticle = new HeartFireParticle(clientWorld, d, e, f, g, h, i);
            heartFireParticle.setSprite(this.spriteProvider);
            return heartFireParticle;
        }
    }
}

