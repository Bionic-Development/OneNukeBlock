package de.takacick.secretgirlbase.mixin;

import de.takacick.secretgirlbase.registry.ItemRegistry;
import de.takacick.secretgirlbase.registry.block.entity.BubbleGumLauncherBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Unique
    private BubbleGumLauncherBlockEntity secretgirlbase$renderBubbleGumLauncher;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/item/ItemStack;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V", shift = At.Shift.BEFORE))
    public void render(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if (stack.isOf(ItemRegistry.BUBBLE_GUM_LAUNCHER_ITEM)) {
            if (this.secretgirlbase$renderBubbleGumLauncher == null) {
                this.secretgirlbase$renderBubbleGumLauncher = new BubbleGumLauncherBlockEntity(BlockPos.ORIGIN, ItemRegistry.BUBBLE_GUM_LAUNCHER.getDefaultState());
            }
            this.client.getBlockEntityRenderDispatcher().renderEntity(this.secretgirlbase$renderBubbleGumLauncher, matrices, vertexConsumers, light, overlay);
        }
    }
}