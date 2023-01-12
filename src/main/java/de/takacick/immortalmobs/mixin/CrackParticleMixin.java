package de.takacick.immortalmobs.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.takacick.immortalmobs.client.CustomLayers;
import de.takacick.immortalmobs.registry.ItemRegistry;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(CrackParticle.class)
public abstract class CrackParticleMixin extends SpriteBillboardParticle {

    protected CrackParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    private ItemStack stack;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void init(ClientWorld world, double x, double y, double z, double d, double e, double f, ItemStack stack, CallbackInfo info) {
        this.stack = stack;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (stack.isOf(ItemRegistry.IMMORTAL_PORKCHOP)) {
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
            vertexConsumer = vertexConsumerProvider.getBuffer(CustomLayers.IMMORTAL_CUTOUT.apply(sprite.getId()));
            vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
            vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
            vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
            vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).color(this.red, this.green, this.blue, this.alpha).texture(l, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();

            vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEyes(sprite.getId()));
            for (int i = 0; i < 1; i++) {
                vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).color(0.4029412f, 0.14705883f, 0.5411765f, 0.2f).texture(m, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).color(0.4029412f, 0.14705883f, 0.5411765f, 0.2f).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).color(0.4029412f, 0.14705883f, 0.5411765f, 0.2f).texture(l, n).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
                vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).color(0.4029412f, 0.14705883f, 0.5411765f, 0.2f).texture(l, o).overlay(OverlayTexture.DEFAULT_UV).light(p).normal(0, 0, 0).next();
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
        } else {
            super.buildGeometry(vertexConsumer, camera, tickDelta);
        }
    }
}
