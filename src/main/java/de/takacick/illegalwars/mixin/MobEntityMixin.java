package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.access.PiglinProperties;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    public void tickNewAi(CallbackInfo info) {
        if (this instanceof PiglinProperties pillagerProperties
                && pillagerProperties.isUsingPiglinGoldTurret()) {
            info.cancel();
        }
    }
}
