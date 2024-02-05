package de.takacick.secretcraftbase.mixin.frog;

import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.secretcraftbase.client.renderer.FrogFeatureRenderer;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.server.utils.EntityNbtHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    private LivingEntity secretcraftbase$tempLivingEntity;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"), cancellable = true)
    public void renderItemHead(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.FROG) && this.secretcraftbase$tempLivingEntity != null) {
            if (this.secretcraftbase$tempLivingEntity.getMainHandStack().equals(stack)) {
                if (renderMode.equals(ModelTransformationMode.THIRD_PERSON_LEFT_HAND) || renderMode.equals(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND)) {
                    info.cancel();
                }
            }
        }
    }


    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE, ordinal = 1))
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.FROG)) {
            matrices.translate(0.5, 1.501, 0.5);

            matrices.scale(1.0f, -1.0f, -1.0f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            Entity entity = EntityNbtHelper.getEntityType(EntityType.FROG, stack).create(MinecraftClient.getInstance().world);
            if (entity instanceof FrogEntity frogEntity) {
                entity.readNbt(EntityNbtHelper.getEntityNbt(stack));

                ModelPart frog = FrogFeatureRenderer.FROG;

                frog.traverse().forEach(ModelPart::resetTransform);

                if (renderMode.equals(ModelTransformationMode.GUI)) {
                    this.secretcraftbase$tempLivingEntity = MinecraftClient.getInstance().player;
                }

                if (this.secretcraftbase$tempLivingEntity instanceof PlayerProperties playerProperties
                        && this.secretcraftbase$tempLivingEntity.getMainHandStack().equals(stack)) {
                    float animationProgress = this.secretcraftbase$tempLivingEntity.age
                            + (MinecraftClient.getInstance().isPaused() ? 0f : MinecraftClient.getInstance().getTickDelta());
                    FrogFeatureRenderer.updateAnimation(frog, playerProperties.getFrogTongueState(),
                            FrogFeatureRenderer.getUsingTongue(playerProperties.getFrogTongueLength()),
                            animationProgress, 1f);
                    this.secretcraftbase$tempLivingEntity = null;
                }

                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(frogEntity.getVariant().texture()));
                frog.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            }
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At("HEAD"))
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.FROG)) {
            this.secretcraftbase$tempLivingEntity = entity;
        }
    }
}