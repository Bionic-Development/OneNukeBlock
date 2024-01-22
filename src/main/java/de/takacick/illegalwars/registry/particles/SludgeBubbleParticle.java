package de.takacick.illegalwars.registry.particles;

import de.takacick.illegalwars.registry.ParticleRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

@Environment(value = EnvType.CLIENT)
public class SludgeBubbleParticle
        extends SpriteBillboardParticle {
    private static final Random RANDOM = Random.create();
    private final SpriteProvider spriteProvider;
    public static final Vector3f COLOR = Vec3d.unpackRgb(0x283808).toVector3f();

    SludgeBubbleParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.5 - RANDOM.nextDouble(), velocityY, 0.5 - RANDOM.nextDouble());
        this.velocityMultiplier = 0.96f;
        this.gravityStrength = -0.03f;
        this.ascending = true;
        this.spriteProvider = spriteProvider;
        this.velocityY *= 0.2f;
        if (velocityX == 0.0 && velocityZ == 0.0) {
            this.velocityX *= 0.1f;
            this.velocityZ *= 0.1f;
        }
        this.scale *= 0.75f;
        this.maxAge = (int) (8.0 / (Math.random() * 0.8 + 0.2));
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);

        Vector3f color = new Vector3f(COLOR);

        float offset = world.getRandom().nextFloat() * 0.15f;
        Vector3f vector3f = color.add(offset, offset, offset);
        setColor(MathHelper.clamp(vector3f.x(), 0f, 1f), MathHelper.clamp(vector3f.y(), 0f, 1f), MathHelper.clamp(vector3f.z(), 0f, 1f));
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public void markDead() {
        this.world.addImportantParticle(ParticleRegistry.SLUDGE_BUBBLE_POP, true, x, y, z, velocityX, velocityY, velocityZ);
        this.world.playSound(x, y, z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 1f, 1f, true);
        super.markDead();
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
            return new SludgeBubbleParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

