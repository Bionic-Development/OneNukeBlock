package de.takacick.upgradebody.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.upgradebody.access.ClientPlayerProperties;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.client.model.BodyEntityModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerEntity.class)
@Implements({@Interface(iface = ClientPlayerProperties.class, prefix = "upgradebody$")})
public abstract class AbstractPlayerEntityMixin extends PlayerEntity {

    @Unique
    private BodyEntityModel upgradebody$upgradeModel;

    public AbstractPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    public void upgradebody$setUpgradeModel(BodyEntityModel bodyEntityModel) {
        this.upgradebody$upgradeModel = bodyEntityModel;
    }

    public BodyEntityModel upgradebody$getUpgradeModel() {
        return this.upgradebody$upgradeModel;
    }

    public void upgradebody$refreshEntityModel() {
        if (this instanceof PlayerProperties playerProperties) {
            if (!playerProperties.getBodyPartManager().isUpgrading()) {
                upgradebody$setUpgradeModel(null);
            } else {
                upgradebody$setUpgradeModel(BodyEntityModel.factory(playerProperties.getBodyPartManager()));
            }
        }
    }
}
