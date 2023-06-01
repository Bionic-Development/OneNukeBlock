package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import net.minecraft.block.BlockState;
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
    @Final
    protected ServerPlayerEntity player;

    @Shadow
    protected ServerWorld world;

    @Inject(method = "processBlockBreakingAction", at = @At("HEAD"))
    private void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo info) {
        BlockState state = world.getBlockState(pos);
        if (state.isOf(Blocks.GRASS_BLOCK) || state.isOf(Blocks.GRASS) || state.isOf(Blocks.TALL_GRASS)) {
            if (player instanceof PlayerProperties playerProperties && player.isAlive() && !this.world.isClient) {
                if (playerProperties.getGamerAllergyTicks() > 0) {
                    player.damage(DeathDamageSources.GRASS_TOUCH, 5f);
                }
            }
        }
    }
}
