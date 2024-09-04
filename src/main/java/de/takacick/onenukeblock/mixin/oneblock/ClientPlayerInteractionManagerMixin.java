package de.takacick.onenukeblock.mixin.oneblock;

import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onenukeblock.registry.block.AbstractOneBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;", shift = At.Shift.AFTER), cancellable = true)
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info, @Local BlockState block) {
        if (block.getBlock() instanceof AbstractOneBlock) {
            if (!this.client.player.isCreative()) {
                info.setReturnValue(false);
            }
        }
    }
}

