package de.takacick.deathmoney.registry.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.deathmoney.client.CustomLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class BlackMatterExplosionParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    BlackMatterExplosionParticle(ClientWorld clientWorld, double d, double e, double f, double g, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, 0.0, 0.0, 0.0);

        this.maxAge = 6 + this.random.nextInt(4);
        this.scale = 2.0f * (1.0f - (float) g * 0.5f);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternion quaternion;
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp((double) tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp((double) tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp((double) tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
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

        vertexConsumer = vertexConsumerProvider.getBuffer(CustomLayers.BLACK_MATTER_CUTOUT.apply(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE));
        vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
        vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
        vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
        vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();

        vertexConsumerProvider.draw();
        tessellator.getBuffer().clear();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);

        for (int d = 0; d < 4; d++) {
            RenderSystem.setShaderTexture(d, list.get(d));
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getParticleShader);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new BlackMatterExplosionParticle(clientWorld, d, e, f, g, this.spriteProvider);
        }
    }
}