package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.access.PlayerProperties;
import de.takacick.tinyhouse.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    private LivingEntity tinyhouse$tempLivingEntity;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE, ordinal = 1))
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {

        if (stack.isOf(ItemRegistry.BLOCK_MAGNET)
                && this.tinyhouse$tempLivingEntity instanceof PlayerProperties playerProperties && playerProperties.getBlockMagnetHolding() >= 0) {
            matrices.push();

            Random random = Random.create();
            int lightning = 15;
            for (int i = 0; i < random.nextBetween(1, lightning + 1); i++) {

                float x = random.nextBoolean() ? 0.21875f : -0.21875f;
                matrices.push();
                matrices.translate(0.5, 0, 0.5);

                matrices.translate(x + random.nextGaussian() * 0.15625, 0.875 + random.nextDouble() * -0.1, random.nextGaussian() * 0.01);
                matrices.scale(0.006f , 0.006f , 0.006f );
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
                matrices.pop();
            }

            matrices.pop();
            this.tinyhouse$tempLivingEntity = null;
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At("HEAD"))
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.BLOCK_MAGNET)) {
            this.tinyhouse$tempLivingEntity = entity;
        }
    }
}