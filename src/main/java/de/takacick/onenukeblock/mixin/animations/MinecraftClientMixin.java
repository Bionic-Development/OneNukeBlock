package de.takacick.onenukeblock.mixin.animations;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import de.takacick.onenukeblock.registry.item.HandheldItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @WrapWithCondition(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;resetEquipProgress(Lnet/minecraft/util/Hand;)V", ordinal = 1))
    private boolean doItemUse(HeldItemRenderer instance, Hand hand) {
        ItemStack itemStack = this.player.getStackInHand(hand);
        if (itemStack.getItem() instanceof HandheldItem handheldItem && !handheldItem.allowVanillaUsageAnimation()) {
            return false;
        }

        return true;
    }
}
