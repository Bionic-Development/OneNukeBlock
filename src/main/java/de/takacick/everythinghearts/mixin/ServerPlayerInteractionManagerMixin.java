package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.PlayerProperties;
import net.minecraft.block.BlockState;
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
    @Final
    protected ServerPlayerEntity player;

    @Shadow
    protected ServerWorld world;

    @Inject(at = @At("HEAD"), method = "processBlockBreakingAction", cancellable = true)
    private void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo info) {
        if (((PlayerProperties) player).hasHeartTouch() && !player.isCreative() && player.getMainHandStack().isEmpty()) {
            BlockState blockState = world.getBlockState(pos);
            if (EverythingHearts.replaceBlock(world, blockState, pos, ((PlayerProperties) player).getHeartTouchLevel())) {
                info.cancel();
            }
        }
    }
}
