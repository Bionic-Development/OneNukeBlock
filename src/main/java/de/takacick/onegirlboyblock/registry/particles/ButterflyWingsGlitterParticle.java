package de.takacick.onegirlboyblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Environment(value = EnvType.CLIENT)
public class ButterflyWingsGlitterParticle
        extends AnimatedParticle implements ItemParticle {

    public static final List<Vector3f> COLORS = Stream.of(
            0xFECAF7,
            0xF8A8FF,
            0xE979FA,
            0xFFE6FA
    ).map(integer -> Vec3d.unpackRgb(integer).toVector3f()).toList();

    private final float rotationSpeed;

    public HashMap<ModelTransformationMode, MatrixStack.Entry> entryHashMap = new HashMap<>();
    private ModelTransformationMode modelTransformationMode;

    private float yaw;

    ButterflyWingsGlitterParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);
        this.velocityMultiplier = 1f;
        this.gravityStrength = 0f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.25f;
        this.maxAge = 11 + this.random.nextInt(4);
        this.setSpriteForAge(spriteProvider);
        this.collidesWithWorld = false;

        float offset = this.random.nextFloat() * 0.1f + 0.9f;
        Vector3f color = COLORS.get(this.random.nextInt(COLORS.size())).mul(offset, new Vector3f(1f,1f,1f));
        setColor(color.x(), color.y(), color.z());

        this.rotationSpeed = ((float) Math.random() - 0.5f) * 0.1f;
        this.angle = (float) Math.random() * ((float) Math.PI * 2);
        this.prevAngle = this.angle;
        this.yaw = this.random.nextFloat() * 360f - 180f;
    }

    @Override
    public void setMatrixEntry(MatrixStack.Entry entry, ModelTransformationMode modelTransformationMode, boolean force) {
        if (!this.entryHashMap.containsKey(modelTransformationMode) || !modelTransformationMode.equals(ModelTransformationMode.GUI) || force) {
            this.entryHashMap.put(modelTransformationMode, entry);
        }
        this.modelTransformationMode = modelTransformationMode;
    }

    @Override
    public void tick() {
        this.prevAngle = this.angle;
        this.angle += (float) Math.PI * this.rotationSpeed * 2.0f;

        super.tick();
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        MatrixStack.Entry entry = this.entryHashMap.get(this.modelTransformationMode);
        if (entry == null) {
            super.buildGeometry(vertexConsumer, camera, tickDelta);
            return;
        }
        Quaternionf quaternionf;

        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x));
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y));
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z));

        float k = this.getMinU();
        float l = this.getMaxU();
        float m = this.getMinV();
        float n = this.getMaxV();
        int o = 0xF000F0;

        for (int rot = 0; rot <= 1; rot++) {

            quaternionf = new Quaternionf(0f, 0, 0f, 1f);
            quaternionf.mul(RotationAxis.POSITIVE_Y.rotationDegrees(this.yaw + (rot == 0 ? 0f : -180f)));
            if (this.angle != 0.0f) {
                quaternionf.rotateZ(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));
            }

            Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
            float i = this.getSize(tickDelta);
            for (int j = 0; j < 4; ++j) {
                Vector3f vector3f = vector3fs[j];
                vector3f.rotate(quaternionf);
                vector3f.mul(i);
                vector3f.add(f, g, h);
            }

            vertexConsumer.vertex(entry.getPositionMatrix(), vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o);
            vertexConsumer.vertex(entry.getPositionMatrix(), vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o);
            vertexConsumer.vertex(entry.getPositionMatrix(), vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o);
            vertexConsumer.vertex(entry.getPositionMatrix(), vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o);
        }
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
            return new ButterflyWingsGlitterParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

