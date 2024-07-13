package de.takacick.onegirlboyblock.mixin.oneblock;

import de.takacick.onegirlboyblock.utils.oneblock.OneBlock;
import de.takacick.onegirlboyblock.utils.oneblock.OneBlockServerState;
import de.takacick.onegirlboyblock.registry.block.AbstractOneBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow
    protected ServerWorld world;

    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Inject(method = "tryBreakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void processBlockBreakingAction(BlockPos pos, CallbackInfoReturnable<Boolean> info, BlockEntity blockEntity, Block block, BlockState blockState) {
        if (blockState.getBlock() instanceof AbstractOneBlock abstractOneBlock) {
            if(!this.player.isCreative()) {
                OneBlockServerState.getServerState(this.world.getServer()).ifPresent(serverState -> {
                    OneBlock oneBlock = abstractOneBlock.getOneBlock(serverState);
                    if (oneBlock != null) {
                        oneBlock.drop(world, pos);
                    }
                });

                ItemStack itemStack = this.player.getMainHandStack();
                itemStack.postMine(world, blockState, pos, player);

                info.setReturnValue(false);
            }
        }
    }
}
