package de.takacick.onescaryblock.registry.particles;

import de.takacick.onescaryblock.client.utils.LightningRenderer;
import de.takacick.onescaryblock.registry.entity.custom.renderer.HerobrineLightningEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class HerobrineLightningParticle
        extends SpriteBillboardParticle {

    private final float yaw;
    private final float pitch;
    private int length = 10;

    protected HerobrineLightningParticle(ClientWorld world, double x, double y, double z, double d, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.maxAge = 1 + this.random.nextInt(4);
        Vector3f color = Vec3d.unpackRgb(0x77ebf9).toVector3f();
        setColor(color.x(), color.y(), color.z());

        this.scale = 2.0f * (1.0f - (float) d * 0.5f);
        this.setSpriteForAge(spriteProvider);

        this.yaw = world.getRandom().nextFloat() * 360f - 180f;
        this.pitch = world.getRandom().nextFloat() * 360f - 180f;

        Vec3d velocity = getRotationVector(this.yaw, this.pitch).multiply(0.1);
        setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public void tick() {
        if (this.length > 1) {
            this.length -= 2;
        }

        super.tick();
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(f, g + 0.1f, h);
        matrixStack.scale(0.0194f, 0.0194f, 0.0194f);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.yaw));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.pitch));
        for (int i = 0; i < 1; i++) {
            LightningRenderer.renderLightning(matrixStack, immediate, HerobrineLightningEntityRenderer.LIGHTNING_COLOR_HEX, this.random.nextLong(), 70f, 3f, this.length, 6f);
        }
        immediate.draw();
    }

    public final Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new HerobrineLightningParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}

