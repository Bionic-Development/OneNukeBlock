package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.access.LivingProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity {

    public MobEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickNewAi", at = @At(value = "HEAD"), cancellable = true)
    private void tickNewAi(CallbackInfo info) {
        if (this instanceof LivingProperties livingProperties && livingProperties.isMaidExploding()) {
            info.cancel();
        }
    }
}