package de.takacick.onenukeblock.registry.particles.water;

import de.takacick.onenukeblock.registry.ParticleRegistry;
import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;

public class NuclearBubbleColumnUpParticle extends SpriteBillboardParticle {

    private int outside = 0;

    NuclearBubbleColumnUpParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f);
        this.gravityStrength = -0.125f;
        this.velocityMultiplier = 0.85f;
        this.setBoundingBoxSpacing(0.02f, 0.02f);
        this.scale *= this.random.nextFloat() * 0.6f + 0.2f;
        this.velocityX = g * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.velocityY = h * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.velocityZ = i * (double) 0.2f + (Math.random() * 2.0 - 1.0) * (double) 0.02f;
        this.maxAge = (int) (40.0 / (Math.random() * 0.8 + 0.2));
        Vector3f color = NuclearWaterFluid.COLOR;
        this.setColor(color.x(), color.y(), color.z());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
            outside++;
            if (outside > 5) {
                this.markDead();
            }
        }
    }

    @Override
    public void markDead() {
        Particle particle = MinecraftClient.getInstance().particleManager.addParticle(ParticleRegistry.NUCLEAR_BUBBLE_POP,
                x, y, z, velocityX, velocityY, velocityZ);
        particle.scale(scale);
        super.markDead();
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
            NuclearBubbleColumnUpParticle bubbleColumnUpParticle = new NuclearBubbleColumnUpParticle(clientWorld, d, e, f, g, h, i);
            bubbleColumnUpParticle.setSprite(this.spriteProvider);
            return bubbleColumnUpParticle;
        }
    }
}