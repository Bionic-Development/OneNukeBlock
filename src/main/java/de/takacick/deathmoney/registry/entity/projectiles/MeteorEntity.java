package de.takacick.deathmoney.registry.entity.projectiles;

import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.utils.MeteorExplosionHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class MeteorEntity extends ThrownEntity {

    protected Vec3d vec3d;

    public MeteorEntity(EntityType<? extends MeteorEntity> entityType, World world) {
        super(entityType, world);
    }

    public MeteorEntity(EntityType<? extends MeteorEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world);
    }

    public MeteorEntity(World world, LivingEntity owner) {
        super(EntityRegistry.METEOR, owner, world);
    }

    public static MeteorEntity create(EntityType<MeteorEntity> entityType, World world) {
        return new MeteorEntity(entityType, world);
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {

        if (vec3d != null) {
            setVelocity(vec3d);
            this.velocityDirty = true;
            this.velocityModified = true;
        }

        if (!world.isClient) {
            if (age >= 300) {
                MeteorExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                        getX(), getBodyY(0.5), getZ(), 3.2f, false,
                        Explosion.DestructionType.DESTROY);
                this.discard();
            }
        } else {
            Vec3d vec3d = getVelocity();
            for (int i = 0; i < 3; i++) {
                world.addImportantParticle(ParticleRegistry.SMOKE, true, getX() + world.getRandom().nextGaussian() * 0.4, getBodyY(0.5) + world.getRandom().nextGaussian() * 0.5, getZ() + world.getRandom().nextGaussian() * 0.4, -vec3d.getX() * 0.001, -vec3d.getY() * 0.001, -vec3d.getZ() * 0.001);
            }
        }

        super.tick();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {

    }

    public void onPlayerCollision(PlayerEntity player) {
        if (this.isOwner(player) || this.getOwner() == null) {
            super.onPlayerCollision(player);
        }
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!world.isClient) {
            if (!hitResult.getType().equals(HitResult.Type.MISS)) {
                MeteorExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                        getX(), getBodyY(0.5), getZ(), 3.2f, false,
                        Explosion.DestructionType.DESTROY);
                this.discard();
            }
        }
    }

    public boolean shouldRender(double distance) {
        double d = 428.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }
}
