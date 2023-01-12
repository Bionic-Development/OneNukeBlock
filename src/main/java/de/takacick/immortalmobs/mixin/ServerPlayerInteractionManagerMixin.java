package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow
    protected ServerWorld world;

    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "processBlockBreakingAction", cancellable = true)
    private void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo info) {
        if (player.isCreative()) {
            return;
        }

        if (player.getMainHandStack().isOf(ItemRegistry.IMMORTAL_PICKAXE)) {
            if (world.getBlockState(pos).isOf(Blocks.BEDROCK) || world.getBlockState(pos).isOf(Blocks.END_PORTAL)) {
                Block.dropStack(world, pos, world.getBlockState(pos).getBlock().asItem().getDefaultStack());
                world.breakBlock(pos, false);
                info.cancel();
            }
        }
    }
}
