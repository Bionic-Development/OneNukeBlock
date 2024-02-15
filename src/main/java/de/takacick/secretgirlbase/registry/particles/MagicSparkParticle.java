package de.takacick.secretgirlbase.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class MagicSparkParticle extends SpriteBillboardParticle {

    public MagicSparkParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        float j;
        this.red = j = this.random.nextFloat() * 0.1f + 0.2f;
        this.green = j;
        this.blue = j;
        this.setBoundingBoxSpacing(0.02f, 0.02f);
        this.scale *= this.random.nextFloat() * 0.6f + 0.5f;
        this.velocityX *= 0.02f;
        this.velocityY *= 0.02f;
        this.velocityZ *= 0.02f;
        this.maxAge = (int) (20.0 / (Math.random() * 0.8 + 0.2));

        Vector3f vector3f = Vec3d.unpackRgb(0xEA1CD0).toVector3f();
        setColor(vector3f.x(), vector3f.y(), vector3f.z());
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
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
            return;
        }
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= 0.99;
        this.velocityY *= 0.99;
        this.velocityZ *= 0.99;
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<DefaultParticleType> {

        @Override
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            MagicSparkParticle magicSparkParticle = new MagicSparkParticle(clientWorld, d, e, f, g, h, i);
            magicSparkParticle.setSprite(this.spriteProvider);
            magicSparkParticle.setMaxAge(3 + clientWorld.getRandom().nextInt(5));
            return magicSparkParticle;
        }
    }
}

