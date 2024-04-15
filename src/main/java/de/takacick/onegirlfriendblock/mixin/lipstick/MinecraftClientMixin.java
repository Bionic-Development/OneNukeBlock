package de.takacick.onegirlfriendblock.mixin.lipstick;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public HitResult crosshairTarget;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At("HEAD"))
    public void doAttack(CallbackInfoReturnable<Boolean> info) {
        if (this.crosshairTarget.getType() != HitResult.Type.ENTITY) {
            if (this.player.getMainHandStack().isOf(ItemRegistry.LIPSTICK_KATANA)) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(this.interactionManager.isBreakingBlock());
                ClientPlayNetworking.send(new Identifier(OneGirlfriendBlock.MOD_ID, "swinglipstick"), buf);
            }
        }
    }
}

