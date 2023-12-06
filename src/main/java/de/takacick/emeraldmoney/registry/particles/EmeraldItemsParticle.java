package de.takacick.emeraldmoney.registry.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class EmeraldItemsParticle
        extends AnimatedParticle {

    private static final List<Item> items = Arrays.asList(Items.EMERALD_BLOCK, Items.EMERALD,Items.EMERALD, Items.EMERALD, Items.EMERALD_ORE);

    private final Item item;
    private final BakedModel bakedModel;

    EmeraldItemsParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);

        this.item = items.get(random.nextInt(items.size()));
        this.bakedModel = MinecraftClient.getInstance().getItemRenderer().getModel(item.getDefaultStack(), null, null, 0);
        this.collidesWithWorld = false;
        this.gravityStrength = 0;
        this.velocityMultiplier = 0.99f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.95f;
        this.maxAge = 60 + this.random.nextInt(12);
        this.angle = this.random.nextFloat() * 180f;
        this.prevAngle = this.angle;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.velocityY *= 0.8f;
        setAlpha(Math.max(0f, 1 - (float) age / ((float) getMaxAge())));

        this.prevAngle = this.angle;
        this.angle += 0.1f;

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public float getSize(float tickDelta) {
        return (1 - (age + tickDelta) / ((float) getMaxAge())) * this.scale;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternionf quaternionf;
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        quaternionf = new Quaternionf();
        quaternionf.rotateY(MathHelper.lerp(tickDelta, this.prevAngle, this.angle));

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(f, g + 0.1f, h);
        matrixStack.multiply(quaternionf);

        float scale = getSize(tickDelta);

        matrixStack.scale(scale, scale, scale);

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        if (tessellator.getBuffer().isBuilding()) {
            return;
        }
        List<Integer> list = new ArrayList<>();

        for (int d = 0; d < 4; d++) {
            list.add(d, RenderSystem.getShaderTexture(d));
        }

        VertexConsumerProvider.Immediate vertexConsumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());

        MinecraftClient.getInstance().getItemRenderer()
                .renderItem(this.item.getDefaultStack(),
                        ModelTransformationMode.NONE, false, matrixStack,
                        vertexConsumerProvider, 0xF000F0, OverlayTexture.DEFAULT_UV,
                        bakedModel);
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
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Environment(value = EnvType.CLIENT)
    public record Factory(SpriteProvider spriteProvider)
            implements ParticleFactory<DefaultParticleType> {
        @Override
        public Particle createParticle(DefaultParticleType particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EmeraldItemsParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
