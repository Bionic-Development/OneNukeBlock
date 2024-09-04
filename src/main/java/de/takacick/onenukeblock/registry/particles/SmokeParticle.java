package de.takacick.onenukeblock.registry.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AscendingParticle;
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
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;

@Environment(value = EnvType.CLIENT)
public class SmokeParticle extends AscendingParticle implements ItemParticle {

    public HashMap<ModelTransformationMode, MatrixStack.Entry> entryHashMap = new HashMap<>();
    private ModelTransformationMode modelTransformationMode;

    private float yaw;
    private float prevYaw;

    protected SmokeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.1f, 0.1f, 0.1f, velocityX, velocityY, velocityZ, scaleMultiplier * 0.75f, spriteProvider, 0.3f, 8, -0.1f, false);
        this.yaw = world.getRandom().nextFloat() * 360f - 180f;
        this.prevYaw = this.yaw;
    }

    @Override
    public void setMatrixEntry(MatrixStack.Entry entry, ModelTransformationMode modelTransformationMode, boolean force) {
        if (!this.entryHashMap.containsKey(modelTransformationMode) || !modelTransformationMode.equals(ModelTransformationMode.GUI) || force) {
            this.entryHashMap.put(modelTransformationMode, entry);
        }
        this.modelTransformationMode = modelTransformationMode;
    }

    @Override
    protected int getBrightness(float tint) {
        int i = 15;
        int j = 0;

        return i << 20 | j << 4;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (this.modelTransformationMode == null) {
            super.buildGeometry(vertexConsumer, camera, tickDelta);
            return;
        }
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
        int o = getBrightness(tickDelta);

        for (int rot = 0; rot <= 1; rot++) {

            quaternionf = new Quaternionf(0f, 0, 0f, 1f);
            quaternionf.mul(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, this.prevYaw, this.yaw) + (rot == 0 ? 0f : -180f)));
            quaternionf.mul(RotationAxis.POSITIVE_Z.rotation(MathHelper.lerp(tickDelta, this.prevAngle, this.angle)));

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

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new SmokeParticle(clientWorld, d, e, f, g, h, i, 1.0f, this.spriteProvider);
        }
    }
}

