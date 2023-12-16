package de.takacick.tinyhouse.registry.particles.goop;


import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;

public class GoopDropParticle extends SpriteBillboardParticle {
    protected final SpriteProvider spriteProvider;
    protected final Vector3f color;
    float rotSpeed;
    float totalScale;

    protected GoopDropParticle(ClientWorld clientWorld, Vec3d pos, Vec3d vel, SpriteProvider spriteProvider, Vector3f color, float scale) {
        super(clientWorld, pos.x, pos.y, pos.z);
        setColor(color.x(), color.y(), color.z());
        this.color = color;
        this.scale = scale - (scale > 1 ? 1.25f * (scale / 2) : 0f);
        totalScale = scale;
        this.spriteProvider = spriteProvider;
        sprite = spriteProvider.getSprite(random);
        gravityStrength = 1 + scale / 2;
        maxAge = 300;
        setVelocity(random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5, random.nextFloat() * 0.5 - 0.25);
        collidesWithWorld = true;

        rotSpeed = (random.nextFloat() - 0.5f) * 0.25f;

        if (vel.distanceTo(Vec3d.ZERO) > 0)
            setVelocity(vel.x, vel.y, vel.z);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        prevAngle = angle;
        angle += rotSpeed;
    }

    void nextParticle(BlockPos pos, Vec3d dir) {
        if (!world.getBlockState(pos).isAir()) {
            dir = dir.normalize();

            Vec3d offset = new Vec3d(1, 1, 1).multiply(Math.max(random.nextFloat() * 0.02f, 0.01f));
            offset = offset.add(dir.x < 0 ? 0 : 1, dir.y < 0 ? 0 : 1, dir.z < 0 ? 0 : 1);

            if (dir.y != 0) {
                world.addParticle(new GoopParticleEffect(color, totalScale * 2.5f, dir),
                        x + dir.x * offset.x, pos.getY() + dir.y * offset.y, z + dir.z * offset.z,
                        0, 0, 0);

                world.playSound(x + dir.x * offset.x, pos.getY() + dir.y * offset.y, z + dir.z * offset.z,
                        SoundEvents.BLOCK_MUD_BREAK, SoundCategory.BLOCKS, totalScale, 1f, false);
            } else if (dir.x != 0) {
                world.addParticle(new GoopParticleEffect(color, totalScale * 2.5f, dir),
                        pos.getX() + dir.x * offset.x, y + dir.y * offset.y, z + dir.z * offset.z,
                        0, 0, 0);
                world.playSound(pos.getX() + dir.x * offset.x, y + dir.y * offset.y, z + dir.z * offset.z,
                        SoundEvents.BLOCK_MUD_BREAK, SoundCategory.BLOCKS, totalScale, 1f, false);
            } else if (dir.z != 0) {
                world.addParticle(new GoopParticleEffect(color, totalScale * 2.5f, dir),
                        x + dir.x * offset.x, y + dir.y * offset.y, pos.getZ() + dir.z * offset.z,
                        0, 0, 0);
                world.playSound(x + dir.x * offset.x, y + dir.y * offset.y, pos.getZ() + dir.z * offset.z,
                        SoundEvents.BLOCK_MUD_BREAK, SoundCategory.BLOCKS, totalScale, 1f, false);
            }
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternionf quaternion;
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        if (this.angle == 0.0f) {
            quaternion = camera.getRotation();
        } else {
            quaternion = new Quaternionf(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            quaternion.rotateY(i);
        }
        Vector3f vec3f = new Vector3f(-1.0f, -1.0f, 0.0f);
        vec3f.rotate(quaternion);
        Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        float j = this.getSize(tickDelta);
        for (int k = 0; k < 4; ++k) {
            Vector3f vec3f2 = vec3fs[k];
            vec3f2.rotate(quaternion);
            vec3f2.mul(j);
            vec3f2.add(f, g, h);
        }
        float l = this.getMinU();
        float m = this.getMaxU();
        float n = this.getMinV();
        float o = this.getMaxV();
        int p = this.getBrightness(tickDelta);

        vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
    }

    @Override
    public void move(double dx, double dy, double dz) {
        if (this.collidesWithWorld && (dx != 0.0 || dy != 0.0 || dz != 0.0) && dx * dx + dy * dy + dz * dz < MathHelper.square(100.0)) {
            Iterator<VoxelShape> it = world.getBlockCollisions(null, getBoundingBox().stretch(new Vec3d(dx, dy, dz))).iterator();
            if (it.hasNext()) {
                VoxelShape shape = it.next();
                Vec3d point = shape.getBoundingBox().getCenter();
                Vec3d vec3d = Entity.adjustMovementForCollisions(null, new Vec3d(dx, dy, dz), this.getBoundingBox(), this.world, List.of());
                Vec3d diff = vec3d.subtract(new Vec3d(dx, dy, dz));

                nextParticle(BlockPos.ofFloored(point), diff);
                markDead();
            }
        }
        super.move(dx, dy, dz);
    }

    public static class GoopDropParticleFactory implements ParticleFactory<GoopDropParticleEffect> {
        protected final SpriteProvider spriteProvider;

        public GoopDropParticleFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(GoopDropParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new GoopDropParticle(world, new Vec3d(x, y, z), new Vec3d(velocityX, velocityY, velocityZ),
                    spriteProvider, parameters.getColor(), parameters.getScale());
        }
    }
}