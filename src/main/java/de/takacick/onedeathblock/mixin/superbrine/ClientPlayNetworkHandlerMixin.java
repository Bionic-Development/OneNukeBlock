package de.takacick.onedeathblock.mixin.superbrine;

import de.takacick.onedeathblock.registry.entity.living.SuperbrineEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow
    private ClientWorld world;

    @Inject(method = "onEntityPassengersSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V", shift = At.Shift.AFTER), cancellable = true)
    public void onEntityPassengersSet(EntityPassengersSetS2CPacket packet, CallbackInfo info) {
        Entity entity = this.world.getEntityById(packet.getId());
        if (entity instanceof SuperbrineEntity) {
            info.cancel();

            entity.removeAllPassengers();
            for (int i : packet.getPassengerIds()) {
                Entity entity2 = this.world.getEntityById(i);
                if (entity2 == null) continue;
                entity2.startRiding(entity, true);
            }
        }
    }
}