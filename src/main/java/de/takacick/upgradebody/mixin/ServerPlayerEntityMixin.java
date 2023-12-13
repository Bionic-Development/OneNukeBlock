package de.takacick.upgradebody.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
        if (this instanceof PlayerProperties playerProperties
                && oldPlayer instanceof PlayerProperties oldProperties) {
            playerProperties.setBodyPartManager(oldProperties.getBodyPartManager());

            if (!alive) {
                playerProperties.setUpgradeShopPortal(null);
                UpgradeBody.updateEntityHealth(this, playerProperties.getBodyPartManager().getHearts(), true);
            }
        }
    }
}

