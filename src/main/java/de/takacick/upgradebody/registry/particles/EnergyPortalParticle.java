package de.takacick.upgradebody.registry.particles;

import de.takacick.upgradebody.access.PlayerProperties;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class EnergyPortalParticle extends SpriteBillboardParticle {

    private Entity ownerEntity;
    private final double startX;
    private final double startY;
    private final double startZ;
    private boolean flyRandom = false;

    protected EnergyPortalParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        if (g == 0 && h == 0) {
            ownerEntity = world.getEntityById((int) i);
            g = (float) world.getRandom().nextGaussian() * 2;
            h = (float) world.getRandom().nextGaussian() * 2;
            i = (float) world.getRandom().nextGaussian() * 2;
        }
        this.velocityX = g;
        this.velocityY = h;
        this.velocityZ = i;
        this.x = d;
        this.y = e;
        this.z = f;
        this.startX = this.x;
        this.startY = this.y;
        this.startZ = this.z;
        this.scale = 0.1f * (this.random.nextFloat() * 0.2f + 0.5f);
        this.maxAge = (int) (Math.random() * 10.0) + 40;

        Vector3f vector3f = Vec3d.unpackRgb(0xFF1111).toVector3f();

        float brightness = 0.60f + world.getRandom().nextFloat() * 0.4f;

        this.red = vector3f.x() * brightness;
        this.green = vector3f.y() * brightness;
        this.blue = vector3f.z() * brightness;
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
        f = 1.0f - f;
        f *= f;
        f = 1.0f - f;
        return this.scale * f;
    }

    @Override
    public int getBrightness(float tint) {
        int i = super.getBrightness(tint);
        float f = (float) this.age / (float) this.maxAge;
        f *= f;
        f *= f;
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((k += (int) (f * 15.0f * 16.0f)) > 240) {
            k = 240;
        }
        return j | k << 16;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (ownerEntity instanceof PlayerProperties playerProperties) {
            if (!playerProperties.isUsingEnergyBellyBlast()) {
                this.markDead();
            }
        }

        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Override
    public void tick() {
        float f;
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        if (!flyRandom) {
            float g = f = (float) this.age / (float) this.maxAge;
            f = -f + f * f * 2.0f;
            f = 1.0f - f;
            this.x = this.startX + this.velocityX * (double) f;
            this.y = this.startY + this.velocityY * (double) f + (double) (1.0f - g);
            this.z = this.startZ + this.velocityZ * (double) f;
        } else {
            super.tick();
        }
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
            EnergyPortalParticle portalParticle = new EnergyPortalParticle(clientWorld, d, e, f, g, h, i);
            portalParticle.setSprite(this.spriteProvider);
            return portalParticle;
        }
    }
}

