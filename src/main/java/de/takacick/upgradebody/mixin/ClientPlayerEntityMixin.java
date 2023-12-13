package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.access.PlayerProperties;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends LivingEntity {

    protected ClientPlayerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canSprint", at = @At("HEAD"), cancellable = true)
    public void canSprint(CallbackInfoReturnable<Boolean> info) {
        if (this instanceof PlayerProperties playerProperties) {
            if (playerProperties.isUpgrading()) {
                if (!playerProperties.getBodyPartManager().canWalk()) {
                    info.setReturnValue(false);
                }
            }
        }
    }
}
