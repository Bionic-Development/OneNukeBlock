package de.takacick.everythinghearts.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class HeartUpgradeParticle extends ExplosionLargeParticle {

    private Entity ownerEntity;
    private Vec3d offset = new Vec3d(0, 0, 0);

    private float height = 0f;
    private float width = 1f;

    protected HeartUpgradeParticle(ClientWorld clientWorld, double d, double e, double f, double g, int owner, float height, float width, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, spriteProvider);
        this.maxAge = 16;
        this.scale = 1.5f;
        this.setSpriteForAge(spriteProvider);
        this.height = height;
        this.width = width;
        Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(0xFF1313));

        float j = this.random.nextFloat() * 0.2f + 0.8f;
        this.red = vec3f.getX() * j;
        this.green = vec3f.getY() * j;
        this.blue = vec3f.getZ() * j;

        if (owner >= 0) {
            ownerEntity = world.getEntityById(owner);
            if (ownerEntity != null) {
                offset = ownerEntity.getPos().subtract(new Vec3d(d, e, f));
            }
        }
    }

    @Override
    public void tick() {

        this.y = y + (this.height / getMaxAge());

        super.tick();
    }

    @Override
    public float getSize(float tickDelta) {
        return width;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (ownerEntity != null) {
            Vec3d vec3d = ownerEntity.getLerpedPos(tickDelta).subtract(offset);
            setPos(vec3d.getX(), vec3d.getY() + (this.height / getMaxAge() * age), vec3d.getZ());

            prevPosX = x;
            prevPosY = y;
            prevPosZ = z;
        }

        Quaternion quaternion;
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        if (this.angle == 0.0f) {
            quaternion = Vec3f.POSITIVE_X.getDegreesQuaternion(90);
        } else {
            quaternion = Vec3f.POSITIVE_X.getDegreesQuaternion(90);
        }
        Vec3f vec3f = new Vec3f(-1.0f, -1.0f, 0.0f);
        vec3f.rotate(quaternion);
        Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0f, -1.0f, 0.0f), new Vec3f(-1.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f), new Vec3f(1.0f, -1.0f, 0.0f)};
        float j = this.getSize(tickDelta);
        for (int k = 0; k < 4; ++k) {
            Vec3f vec3f2 = vec3fs[k];
            vec3f2.rotate(quaternion);
            vec3f2.scale(j);
            vec3f2.add(f, g, h);
        }
        float l = this.getMinU();
        float m = this.getMaxU();
        float n = this.getMinV();
        float o = this.getMaxV();
        int p = this.getBrightness(tickDelta);
        vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();

        vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<HeartUpgradeParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(HeartUpgradeParticleEffect heartUpgradeParticleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new HeartUpgradeParticle(clientWorld, d, e, f, g, heartUpgradeParticleEffect.owner(), heartUpgradeParticleEffect.height(), heartUpgradeParticleEffect.width(), this.spriteProvider);
        }
    }
}

