package de.takacick.secretgirlbase.mixin.leadcuffs;

import de.takacick.secretgirlbase.access.LeadCuffProperties;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;"))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {

        if (!camera.isThirdPerson() && camera.getFocusedEntity() instanceof PlayerEntity playerEntity
                && camera.getFocusedEntity() instanceof LeadCuffProperties playerProperties && playerProperties.isLeadCuffed()) {
            matrices.push();
            matrices.translate(0, -1.5, 0);
            VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
            secretgirlbase$renderLeash(playerEntity, tickDelta, matrices, immediate, playerProperties.getLeadCuffedOwner());
            matrices.pop();
        }
    }

    private void secretgirlbase$renderLeash(PlayerEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, LivingEntity holdingEntity) {
        int u;
        matrices.push();
        Vec3d vec3d = holdingEntity.getLeashPos(tickDelta).add(0, -0.375, 0);
        double d = (double) (MathHelper.lerp(tickDelta, entity.prevHeadYaw, entity.headYaw) * ((float) Math.PI / 180)) + 1.5707963267948966;
        Vec3d vec3d2 = entity.getLeashOffset(0f).add(0, -entity.getHeight() * 0.375, entity.getWidth() * 0.15f);
        double e = Math.cos(d) * vec3d2.z + Math.sin(d) * vec3d2.x;
        double f = Math.sin(d) * vec3d2.z - Math.cos(d) * vec3d2.x;
        double g = MathHelper.lerp(tickDelta, entity.prevX, entity.getX()) + e;
        double h = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + vec3d2.y;
        double i = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ()) + f;
        matrices.translate(e, vec3d2.y, f);
        float j = (float) (vec3d.x - g);
        float k = (float) (vec3d.y - h);
        float l = (float) (vec3d.z - i);
        float m = 0.025f;
        VertexConsumer vertexConsumer = provider.getBuffer(RenderLayer.getLeash());
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        float n = (float) (MathHelper.fastInverseSqrt(j * j + l * l) * 0.025f / 2.0f);
        float o = l * n;
        float p = j * n;
        BlockPos blockPos = BlockPos.ofFloored(entity.getCameraPosVec(tickDelta));
        BlockPos blockPos2 = BlockPos.ofFloored(holdingEntity.getCameraPosVec(tickDelta));
        int q = this.secretgirlbase$getCustomBlockLight(entity, blockPos);
        int r = this.secretgirlbase$getCustomBlockLight(holdingEntity, blockPos2);
        int s = entity.getWorld().getLightLevel(LightType.SKY, blockPos);
        int t = entity.getWorld().getLightLevel(LightType.SKY, blockPos2);
        for (u = 0; u <= 24; ++u) {
            secretgirlbase$renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.025f, o, p, u, false);
        }
        for (u = 24; u >= 0; --u) {
            secretgirlbase$renderLeashPiece(vertexConsumer, matrix4f, j, k, l, q, r, s, t, 0.025f, 0.0f, o, p, u, true);
        }
        matrices.pop();
    }

    protected int secretgirlbase$getCustomBlockLight(LivingEntity entity, BlockPos pos) {
        if (entity.isOnFire()) {
            return 15;
        }
        return entity.getWorld().getLightLevel(LightType.BLOCK, pos);
    }

    private static void secretgirlbase$renderLeashPiece(VertexConsumer vertexConsumer, Matrix4f positionMatrix, float f, float g, float h, int leashedEntityBlockLight, int holdingEntityBlockLight, int leashedEntitySkyLight, int holdingEntitySkyLight, float i, float j, float k, float l, int pieceIndex, boolean isLeashKnot) {
        float m = (float) pieceIndex / 24.0f;
        int n = MathHelper.lerp(m, leashedEntityBlockLight, holdingEntityBlockLight);
        int o = MathHelper.lerp(m, leashedEntitySkyLight, holdingEntitySkyLight);
        int p = LightmapTextureManager.pack(n, o);
        float q = pieceIndex % 2 == (isLeashKnot ? 1 : 0) ? 0.7f : 1.0f;
        float r = 0.5f * q;
        float s = 0.4f * q;
        float t = 0.3f * q;
        float u = f * m;
        float v = g > 0.0f ? g * m * m : g - g * (1.0f - m) * (1.0f - m);
        float w = h * m;
        vertexConsumer.vertex(positionMatrix, u - k, v + j, w + l).color(r, s, t, 1.0f).light(p).next();
        vertexConsumer.vertex(positionMatrix, u + k, v + i - j, w - l).color(r, s, t, 1.0f).light(p).next();
    }
}