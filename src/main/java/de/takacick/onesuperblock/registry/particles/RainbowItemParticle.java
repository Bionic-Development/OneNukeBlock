package de.takacick.onesuperblock.registry.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.superitems.client.CustomLayers;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AscendingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class RainbowItemParticle extends AscendingParticle {

    private Entity ownerEntity;
    private Vec3d offset = new Vec3d(0, 0, 0);

    private int gameTime = 0;
    private int id = 0;
    private boolean ignoreTime = false;
    private int spriteTick = 0;
    private boolean sprite = true;

    protected RainbowItemParticle(ClientWorld world, int gameTime, boolean ignoreTime, double x, double y, double z, double offsetX, double offsetY, double offsetZ, float scaleMultiplier, SpriteProvider spriteProvider) {
        super(world, 0, 0, 0, 0.1f, 0f, 0.1f, 0, 0, 0, scaleMultiplier, spriteProvider, 0.3f, 8, -0.1f, true);
        this.maxAge = 33;
        this.gameTime = gameTime;
        this.ignoreTime = ignoreTime;
        this.red = 0.55f;
        this.green = 0.55f;
        this.blue = 0.55f;

        this.id = world.getRandom().nextInt(11);

        if (offsetX >= 0) {
            ownerEntity = world.getEntityById((int) offsetX);
            if (ownerEntity != null) {
                offset = ownerEntity.getPos().subtract(new Vec3d(x, y, z));
            }
        }
    }

    @Override
    public void tick() {
        if (this.sprite) {
            this.spriteTick++;
        } else {
            this.spriteTick--;
        }

        if (this.spriteTick >= 11) {
            this.sprite = false;
        } else if (this.spriteTick <= 0) {
            this.sprite = true;
        }

        super.tick();
    }

    @Override
    public void setSpriteForAge(SpriteProvider spriteProvider) {
        if (!this.dead) {
            this.setSprite(spriteProvider.getSprite((this.spriteTick + this.id) % 11, 10));
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (ownerEntity != null) {
            Vec3d vec3d = ownerEntity.getLerpedPos(tickDelta).subtract(offset);
            setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());

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
        RenderSystem.setShaderGameTime(gameTime + (!ignoreTime ? this.world.getTime() : 0), tickDelta);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        if (tessellator.getBuffer().isBuilding()) {
            return;
        }

        List<Integer> list = new ArrayList<>();

        for (int d = 0; d < 4; d++) {
            list.add(d, RenderSystem.getShaderTexture(d));
        }

        VertexConsumerProvider.Immediate vertexConsumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());

        vertexConsumer = vertexConsumerProvider.getBuffer(CustomLayers.RAINBOW_PARTICLE_CUTOUT.apply(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE));
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
        RenderSystem.setShaderGameTime(this.world.getTime(), tickDelta);
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<RainbowParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(RainbowParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new RainbowItemParticle(clientWorld, particleEffect.offset(), particleEffect.ignoreTime(), d, e, f, g, h, i, 2.0f, this.spriteProvider);
        }
    }
}

