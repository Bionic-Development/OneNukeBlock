package de.takacick.upgradebody.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class EmeraldPortalParticle extends SpriteBillboardParticle {

    private final double startX;
    private final double startY;
    private final double startZ;

    EmeraldPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
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

        Vector3f vector3f = Vec3d.unpackRgb(0xFACF18).toVector3f();

        float brightness = 0.80f + clientWorld.getRandom().nextFloat() * 0.2f;

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
            EmeraldPortalParticle emeraldPortalParticle = new EmeraldPortalParticle(clientWorld, d, e, f, g, h, i);
            emeraldPortalParticle.setSprite(this.spriteProvider);
            return emeraldPortalParticle;
        }
    }
}
