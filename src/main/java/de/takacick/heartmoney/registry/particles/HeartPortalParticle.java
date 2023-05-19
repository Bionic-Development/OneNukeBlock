package de.takacick.heartmoney.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class HeartPortalParticle extends SpriteBillboardParticle {

    private final double startX;
    private final double startY;
    private final double startZ;

    HeartPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        this.velocityX = g;
        this.velocityY = h;
        this.velocityZ = i;
        this.startX = d;
        this.startY = e;
        this.startZ = f;
        this.prevPosX = d + g;
        this.prevPosY = e + h;
        this.prevPosZ = f + i;
        this.x = this.prevPosX;
        this.y = this.prevPosY;
        this.z = this.prevPosZ;
        this.collidesWithWorld = false;
        this.maxAge = (int) (Math.random() * 10.0) + 30;
        this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
        Vec3f color = new Vec3f(Vec3d.unpackRgb(0xFF1313));
        this.setColor(color.getX(), color.getY(), color.getZ());
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
        float f = (float) this.age / (float) this.maxAge;
        f = 1.0f - f;
        float g = 1.0f - f;
        g *= g;
        g *= g;
        this.x = this.startX + this.velocityX * (double) f;
        this.y = this.startY + this.velocityY * (double) f - (double) (g * 1.2f);
        this.z = this.startZ + this.velocityZ * (double) f;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            HeartPortalParticle heartPortalParticle = new HeartPortalParticle(clientWorld, d, e, f, g, h, i);
            heartPortalParticle.setSprite(this.spriteProvider);
            return heartPortalParticle;
        }
    }
}
