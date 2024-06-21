package de.takacick.onescaryblock.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onescaryblock.registry.item.Item303;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @ModifyExpressionValue(method = "interactBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldCancelInteraction()Z"))
    public boolean interactBlock(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
        if (itemStack.getItem() instanceof Item303) {
            return true;
        }

        return original;
    }
}

