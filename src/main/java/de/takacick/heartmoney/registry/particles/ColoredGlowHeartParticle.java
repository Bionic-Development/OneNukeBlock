package de.takacick.heartmoney.registry.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class ColoredGlowHeartParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    ColoredGlowHeartParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityMultiplier = 0.96f;
        this.field_28787 = true;
        this.spriteProvider = spriteProvider;
        this.scale *= 0.75f;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getBrightness(float tint) {
        float f = ((float) this.age + tint) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        int i = super.getBrightness(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int) (f * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    public void tick() {
        super.tick();
        setAlpha(Math.max(0f, 1 - (float) age / ((float) getMaxAge() - 30f)));

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternion quaternion;
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        if (this.angle == 0.0f) {
            quaternion = camera.getRotation();
        } else {
            quaternion = new Quaternion(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
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

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<ColoredParticleEffect> {

        @Override
        public Particle createParticle(ColoredParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ColoredGlowHeartParticle coloredSparkParticle = new ColoredGlowHeartParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            coloredSparkParticle.setColor(particleEffect.color().getX(), particleEffect.color().getY(), particleEffect.color().getZ());
            return coloredSparkParticle;
        }
    }
}

