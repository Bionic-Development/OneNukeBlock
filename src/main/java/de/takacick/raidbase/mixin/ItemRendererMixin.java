package de.takacick.raidbase.mixin;

import de.takacick.raidbase.registry.ItemRegistry;
import de.takacick.raidbase.registry.block.entity.PieLauncherBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
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
    private MinecraftClient client;
    private final PieLauncherBlockEntity raidbase$renderPieLauncher = new PieLauncherBlockEntity(BlockPos.ORIGIN, ItemRegistry.PIE_LAUNCHER.getDefaultState());

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.BEFORE, ordinal = 1))
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {

        if (stack.isOf(ItemRegistry.BUCKET_OF_ELECTRO_WATER)) {
            matrices.push();

            Random random = Random.create();
            for (int i = 0; i < 1; i++) {

                float x = random.nextBoolean() ? 0.21875f : -0.21875f;
                matrices.push();
                matrices.translate(0.5, 0, 0.5);

                matrices.translate(x + random.nextGaussian() * 0.15625, 0.875 * random.nextDouble(), random.nextGaussian() * 0.01);
                matrices.scale(0.006f, 0.006f, 0.006f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) Random.create().nextGaussian()));
                matrices.multiply(RotationAxis.POSITIVE_X.rotation((float) Random.create().nextGaussian()));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) Random.create().nextGaussian()));
                MinecraftClient.getInstance().getEntityRenderDispatcher().render(new LightningEntity(EntityType.LIGHTNING_BOLT, MinecraftClient.getInstance().world), BlockPos.ORIGIN.getX(), BlockPos.ORIGIN.getY(), BlockPos.ORIGIN.getZ(), 0, 0, matrices, vertexConsumers, light);
                matrices.pop();
            }

            matrices.pop();
        }
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V", shift = At.Shift.BEFORE))
    public void render(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.PIE_LAUNCHER_ITEM)) {
            this.client.getBlockEntityRenderDispatcher().renderEntity(this.raidbase$renderPieLauncher, matrices, vertexConsumers, light, overlay);
        }
    }
}