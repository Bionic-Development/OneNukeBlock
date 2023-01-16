package de.takacick.imagineanything.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class ThanosExplosionParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private static final List<Integer> colors = new ArrayList<>();

    static {
        colors.add(0xD877FF);
        colors.add(0xE381FF);
    }

    ThanosExplosionParticle(ClientWorld clientWorld, double d, double e, double f, double g, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);

        this.maxAge = 6 + this.random.nextInt(4);
        this.scale = 2.0f * (1.0f - (float) g * 0.5f);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);

        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(colors.get(world.getRandom().nextInt(colors.size()))));
        float j = this.random.nextFloat() * 0.3f + 0.7f;
        this.red = vec3f.getX() * j;
        this.green = vec3f.getY() * j;
        this.blue = vec3f.getZ() * j;
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
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new ThanosExplosionParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}