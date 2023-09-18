package de.takacick.onesuperblock.mixin;

import de.takacick.onesuperblock.registry.ItemRegistry;
import de.takacick.onesuperblock.registry.block.entity.SuperBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {

    @Shadow
    @Final
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final SuperBlockEntity onesuperblock$renderSuperBlock = new SuperBlockEntity(BlockPos.ORIGIN, ItemRegistry.SUPER_BLOCK.getDefaultState());

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        Item item = stack.getItem();
        if (item.equals(ItemRegistry.SUPER_BLOCK_ITEM)) {
            this.blockEntityRenderDispatcher.renderEntity(this.onesuperblock$renderSuperBlock, matrices, vertexConsumers, light, overlay);
        }
    }
}

