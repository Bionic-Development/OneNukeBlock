package de.takacick.stealbodyparts.mixin;

import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.stealbodyparts.utils.BodyPart;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends LivingEntity {

    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void copyFrom(CallbackInfo info) {
        for (BodyPart value : BodyPart.values()) {
            ((PlayerProperties) this).setBodyPart(value.getIndex(), true);
        }
    }
}

