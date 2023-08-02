package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    @Final
    private BuiltinModelItemRenderer builtinModelItemRenderer;

    private LivingEntity onedeathblock$tempLivingEntity;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE, ordinal = 1), cancellable = true)
    public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {

        if (stack.isOf(ItemRegistry.DEADLY_SUPER_MAGNET) && this.onedeathblock$tempLivingEntity != null) {
            matrices.push();
            this.builtinModelItemRenderer.render(stack, renderMode, matrices, vertexConsumers, light, overlay);

            Random random = Random.create();
            if (stack.equals(this.onedeathblock$tempLivingEntity.getActiveItem())) {
                int lightning = Math.min(this.onedeathblock$tempLivingEntity.getItemUseTime(), 80) / 4;
                for (int i = 0; i < random.nextBetween(1, lightning + 1); i++) {

                    float progress = Math.min(MathHelper.getLerpProgress(this.onedeathblock$tempLivingEntity.getItemUseTime(), 0, 80), 1f);

                    float x = random.nextBoolean() ? 0.21875f : -0.21875f;
                    matrices.push();
                    matrices.translate(0.5, 0, 0.5);

                    matrices.translate(x + random.nextGaussian() * 0.15625, 0.875 + random.nextDouble() * -0.1, random.nextGaussian() * 0.01);
                    matrices.scale(-0.006f * progress, -0.006f * progress, 0.006f * progress);
                    matrices.multiply(Quaternion.fromEulerXyz((float) Random.create().nextGaussian(), (float) Random.create().nextGaussian(), (float) Random.create().nextGaussian()));
                    MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
                    matrices.pop();
                }
            }

            matrices.pop();
            this.onedeathblock$tempLivingEntity = null;
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.DEADLY_SUPER_MAGNET)) {
            this.onedeathblock$tempLivingEntity = entity;
        }
    }
}