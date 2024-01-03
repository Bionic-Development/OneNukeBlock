package de.takacick.raidbase.registry.particles;

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
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BanShockwaveParticle extends ExplosionLargeParticle {

    private float distance = 0f;
    private float width = 0f;
    private float lastWidth = 0f;
    private float lastYaw = 0f;
    private float yaw = 0f;
    private float pitch = 0f;

    protected BanShockwaveParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, float distance, int maxAge, float width, SpriteProvider spriteProvider) {
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

        setColor(1f, 0f, 0f);
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        this.lastWidth = width;
        this.width = width + (distance * (0.5f - (float) age / (float) maxAge));
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
        Quaternionf quaternion;
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        for (int x = 0; x < 2; x++) {
            quaternion = new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);
            quaternion.mul(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, x == 1 ? lastYaw - 180 : lastYaw, x == 1 ? yaw - 180 : yaw)));
            quaternion.mul(RotationAxis.POSITIVE_X.rotationDegrees(x == 1 ? pitch : -pitch));

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

            Tessellator tessellator = RenderSystem.renderThreadTesselator();
            if (tessellator.getBuffer().isBuilding()) {
                return;
            }
            List<Integer> list = new ArrayList<>();

            for (int d = 0; d < 4; d++) {
                list.add(d, RenderSystem.getShaderTexture(d));
            }

            VertexConsumerProvider.Immediate vertexConsumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());
            vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE));
            vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).color(this.red, this.green, this.blue, this.alpha).texture(m, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
            vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).color(this.red, this.green, this.blue, this.alpha).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
            vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).color(this.red, this.green, this.blue, this.alpha).texture(l, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
            vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).color(this.red, this.green, this.blue, this.alpha).texture(l, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();

            vertexConsumerProvider.draw();
            tessellator.getBuffer().clear();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            for (int d = 0; d < 4; d++) {
                RenderSystem.setShaderTexture(d, list.get(d));
            }
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShader(GameRenderer::getParticleProgram);
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
            return new BanShockwaveParticle(clientWorld, d, e, f, g, h, i, particleEffect.distance(), particleEffect.maxAge(), particleEffect.width(), this.spriteProvider);
        }
    }
}

