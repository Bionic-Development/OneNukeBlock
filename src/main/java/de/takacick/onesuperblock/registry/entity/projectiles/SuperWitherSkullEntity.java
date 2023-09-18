package de.takacick.onesuperblock.registry.entity.projectiles;

import de.takacick.onesuperblock.registry.EntityRegistry;
import de.takacick.onesuperblock.registry.ParticleRegistry;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import de.takacick.superitems.utils.RainbowExplosionHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class SuperWitherSkullEntity
        extends ExplosiveProjectileEntity {
    private static final TrackedData<Boolean> CHARGED = DataTracker.registerData(SuperWitherSkullEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public SuperWitherSkullEntity(EntityType<? extends SuperWitherSkullEntity> entityType, World world) {
        super(entityType, world);
    }

    public SuperWitherSkullEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(EntityRegistry.SUPER_WITHER_SKULL, owner, directionX, directionY, directionZ, world);
    }

    public static SuperWitherSkullEntity create(EntityType<? extends SuperWitherSkullEntity> entityType, World world) {
        return new SuperWitherSkullEntity(entityType, world);
    }

    @Override
    protected float getDrag() {
        return this.isCharged() ? 0.73f : super.getDrag();
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public float getEffectiveExplosionResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, float max) {
        if (this.isCharged() && WitherEntity.canDestroy(blockState)) {
            return Math.min(0.8f, max);
        }
        return max;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        boolean bl;
        super.onEntityHit(entityHitResult);
        if (this.world.isClient) {
            return;
        }
        Entity entity = entityHitResult.getEntity();
        Entity entity2 = this.getOwner();
        if (entity2 instanceof LivingEntity livingEntity) {
            bl = entity.damage(DamageSource.mobProjectile(this, livingEntity), 8.0f);
            if (bl) {
                if (entity.isAlive()) {
                    this.applyDamageEffects(livingEntity, entity);
                } else {
                    livingEntity.heal(5.0f);
                }
            }
        } else {
            bl = entity.damage(DamageSource.MAGIC, 5.0f);
        }
        if (bl && entity instanceof LivingEntity) {
            int i = 0;
            if (this.world.getDifficulty() == Difficulty.NORMAL) {
                i = 10;
            } else if (this.world.getDifficulty() == Difficulty.HARD) {
                i = 40;
            }
            if (i > 0) {
                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20 * i, 1), this.getEffectCause());
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            RainbowExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                    getX(), getBodyY(0.5), getZ(), 1.4f, false,
                    destructionType);
            this.discard();
        }
    }

    @Override
    public boolean collides() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(CHARGED, false);
    }

    public boolean isCharged() {
        return this.dataTracker.get(CHARGED);
    }

    public void setCharged(boolean charged) {
        this.dataTracker.set(CHARGED, charged);
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    protected ParticleEffect getParticleType() {
        return new RainbowParticleEffect(ParticleRegistry.RAINBOW_DUST, this.world.getRandom().nextInt(240000), false);
    }
}

