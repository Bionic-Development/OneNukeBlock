package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.registry.block.fluid.MagicWellWaterFluid;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer,
                        BlockState blockState, FluidState fluidState, CallbackInfo info) {
        if (fluidState.getFluid() instanceof MagicWellWaterFluid) {
            info.cancel();
        }
    }
}