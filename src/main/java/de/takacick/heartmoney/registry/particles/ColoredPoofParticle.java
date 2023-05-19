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

public class ColoredPoofParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    public boolean glow = true;

    protected ColoredPoofParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        this.gravityStrength = -0.1f;
        this.velocityMultiplier = 0.9f;
        this.spriteProvider = spriteProvider;
        this.velocityX = velocityX + (Math.random() * 2.0 - 1.0) * (double) 0.05f;
        this.velocityY = velocityY + (Math.random() * 2.0 - 1.0) * (double) 0.05f;
        this.velocityZ = velocityZ + (Math.random() * 2.0 - 1.0) * (double) 0.05f;
        this.scale = 0.1f * (this.random.nextFloat() * this.random.nextFloat() * 6.0f + 1.0f);
        this.maxAge = (int) (16.0 / ((double) this.random.nextFloat() * 0.8 + 0.2)) + 2;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (!glow) {
            super.buildGeometry(vertexConsumer, camera, tickDelta);
            return;
        }

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
    public static class Factory
            implements ParticleFactory<ColoredParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(ColoredParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            ColoredPoofParticle poofParticle = new ColoredPoofParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            poofParticle.setSprite(this.spriteProvider);

            if (particleEffect.color().equals(new Vec3f(Vec3d.unpackRgb(0x820A0B)))) {
                Vec3f color = new Vec3f(Vec3d.unpackRgb(0x820A0A));
                poofParticle.setColor(color.getX(), color.getY(), color.getZ());
                poofParticle.glow = false;
            } else {
                poofParticle.setColor(particleEffect.color().getX(), particleEffect.color().getY(), particleEffect.color().getZ());
            }

            return poofParticle;
        }
    }
}

