package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.access.PlayerProperties;
import de.takacick.immortalmobs.registry.entity.dragon.ImmortalEnderDragonEntity;
import draylar.identity.impl.PlayerDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "onStartedTrackingBy", at = @At(value = "HEAD"))
    private void onStartedTrackingBy(ServerPlayerEntity player, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties &&
                this instanceof PlayerDataProvider playerDataProvider && playerDataProvider.getIdentity() instanceof ImmortalEnderDragonEntity) {
            playerProperties.getImmortalDragonBar().addPlayer(player);
        }
    }


    @Inject(method = "onStoppedTrackingBy", at = @At(value = "HEAD"))
    private void onStoppedTrackingBy(ServerPlayerEntity player, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties &&
                this instanceof PlayerDataProvider playerDataProvider && playerDataProvider.getIdentity() instanceof ImmortalEnderDragonEntity) {
            playerProperties.getImmortalDragonBar().removePlayer(player);
        }
    }

}