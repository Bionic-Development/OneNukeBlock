package de.takacick.everythinghearts.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class HeartSplashParticle
        extends SpriteBillboardParticle {
    protected HeartSplashParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);
        this.velocityX *= 0.3f;
        this.velocityY = Math.random() * (double) 0.2f + (double) 0.1f;
        this.velocityZ *= 0.3f;
        this.setBoundingBoxSpacing(0.01f, 0.01f);
        this.gravityStrength = 0.06f;
        this.maxAge = (int) (8.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        BlockPos blockPos;
        double d;
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
            return;
        }
        this.velocityY -= (double) this.gravityStrength;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= (double) 0.98f;
        this.velocityY *= (double) 0.98f;
        this.velocityZ *= (double) 0.98f;
        if (this.onGround) {
            if (Math.random() < 0.5) {
                this.markDead();
            }
            this.velocityX *= (double) 0.7f;
            this.velocityZ *= (double) 0.7f;
        }
        if ((d = Math.max(this.world.getBlockState(blockPos = new BlockPos(this.x, this.y, this.z)).getCollisionShape(this.world, blockPos).getEndingCoord(Direction.Axis.Y, this.x - (double) blockPos.getX(), this.z - (double) blockPos.getZ()), (double) this.world.getFluidState(blockPos).getHeight(this.world, blockPos))) > 0.0 && this.y < (double) blockPos.getY() + d) {
            this.markDead();
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
            HeartSplashParticle heartSplashParticle = new HeartSplashParticle(clientWorld, d, e, f);
            heartSplashParticle.setSprite(this.spriteProvider);
            return heartSplashParticle;
        }
    }
}

