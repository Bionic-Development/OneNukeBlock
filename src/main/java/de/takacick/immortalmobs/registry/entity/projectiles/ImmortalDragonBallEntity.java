package de.takacick.immortalmobs.registry.entity.projectiles;

import de.takacick.immortalmobs.network.ImmortalFireworkExplosionHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ImmortalDragonBallEntity extends ExplosiveProjectileEntity {
    protected ImmortalDragonBallEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ImmortalDragonBallEntity(EntityType<? extends ExplosiveProjectileEntity> type, double x, double y, double z, double directionX, double directionY, double directionZ, World world) {
        super(type, x, y, z, directionX, directionY, directionZ, world);
    }

    public ImmortalDragonBallEntity(EntityType<? extends ExplosiveProjectileEntity> type, LivingEntity owner, double directionX, double directionY, double directionZ, World world) {
        super(type, owner, directionX, directionY, directionZ, world);
    }

    public static ImmortalDragonBallEntity create(EntityType<ImmortalDragonBallEntity> entityType, World world) {
        return new ImmortalDragonBallEntity(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            ImmortalFireworkExplosionHandler.createExplosion((ServerWorld) world, getOwner(), null, null,
                    getX(), getBodyY(0.5), getZ(), 4f, false,
                    Explosion.DestructionType.DESTROY);
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
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
