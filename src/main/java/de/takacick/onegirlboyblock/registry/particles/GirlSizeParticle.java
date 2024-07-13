package de.takacick.onegirlboyblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class GirlSizeParticle
        extends SpriteBillboardParticle {
    private static final List<Vector3f> colors = Arrays.asList(
            Vec3d.unpackRgb(0xE0218A).toVector3f(),
            Vec3d.unpackRgb(0xED5C9B).toVector3f(),
            Vec3d.unpackRgb(0xF7B9D7).toVector3f(),
            Vec3d.unpackRgb(0xFACDE5).toVector3f(),
            Vec3d.unpackRgb(0xF18DBC).toVector3f()
    );

    private final SpriteProvider spriteProvider;
    private Entity ownerEntity;
    private Vec3d offset = new Vec3d(0, 0, 0);
    private Vec3d prevOffset = new Vec3d(0, 0, 0);

    private GirlSizeParticle(ClientWorld world, int owner, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0, velocityY, 0);

        this.spriteProvider = spriteProvider;
        this.scale *= 0.75f;
        this.velocityMultiplier = 0.96f;

        this.collidesWithWorld = false;
        this.scale *= 0.75f;
        this.velocityY = velocityY;
        this.maxAge = 20;
        this.setSpriteForAge(spriteProvider);

        Vector3f color = colors.get(world.getRandom().nextInt(colors.size()));
        setColor(color.x(), color.y(), color.z());

        if (owner >= 0) {
            this.ownerEntity = world.getEntityById(owner);
            if (this.ownerEntity != null) {
                this.offset = ownerEntity.getPos().subtract(new Vec3d(x, y, z));
                this.prevOffset = this.offset;
            }
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        this.prevOffset = this.offset;
        this.offset = this.offset.add(new Vec3d(0, this.velocityY * 1, 0).multiply(0.25));

        this.velocityX *= this.velocityMultiplier;
        this.velocityY *= this.velocityMultiplier;
        this.velocityZ *= this.velocityMultiplier;
        this.age = Math.min(this.age + 1, this.maxAge);

        if (this.age >= this.maxAge) {
            this.markDead();
        }

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public int getBrightness(float tint) {
        return 15728880;
    }


    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        float alpha = (1 - (age + tickDelta) / ((float) getMaxAge()));
        if (this.ownerEntity != null) {
            Vec3d add = new Vec3d(MathHelper.lerp(tickDelta, this.prevOffset.getX(), this.offset.getX()),
                    MathHelper.lerp(tickDelta, this.prevOffset.getY(), this.offset.getY()),
                    MathHelper.lerp(tickDelta, this.prevOffset.getZ(), this.offset.getZ()));

            Vec3d vec3d = this.ownerEntity.getLerpedPos(tickDelta).subtract(add);
            setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());

            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;
        }

        setAlpha(Math.max(0f, 1 - (float) age / ((float) getMaxAge())));

        if (alpha <= 0) {
            this.markDead();
        }

        super.buildGeometry(vertexConsumer, camera, tickDelta);
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<EntityParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(EntityParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new GirlSizeParticle(clientWorld, particleEffect.owner(), d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

