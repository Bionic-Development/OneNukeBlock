package de.takacick.secretcraftbase.registry.particles;

import de.takacick.secretcraftbase.registry.entity.living.SecretPigPoweredPortalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

@Environment(value = EnvType.CLIENT)
public class FollowDustParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;
    private SecretPigPoweredPortalEntity pigPoweredPortalEntity;

    protected FollowDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Vector3f color, float alpha, int owner, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityMultiplier = 0.96f;
        this.ascending = true;
        this.spriteProvider = spriteProvider;
        this.velocityX *= 0.1f;
        this.velocityY *= 0.1f;
        this.velocityZ *= 0.1f;
        float f = this.random.nextFloat() * 0.4f + 0.6f;
        this.red = this.darken(color.x(), f);
        this.green = this.darken(color.y(), f);
        this.blue = this.darken(color.z(), f);
        this.scale *= 0.75f * alpha;
        int i = (int) (8.0 / (this.random.nextDouble() * 0.8 + 0.2));
        this.maxAge = (int) Math.max((float) i * alpha, 1.0f);
        this.setSpriteForAge(spriteProvider);

        if (owner > -1) {
            if (world.getEntityById(owner) instanceof SecretPigPoweredPortalEntity secretPigPoweredPortalEntity) {
                this.pigPoweredPortalEntity = secretPigPoweredPortalEntity;
            }
        }

    }

    protected float darken(float colorComponent, float multiplier) {
        return (this.random.nextFloat() * 0.2f + 0.8f) * colorComponent * multiplier;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp(((float) this.age + tickDelta) / (float) this.maxAge * 32.0f, 0.0f, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {

        if (this.pigPoweredPortalEntity != null && this.pigPoweredPortalEntity.isAlive()) {
            Vec3d vec3d = this.pigPoweredPortalEntity.getSidewaysRotationVector().multiply(0.525);
            Vec3d pos = this.pigPoweredPortalEntity.getPos().add(0, 0.625 * this.pigPoweredPortalEntity.getHeight() / 0.9f, 0).add(vec3d);
            setPos(pos.getX(), pos.getY(), pos.getZ());
        }

        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<ColoredParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(ColoredParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new FollowDustParticle(clientWorld, d, e, f, g, h, i, particleEffect.color(), particleEffect.alpha(), particleEffect.owner(), this.spriteProvider);
        }
    }
}
