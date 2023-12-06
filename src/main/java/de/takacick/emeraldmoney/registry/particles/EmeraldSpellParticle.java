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
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class EmeraldSpellParticle
        extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;
    private Entity ownerEntity;
    float bodyY;
    private final Item item;
    private final BakedModel bakedModel;
    private Vec3d offset = new Vec3d(0, 0, 0);
    private Vec3d prevOffset = new Vec3d(0, 0, 0);

    private EmeraldSpellParticle(ClientWorld world, int owner, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0, 0, 0);

        this.item = Items.EMERALD;
        this.bakedModel = MinecraftClient.getInstance().getItemRenderer().getModel(item.getDefaultStack(), null, null, 0);
        this.spriteProvider = spriteProvider;
        this.scale(1.5f);
        this.setSpriteForAge(spriteProvider);

        this.velocityMultiplier = 1;
        this.gravityStrength = 1;
        this.collidesWithWorld = false;
        this.scale *= 0.95f;
        this.maxAge = 35 + this.random.nextInt(12);
        this.setSpriteForAge(spriteProvider);

        if (owner >= 0) {
            ownerEntity = world.getEntityById(owner);
            if (ownerEntity != null) {
                this.offset = ownerEntity.getPos().subtract(new Vec3d(x, y, z));
                this.prevOffset = this.offset;
            }
        }
        this.angle = this.random.nextFloat() * 180f;
        this.prevAngle = this.angle;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        this.prevOffset = this.offset;
        this.offset = this.offset.add(new Vec3d(0, this.velocityY*-1, 0).multiply(0.25));

        this.velocityX *= this.velocityMultiplier;
        this.velocityY *= this.velocityMultiplier;
        this.velocityZ *= this.velocityMultiplier;
        this.age = Math.min(this.age + 1, this.maxAge);

        if (this.age >= this.maxAge) {
            this.markDead();
        }
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
        if (ownerEntity != null) {

            Vec3d add = new Vec3d(MathHelper.lerp(tickDelta, this.prevOffset.getX(), this.offset.getX()),
                    MathHelper.lerp(tickDelta, this.prevOffset.getY(), this.offset.getY()),
                    MathHelper.lerp(tickDelta, this.prevOffset.getZ(), this.offset.getZ()));

            Vec3d vec3d = ownerEntity.getLerpedPos(tickDelta).subtract(add);
            setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());

            prevPosX = x;
            prevPosY = y;
            prevPosZ = z;
        }

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
    public static class Factory
            implements ParticleFactory<TargetParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(TargetParticleEffect particleEffect, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new EmeraldSpellParticle(clientWorld, particleEffect.owner(), d, e, f, g, h, i, this.spriteProvider);
        }
    }
}

