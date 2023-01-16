package de.takacick.imagineanything.mixin;

import de.takacick.imagineanything.access.LivingProperties;
import de.takacick.imagineanything.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T>
        implements FeatureRendererContext<T, M> {

    private static final Identifier EXPLOSION_BEAM_TEXTURE = new Identifier("textures/entity/guardian_beam.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(EXPLOSION_BEAM_TEXTURE);

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "shouldFlipUpsideDown", at = @At("HEAD"), cancellable = true)
    private static void shouldFlipUpsideDown(LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
        if (((LivingProperties) entity).getVibratingTicks() > 0) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "isShaking", at = @At("HEAD"), cancellable = true)
    private void isShaking(T entity, CallbackInfoReturnable<Boolean> info) {
        if (((LivingProperties) entity).getVibratingTicks() > 0) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo info) {
        if (((LivingProperties) livingEntity).hasHolder() &&
                ((PlayerProperties) MinecraftClient.getInstance().player).getHolding() == livingEntity.getId() && MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
            AbstractClientPlayerEntity abstractClientPlayerEntity = MinecraftClient.getInstance().player;

            float h = 0;
            float j = (float) abstractClientPlayerEntity.world.getTime() + g;
            float k = j * 0.5f % 1.0f;
            float l = abstractClientPlayerEntity.getStandingEyeHeight() - 1;
            matrixStack.push();
            matrixStack.translate(0.0, l, 0.0);
            Vec3d vec3d = this.fromLerpedPosition(livingEntity, (double) livingEntity.getHeight() * 0.5, g);
            Vec3d vec3d2 = this.fromLerpedPosition(abstractClientPlayerEntity, l, g);
            Vec3d vec3d3 = vec3d2.subtract(vec3d);
            float m = (float) (vec3d3.length() + 1.0);
            vec3d3 = vec3d3.normalize();
            float n = (float) Math.acos(vec3d3.y);
            float o = (float) Math.atan2(vec3d3.z, vec3d3.x);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((1.5707964f - o) * 57.295776f));
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(n * 57.295776f));
            boolean p = true;
            float q = j * 0.05f * -1.5f;
            float r = h * h;
            Vec3f vec3f = new Vec3f(Vec3d.unpackRgb(0x9D00FF));
            int s = (int) (vec3f.getX() * 255);
            int t = (int) (vec3f.getY() * 255);
            int u = (int) (vec3f.getZ() * 255);
            float v = 0.2f;
            float w = 0.282f;
            float x = MathHelper.cos(q + 2.3561945f) * 0.282f;
            float y = MathHelper.sin(q + 2.3561945f) * 0.282f;
            float z = MathHelper.cos(q + 0.7853982f) * 0.282f;
            float aa = MathHelper.sin(q + 0.7853982f) * 0.282f;
            float ab = MathHelper.cos(q + 3.926991f) * 0.282f;
            float ac = MathHelper.sin(q + 3.926991f) * 0.282f;
            float ad = MathHelper.cos(q + 5.4977875f) * 0.282f;
            float ae = MathHelper.sin(q + 5.4977875f) * 0.282f;
            float af = MathHelper.cos(q + (float) Math.PI) * 0.2f;
            float ag = MathHelper.sin(q + (float) Math.PI) * 0.2f;
            float ah = MathHelper.cos(q + 0.0f) * 0.2f;
            float ai = MathHelper.sin(q + 0.0f) * 0.2f;
            float aj = MathHelper.cos(q + 1.5707964f) * 0.2f;
            float ak = MathHelper.sin(q + 1.5707964f) * 0.2f;
            float al = MathHelper.cos(q + 4.712389f) * 0.2f;
            float am = MathHelper.sin(q + 4.712389f) * 0.2f;
            float an = m;
            float ao = 0.0f;
            float ap = 0.4999f;
            float aq = -1.0f + k;
            float ar = m * 2.5f + aq;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
            MatrixStack.Entry entry = matrixStack.peek();
            Matrix4f matrix4f = entry.getPositionMatrix();
            Matrix3f matrix3f = entry.getNormalMatrix();
            beamVertex(vertexConsumer, matrix4f, matrix3f, af, an, ag, s, t, u, 0.4999f, ar);
            beamVertex(vertexConsumer, matrix4f, matrix3f, af, 0.0f, ag, s, t, u, 0.4999f, aq);
            beamVertex(vertexConsumer, matrix4f, matrix3f, ah, 0.0f, ai, s, t, u, 0.0f, aq);
            beamVertex(vertexConsumer, matrix4f, matrix3f, ah, an, ai, s, t, u, 0.0f, ar);
            beamVertex(vertexConsumer, matrix4f, matrix3f, aj, an, ak, s, t, u, 0.4999f, ar);
            beamVertex(vertexConsumer, matrix4f, matrix3f, aj, 0.0f, ak, s, t, u, 0.4999f, aq);
            beamVertex(vertexConsumer, matrix4f, matrix3f, al, 0.0f, am, s, t, u, 0.0f, aq);
            beamVertex(vertexConsumer, matrix4f, matrix3f, al, an, am, s, t, u, 0.0f, ar);
            float as = 0.0f;
            if (abstractClientPlayerEntity.age % 2 == 0) {
                as = 0.5f;
            }
            beamVertex(vertexConsumer, matrix4f, matrix3f, x, an, y, s, t, u, 0.5f, as + 0.5f);
            beamVertex(vertexConsumer, matrix4f, matrix3f, z, an, aa, s, t, u, 1.0f, as + 0.5f);
            beamVertex(vertexConsumer, matrix4f, matrix3f, ad, an, ae, s, t, u, 1.0f, as);
            beamVertex(vertexConsumer, matrix4f, matrix3f, ab, an, ac, s, t, u, 0.5f, as);
            matrixStack.pop();
        }
    }

    private static void beamVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, float x, float y, float z, int red, int green, int blue, float u, float v) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(red, green, blue, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }

    private Vec3d fromLerpedPosition(LivingEntity entity, double yOffset, float delta) {
        double d = MathHelper.lerp((double) delta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp((double) delta, entity.lastRenderY, entity.getY()) + yOffset;
        double f = MathHelper.lerp((double) delta, entity.lastRenderZ, entity.getZ());
        return new Vec3d(d, e, f);
    }
}