package de.takacick.everythinghearts.registry.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class HeartShockwaveParticle extends ExplosionLargeParticle {

    public static final List<Vec3f> colors = List.of(new Vec3f(Vec3d.unpackRgb(0xFF1313)));

    private float distance = 0f;
    private float width = 0f;
    private float lastWidth = 0f;
    private float lastYaw = 0f;
    private float yaw = 0f;
    private float pitch = 0f;

    protected HeartShockwaveParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, float distance, int maxAge, float width, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, g, spriteProvider);
        this.maxAge = 16;
        this.scale = 1.5f;
        this.setSpriteForAge(spriteProvider);
        this.distance = distance;
        this.lastWidth = 0;
        this.width = width;
        this.maxAge = maxAge;
        this.lastYaw = (float) i;
        this.yaw = (float) i + 10f;
        this.pitch = (float) h;

        Vec3f vec3f = colors.get(Random.create().nextInt(colors.size()));

        setColor(vec3f.getX(), vec3f.getY(), vec3f.getZ());
    }

    @Override
    public void tick() {
        this.lastWidth = width;
        this.width = width + (distance * age);
        this.lastYaw = yaw;
        this.yaw += 10;
        setAlpha(1 - (float) age / (float) maxAge);
        super.tick();
    }

    @Override
    public float getSize(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.lastWidth, this.width);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternion quaternion;
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        for (int x = 0; x < 2; x++) {
            quaternion = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
            quaternion.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, x == 1 ? lastYaw - 180 : lastYaw, x == 1 ? yaw - 180 : yaw)));
            quaternion.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(x == 1 ? pitch : -pitch));

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

            Tessellator tessellator = RenderSystem.renderThreadTesselator();
            if (tessellator.getBuffer().isBuilding()) {
                return;
            }
            List<Integer> list = new ArrayList<>();

            for (int d = 0; d < 4; d++) {
                list.add(d, RenderSystem.getShaderTexture(d));
            }

            VertexConsumerProvider.Immediate vertexConsumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());
            vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE));
            for (int i = 0; i < 2; i++) {
                vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();

                vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
            }
            vertexConsumerProvider.draw();
            tessellator.getBuffer().clear();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            for (int d = 0; d < 4; d++) {
                RenderSystem.setShaderTexture(d, list.get(d));
            }
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(GameRenderer::getParticleShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<ShockwaveParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(ShockwaveParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new HeartShockwaveParticle(clientWorld, d, e, f, g, h, i, particleEffect.distance(), particleEffect.maxAge(), particleEffect.width(), this.spriteProvider);
        }
    }
}

