package de.takacick.onescaryblock.registry.entity.projectile;

import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.entity.custom.HerobrineLightningEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HerobrineLightningProjectileEntity extends ThrownEntity {
    public HerobrineLightningProjectileEntity(EntityType<? extends HerobrineLightningProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public HerobrineLightningProjectileEntity(World world, LivingEntity owner) {
        super(EntityRegistry.HEROBRINE_LIGHTNING_PROJECTILE, owner, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {

            HerobrineLightningEntity lightningEntity = new HerobrineLightningEntity(EntityRegistry.HEROBRINE_LIGHTNING_BOLT, getWorld());
            lightningEntity.setPos(getX(), getY(), getZ());
            getWorld().spawnEntity(lightningEntity);
            this.discard();
        }
    }

    @Override
    public void tick() {
        Vec3d vec3d = getVelocity();
        super.tick();
        setVelocity(vec3d);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }
}

