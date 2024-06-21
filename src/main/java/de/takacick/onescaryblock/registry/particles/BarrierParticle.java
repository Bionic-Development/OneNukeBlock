package de.takacick.onescaryblock.registry.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(value = EnvType.CLIENT)
public class BarrierParticle extends AnimatedParticle {
    private final Quaternionf rotation = new Quaternionf();

    BarrierParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);
        this.collidesWithWorld = false;
        this.gravityStrength = 0;
        this.velocityMultiplier = 1f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.45f;
        this.maxAge = 15 + this.random.nextInt(12);

        setSpriteForAge(spriteProvider);
        setColor(2138367);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        super.tick();
        this.velocityY *= 0.8f;
        setAlpha(Math.max(0f, 1 - (float) age / ((float) getMaxAge())));

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public float getSize(float tickDelta) {
        return (1 - (age + tickDelta) / ((float) getMaxAge())) * this.scale;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(f, g + 0.1f, h);
        matrixStack.multiply(this.rotation);
        this.getRotator().setRotation(this.rotation, camera, tickDelta);

        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        float i = this.getSize(tickDelta);
        for (int j = 0; j < 4; ++j) {
            Vector3f vector3f = vector3fs[j];
            vector3f.mul(i);
        }
        float k = this.getMinU();
        float l = this.getMaxU();
        float m = this.getMinV();
        float n = this.getMaxV();

        Tessellator tessellator = Tessellator.getInstance();
        if (tessellator.getBuffer().isBuilding()) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        RenderSystem.setShaderColor(this.red, this.green, this.blue, this.alpha);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.polygonOffset(-3.0f, -3.0f);
        RenderSystem.enablePolygonOffset();
        RenderSystem.disableCull();

        matrixStack.push();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        bufferBuilder.vertex(matrix4f, vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(l, n).next();
        bufferBuilder.vertex(matrix4f, vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(l, m).next();
        bufferBuilder.vertex(matrix4f, vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(k, m).next();
        bufferBuilder.vertex(matrix4f, vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(k, n).next();

        matrixStack.push();

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.enableCull();
        RenderSystem.polygonOffset(0.0f, 0.0f);
        RenderSystem.disablePolygonOffset();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<DefaultParticleType> {
        @Override
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new BarrierParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
