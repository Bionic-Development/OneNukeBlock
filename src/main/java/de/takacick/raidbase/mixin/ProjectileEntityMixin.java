package de.takacick.raidbase.mixin;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.PlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity implements Ownable {

    @Shadow public abstract void setOwner(@Nullable Entity entity);

    protected ProjectileEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    public void onCollision(HitResult hitResult, CallbackInfo info) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getEntity() instanceof PlayerEntity playerEntity
                    && playerEntity instanceof PlayerProperties playerProperties
                    && playerProperties.hasSlimeSuit()) {

                if (!getWorld().isClient) {
                    RaidBase.sendProjectile(playerEntity, (ProjectileEntity) (Object) this);

                    if (this.getVelocity().length() <= 0.2) {
                        setVelocity(this.getVelocity().multiply(2).add(0, 1.5, 0));
                    }
                    setOwner(playerEntity);
                    age = age - 1;

                    this.setVelocity(getVelocity().multiply(-0.7));
                    this.velocityModified = true;
                }
                info.cancel();
            }
        }
    }
}