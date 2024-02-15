package de.takacick.secretgirlbase.registry.particles;

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
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

@Environment(value = EnvType.CLIENT)
public class ShrinkParticle
        extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;
    private Entity ownerEntity;
    float bodyY;
    private Vec3d offset = new Vec3d(0, 0, 0);
    private Vec3d prevOffset = new Vec3d(0, 0, 0);

    private ShrinkParticle(ClientWorld world, int owner, Vector3f color, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0, velocityY, 0);

        this.spriteProvider = spriteProvider;
        this.scale(1.5f);
        this.setSpriteForAge(spriteProvider);

        this.velocityMultiplier = 1;
        this.gravityStrength = 1;
        this.collidesWithWorld = false;
        this.scale *= 0.95f;
        this.setSpriteForAge(spriteProvider);
        this.velocityY = velocityY;
        this.maxAge = 10;

        setColor(color.x(), color.y(), color.z());

        if (owner >= 0) {
            ownerEntity = world.getEntityById(owner);
            if (ownerEntity != null) {
                this.offset = ownerEntity.getPos().subtract(new Vec3d(x, y, z));
                this.prevOffset = this.offset;
                this.maxAge = (int) (Math.abs((float) (ownerEntity.getBodyY(0) - this.y)) * 4f) + world.getRandom().nextInt(6);

                ScaleData scaleDataWidth = ScaleTypes.BASE.getScaleData(ownerEntity);
                this.scale *= scaleDataWidth.getScale() * 0.5f;
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
        if (ownerEntity != null) {
            Vec3d add = new Vec3d(MathHelper.lerp(tickDelta, this.prevOffset.getX(), this.offset.getX()),
                    MathHelper.lerp(tickDelta, this.prevOffset.getY(), this.offset.getY()),
                    MathHelper.lerp(tickDelta, this.prevOffset.getZ(), this.offset.getZ()));

            Vec3d vec3d = ownerEntity.getLerpedPos(tickDelta).subtract(add);
            setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());

            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;

            alpha = MathHelper.clamp(Math.min(1f - (float) (ownerEntity.getBodyY(1) - this.y) / ownerEntity.getHeight(), alpha), 0f, 1f);
        }

        setAlpha(alpha);
        if (alpha <= 0) {
            this.markDead();
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
            return new ShrinkParticle(clientWorld, particleEffect.owner(), particleEffect.color(), d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

