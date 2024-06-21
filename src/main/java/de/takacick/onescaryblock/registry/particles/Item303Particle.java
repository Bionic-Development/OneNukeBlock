package de.takacick.onescaryblock.registry.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.onescaryblock.client.hud.CursedTextRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

@Environment(value = EnvType.CLIENT)
public class Item303Particle extends AnimatedParticle {

    private final Quaternionf rotation = new Quaternionf();
    private final TextRenderer textRenderer = new CursedTextRenderer(MinecraftClient.getInstance().textRenderer);
    private final Text text;

    Item303Particle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25f);
        this.collidesWithWorld = false;
        this.gravityStrength = 0;
        this.velocityMultiplier = 1f;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.35f;
        this.maxAge = 20 + this.random.nextInt(12);
        this.text = Text.of(world.getRandom().nextDouble() <= 0.2 ? "303" : world.getRandom().nextDouble() <= 0.3 ? "0" : "3");
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        this.getRotator().setRotation(this.rotation, camera, tickDelta);

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        if (tessellator.getBuffer().isBuilding()) {
            return;
        }

        VertexConsumerProvider.Immediate vertexConsumerProvider = VertexConsumerProvider.immediate(tessellator.getBuffer());
        DrawContext drawContext = new DrawContext(MinecraftClient.getInstance(), vertexConsumerProvider);
        MatrixStack matrixStack = drawContext.getMatrices();
        matrixStack.translate(f, g + 0.1f, h);
        matrixStack.multiply(this.rotation);

        float scale = getSize(tickDelta);

        matrixStack.scale(-scale, -scale, scale);

        List<Integer> list = new ArrayList<>();

        for (int d = 0; d < 4; d++) {
            list.add(d, RenderSystem.getShaderTexture(d));
        }

        drawContext.drawCenteredTextWithShadow(this.textRenderer,
                this.text, 0, (int) (this.textRenderer.fontHeight * -0.5), 0xFFFFFF);
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
            return new Item303Particle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
