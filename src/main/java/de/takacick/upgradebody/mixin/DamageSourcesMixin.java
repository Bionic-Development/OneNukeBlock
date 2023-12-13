package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.registry.entity.living.TurretPillagerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin {

    @Shadow
    public abstract DamageSource create(RegistryKey<DamageType> key, @Nullable Entity source, @Nullable Entity attacker);

    @Inject(method = "arrow", at = @At("HEAD"), cancellable = true)
    public void arrow(PersistentProjectileEntity source, Entity attacker, CallbackInfoReturnable<DamageSource> info) {
        if (attacker instanceof TurretPillagerEntity) {
            info.setReturnValue(this.create(TurretPillagerEntity.TURRET_PILLAGER, source, attacker));
        }
    }
}
