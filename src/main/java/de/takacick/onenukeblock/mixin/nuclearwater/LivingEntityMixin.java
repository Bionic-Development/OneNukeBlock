package de.takacick.onenukeblock.mixin.nuclearwater;

import de.takacick.onenukeblock.registry.block.fluid.NuclearWaterFluid;
import de.takacick.onenukeblock.utils.data.AttachmentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {

        if (getFluidHeight(NuclearWaterFluid.NUCLEAR_WATER) > 0) {
            getAttachedOrCreate(AttachmentTypes.NUCLEAR_MUTATIONS);
        }

        Optional.ofNullable(getAttached(AttachmentTypes.NUCLEAR_MUTATIONS))
                .ifPresent(attachment -> {
                    attachment.tick((LivingEntity) (Object) this);
                    if (attachment.shouldRemove()) {
                        removeAttached(AttachmentTypes.NUCLEAR_MUTATIONS);
                    }
                });
    }
}

