package de.takacick.raidbase.mixin;

import de.takacick.raidbase.access.PlayerProperties;
import de.takacick.raidbase.client.renderer.SlimeSuitFeatureRenderer;
import de.takacick.raidbase.registry.block.entity.PieLauncherBlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow
    private @Nullable ClientWorld world;

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;
    @Unique
    private static final Identifier raidbase$TEXTURE = new Identifier("textures/entity/slime/slime.png");

    @Unique
    private static final ModelPart raidbase$SLIME_SUIT = SlimeEntityModel.getOuterTexturedModelData().createModel();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;"))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        if (!camera.isThirdPerson()
                && camera.getFocusedEntity() instanceof LivingEntity livingEntity
                && livingEntity instanceof PlayerProperties playerProperties
                && playerProperties.hasSlimeSuit()) {

            if (livingEntity.isInvisible()) {
                return;
            }

            matrices.push();
            Vec3d vec3d = camera.getPos();
            double d = vec3d.getX();
            double e = vec3d.getY();
            double f = vec3d.getZ();


            VertexConsumerProvider.Immediate vertexConsumerProvider = SlimeSuitFeatureRenderer.SLIME_SUIT;
            d = MathHelper.lerp(tickDelta, livingEntity.lastRenderX, livingEntity.getX()) - d;
            e = MathHelper.lerp(tickDelta, livingEntity.lastRenderY, livingEntity.getY()) - e;
            f = MathHelper.lerp(tickDelta, livingEntity.lastRenderZ, livingEntity.getZ()) - f;

            matrices.translate(d, e, f);

            float h = 1f;
            float c = MathHelper.lerp(tickDelta, playerProperties.getSlimeSuitLastStretch(),
                    playerProperties.getSlimeSuitStretch()) / (h * 0.5f + 1.0f);
            float j = 1.0f / (c + 1.0f);

            float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(bodyYaw));

            matrices.scale(j * h, 1.0f / j * h, j * h);

            matrices.scale(5, 5, 5);
            matrices.translate(0, -1.0, 0);
            matrices.translate(0, 0.001, 0);

            int light = this.entityRenderDispatcher.getLight(livingEntity, tickDelta);
            raidbase$SLIME_SUIT.render(matrices, vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(raidbase$TEXTURE)),
                    light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            matrices.pop();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.BEFORE, ordinal = 3))
    public void renderTail(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        SlimeSuitFeatureRenderer.SLIME_SUIT.draw();
    }

    @Inject(method = "processWorldEvent", at = @At("HEAD"), cancellable = true)
    public void processWorldEvent(int eventId, BlockPos blockPos, int data, CallbackInfo info) {
        if (eventId == 634123) {

            if (data == 0) {
                if (this.world.getBlockEntity(blockPos) instanceof PieLauncherBlockEntity pieLauncherBlockEntity) {
                    pieLauncherBlockEntity.getShootState().start(pieLauncherBlockEntity.getAge());
                }
            }

            info.cancel();
        }
    }

}
