package de.takacick.onegirlboyblock.registry.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class GoldNuggetParticle
        extends Particle implements ItemParticle {

    public HashMap<ModelTransformationMode, MatrixStack.Entry> entryHashMap = new HashMap<>();
    private ModelTransformationMode modelTransformationMode;

    private float scale;
    private float yaw;
    private float prevYaw;

    private static final Item ITEM = Items.GOLD_NUGGET;
    private final BakedModel bakedModel;

    protected GoldNuggetParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);

        this.velocityX = velocityX * 0.1;
        this.velocityY = velocityY * 0.1;
        this.velocityZ = velocityZ * 0.1;
        this.velocityMultiplier = 1f;
        this.gravityStrength = 0f;

        this.bakedModel = MinecraftClient.getInstance().getItemRenderer().getModel(ITEM.getDefaultStack(), null, null, 0);

        this.scale = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
        this.yaw = world.getRandom().nextFloat() * 360f - 180f;
        this.prevYaw = this.yaw;
        this.maxAge = (int)(5.0f / (this.random.nextFloat() * 0.9f + 0.1f));
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
        MatrixStack.Entry entry = this.entryHashMap.get(this.modelTransformationMode);
        if (entry == null) {
            return;
        }

        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x));
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y));
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z));

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(entry.getPositionMatrix());
        matrixStack.translate(f, g + 0.1f, h);

        matrixStack.translate(0, -0.5, 0);
        float scale = getSize(tickDelta);
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0, 0.5, 0);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, this.prevYaw, this.yaw)));

        List<Integer> list = new ArrayList<>();
        for (int d = 0; d < 4; d++) {
            list.add(d, RenderSystem.getShaderTexture(d));
        }

        VertexConsumerProvider.Immediate vertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        MinecraftClient.getInstance().getItemRenderer()
                .renderItem(ITEM.getDefaultStack(),
                        ModelTransformationMode.NONE, false, matrixStack,
                        vertexConsumerProvider, 0xF000F0, OverlayTexture.DEFAULT_UV,
                        bakedModel);
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

    @Override
    public void setMatrixEntry(MatrixStack.Entry entry, ModelTransformationMode modelTransformationMode, boolean force) {
        if (!this.entryHashMap.containsKey(modelTransformationMode) || !modelTransformationMode.equals(ModelTransformationMode.GUI) || force) {
            this.entryHashMap.put(modelTransformationMode, entry);
        }
        this.modelTransformationMode = modelTransformationMode;
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
            implements ParticleFactory<SimpleParticleType> {

        public Factory() {
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new GoldNuggetParticle(clientWorld, d, e, f, g, h, i);
        }
    }
}

