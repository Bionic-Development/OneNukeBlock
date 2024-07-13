package de.takacick.onegirlboyblock.registry.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onegirlboyblock.registry.entity.projectiles.TetrisEntity;
import de.takacick.onegirlboyblock.registry.entity.projectiles.model.TetrisModel;
import de.takacick.onegirlboyblock.registry.entity.projectiles.renderer.TetrisEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class TetrisParticle
        extends Particle {

    private float scale;
    private float yaw;
    private float pitch;
    private float prevYaw;
    private final TetrisEntity.Variant tetris;

    protected TetrisParticle(ClientWorld world, int variant, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);

        this.velocityX = velocityX * 0.4;
        this.velocityY = velocityY * 0.4;
        this.velocityZ = velocityZ * 0.4;
        this.velocityMultiplier = 1f;
        this.gravityStrength = 0f;

        int length = TetrisEntity.Variant.values().length;
        this.tetris = variant < length && variant >= 0 ? TetrisEntity.Variant.byId(variant) : TetrisEntity.Variant.byId(this.random.nextInt(length));

        this.pitch = this.random.nextInt(4) * 90f;
        this.scale = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.yaw = world.getRandom().nextFloat() * 360f - 180f;
        this.prevYaw = this.yaw;
        this.maxAge = 13 + this.random.nextInt(4);
    }

    @Override
    public void tick() {

        this.prevYaw = this.yaw;
        this.yaw += 15f;

        super.tick();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    public float getSize(float tickDelta) {
        return (1f - MathHelper.getLerpProgress(this.age + tickDelta, 0f, this.maxAge)) * this.scale;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(f, g, h);

        matrixStack.translate(0, -0.75, 0);
        float scale = getSize(tickDelta);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, 0.75, 0);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, this.prevYaw, this.yaw)));
        matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(this.pitch));

        List<Integer> list = new ArrayList<>();
        for (int d = 0; d < 4; d++) {
            list.add(d, RenderSystem.getShaderTexture(d));
        }

        VertexConsumerProvider.Immediate vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        Identifier identifier = TetrisEntityRenderer.TEXTURES.get(this.tetris);
        TetrisModel tetrisModel = TetrisEntityRenderer.MODELS.get(this.tetris);

        if (identifier != null && tetrisModel != null) {
            tetrisModel.render(matrixStack, vertexConsumerProvider.getBuffer(tetrisModel.getLayer(identifier)), 0xF000F0, OverlayTexture.DEFAULT_UV);
        }

        vertexConsumerProvider.drawCurrentLayer();

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        for (int d = 0; d < 4; d++) {
            RenderSystem.setShaderTexture(d, list.get(d));
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShader(GameRenderer::getParticleProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<TetrisParticleEffect> {

        public Factory() {
        }

        @Override
        public Particle createParticle(TetrisParticleEffect parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new TetrisParticle(clientWorld, parameters.tetrisVariant(), d, e, f, g, h, i);
        }
    }
}

