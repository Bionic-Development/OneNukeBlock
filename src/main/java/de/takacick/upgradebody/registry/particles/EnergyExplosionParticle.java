package de.takacick.upgradebody.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class EnergyExplosionParticle extends SpriteBillboardParticle {
    private final static List<Integer> ENERGY_COLORS = new ArrayList<>();

    static {
        ENERGY_COLORS.add(0xFF2C28);
        ENERGY_COLORS.add(0xFF1111);
    }

    private final SpriteProvider spriteProvider;

    EnergyExplosionParticle(ClientWorld clientWorld, double d, double e, double f, double g, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);

        this.maxAge = 6 + this.random.nextInt(4);
        this.scale = 2.0f * (1.0f - (float) g * 0.5f);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);

        Vector3f vector3f = Vec3d.unpackRgb(0xFF1111).toVector3f();

        float brightness = 0.60f + clientWorld.getRandom().nextFloat() * 0.4f;

        this.red = vector3f.x() * brightness;
        this.green = vector3f.y() * brightness;
        this.blue = vector3f.z() * brightness;
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
            return new EnergyExplosionParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }

    public static Vector3f getEnergyColor(Random random) {
        return Vec3d.unpackRgb(ENERGY_COLORS.get(random.nextInt(ENERGY_COLORS.size())))
                .toVector3f();
    }
}