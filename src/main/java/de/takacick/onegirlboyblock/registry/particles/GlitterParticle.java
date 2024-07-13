package de.takacick.onegirlboyblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.List;
import java.util.stream.Stream;

@Environment(value = EnvType.CLIENT)
public class GlitterParticle
        extends AnimatedParticle {

    public static final List<Vector3f> COLORS = Stream.of(
            0x8CF3ED,
            0xD4B9E1,
            0xEDC2EF,
            0xFFE1FD,
            0xFFFFFF
    ).map(integer -> Vec3d.unpackRgb(integer).toVector3f()).toList();

    private final float rotationSpeed;

    GlitterParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);
        this.velocityMultiplier = 0.6f;
        this.gravityStrength = 0.6f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.25f;
        this.maxAge = 15 + this.random.nextInt(12);
        this.setSpriteForAge(spriteProvider);

        float offset = this.random.nextFloat() * 0.1f + 0.9f;
        Vector3f color = COLORS.get(this.random.nextInt(COLORS.size())).mul(offset, new Vector3f(1f,1f,1f));
        setColor(color.x(), color.y(), color.z());

        this.rotationSpeed = ((float)Math.random() - 0.5f) * 0.1f;
        this.angle =  (float)Math.random() * ((float)Math.PI * 2);
        this.prevAngle = this.angle;
    }

    @Override
    public void tick() {
        this.prevAngle = this.angle;
        this.angle += (float)Math.PI * this.rotationSpeed * 2.0f;

        super.tick();
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new GlitterParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

