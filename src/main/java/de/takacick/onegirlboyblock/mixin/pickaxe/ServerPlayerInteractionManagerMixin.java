package de.takacick.onegirlboyblock.mixin.pickaxe;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.item.StarMiner;
import de.takacick.onegirlboyblock.registry.item.WeightedAnvilHammer;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow
    protected ServerWorld world;

    @Shadow
    @Final
    protected ServerPlayerEntity player;

    @Inject(method = "continueMining", at = @At("HEAD"))
    private void continueMining(BlockState state, BlockPos pos, int failedStartMiningTime, CallbackInfoReturnable<Float> info) {
        if (this.player.getMainHandStack().getItem() instanceof StarMiner) {
            EventHandler.sendWorldStatus(this.player.getWorld(), pos.toCenterPos(), OneGirlBoyBlock.IDENTIFIER, 1, 0);
        }
    }

    @Inject(method = "finishMining", at = @At("HEAD"))
    private void finishMining(BlockPos pos, int sequence, String reason, CallbackInfo info) {
        if (this.player.getMainHandStack().getItem() instanceof WeightedAnvilHammer) {
            EventHandler.sendWorldStatus(this.player.getWorld(), pos.toCenterPos(), OneGirlBoyBlock.IDENTIFIER, 2, 0);
        }
    }
}
