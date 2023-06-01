package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.access.PlayerProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPlayPacketListener {

    @Shadow
    @Final
    private MinecraftClient client;
    private ClientPlayerEntity deathmoney$tempPlayerEntity;

    @Inject(method = "onPlayerRespawn", at = @At("HEAD"))
    public void saveTempPlayer(PlayerRespawnS2CPacket packet, CallbackInfo info) {
        this.deathmoney$tempPlayerEntity = this.client.player;
    }

    @Inject(method = "onPlayerRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;init()V", shift = At.Shift.BEFORE))
    public void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo info) {
        if (this.client.player instanceof PlayerProperties playerProperties
                && this.deathmoney$tempPlayerEntity instanceof PlayerProperties oldProperties) {
            playerProperties.getDeathToasts().addAll(oldProperties.getDeathToasts());
        }
        this.deathmoney$tempPlayerEntity = null;
    }
}

