package de.takacick.onegirlboyblock.mixin.glitterblade;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.item.GlitterBlade;
import de.takacick.utils.common.ability.networking.AbilityPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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
    public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At("HEAD"))
    public void doAttack(CallbackInfoReturnable<Boolean> info) {
        if (this.crosshairTarget.getType() != HitResult.Type.ENTITY) {
            if (this.player.getMainHandStack().getItem() instanceof GlitterBlade) {
                ClientPlayNetworking.send(new AbilityPacket(Identifier.of(OneGirlBoyBlock.MOD_ID, "glitterbladehit")));
            }
        }
    }
}

