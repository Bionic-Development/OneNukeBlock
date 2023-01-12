package de.takacick.immortalmobs.registry.entity.projectiles;

import de.takacick.immortalmobs.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class ImmortalDragonBreathEntity extends ExplosiveProjectileEntity {
    protected ImmortalDragonBreathEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ImmortalDragonBreathEntity(EntityType<? extends ExplosiveProjectileEntity> type, double x, double y, double z, double directionX, double directionY, double directionZ, World world) {
        super(type, x, y, z, directionX, directionY, directionZ, world);
    }

    public ImmortalDragonBreathEntity(EntityType<? extends ExplosiveProjectileEntity> type, LivingEntity owner, double directionX, double directionY, double directionZ, World world) {
        super(type, owner, directionX, directionY, directionZ, world);
    }

    public static ImmortalDragonBreathEntity create(EntityType<ImmortalDragonBreathEntity> entityType, World world) {
        return new ImmortalDragonBreathEntity(entityType, world);
    }

    @Override
    public void tick() {

        if (world.isClient) {
            for (int i = 0; i < 8; i++) {
                world.addParticle(ParticleRegistry.IMMORTAL_FLAME, true, getX() + world.getRandom().nextGaussian() * 0.5, getY() + world.getRandom().nextGaussian() * 0.5, getZ() + world.getRandom().nextGaussian() * 0.5, 0, 0, 0);
            }
        } else if (age > 240) {
            this.discard();
        }


        super.tick();
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!world.isClient) {
            this.discard();
            entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, getOwner()), 5);
        }
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return entity instanceof LivingEntity;
    }
}
