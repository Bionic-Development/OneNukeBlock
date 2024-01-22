package de.takacick.illegalwars.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

public class SludgeBubblePopParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    SludgeBubblePopParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.spriteProvider = spriteProvider;
        this.maxAge = 4;
        this.gravityStrength = 0.008f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.setSpriteForAge(spriteProvider);

        Vector3f color = new Vector3f(SludgeBubbleParticle.COLOR);
        float offset = world.getRandom().nextFloat() * 0.15f;
        Vector3f vector3f = color.add(offset, offset, offset);
        setColor(MathHelper.clamp(vector3f.x(), 0f, 1f), MathHelper.clamp(vector3f.y(), 0f, 1f), MathHelper.clamp(vector3f.z(), 0f, 1f));
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
        this.velocityY -= this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            SludgeBubblePopParticle sludgeBubblePopParticle = new SludgeBubblePopParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            sludgeBubblePopParticle.setSprite(this.spriteProvider);
            return sludgeBubblePopParticle;
        }
    }
}