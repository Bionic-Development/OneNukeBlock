package de.takacick.onenukeblock.registry.particles.water;

import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

public class NuclearWaterBubbleParticle extends SpriteBillboardParticle {

    NuclearWaterBubbleParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        this.setBoundingBoxSpacing(0.02f, 0.02f);
        this.scale *= this.random.nextFloat() * 0.6f + 0.2f;
        this.velocityX = g * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.velocityY = h * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.velocityZ = i * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.maxAge = (int) (8.0 / (Math.random() * 0.8 + 0.2));
        Vector3f color = NuclearWaterFluid.COLOR;
        this.setColor(color.x(), color.y(), color.z());
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
        this.velocityY += 0.002;
        this.move(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= 0.85f;
        this.velocityY *= 0.85f;
        this.velocityZ *= 0.85f;
        if (!this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
            this.markDead();
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            NuclearWaterBubbleParticle waterBubbleParticle = new NuclearWaterBubbleParticle(clientWorld, d, e, f, g, h, i);
            waterBubbleParticle.setSprite(this.spriteProvider);
            return waterBubbleParticle;
        }
    }
}

