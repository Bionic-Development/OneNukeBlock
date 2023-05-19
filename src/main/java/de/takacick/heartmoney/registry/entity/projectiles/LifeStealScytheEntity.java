package de.takacick.heartmoney.registry.entity.projectiles;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicBoolean;

public class LifeStealScytheEntity extends ThrowProjectileEntity {

    protected Vec3d vec3d;

    public LifeStealScytheEntity(EntityType<? extends LifeStealScytheEntity> entityType, World world) {
        super(entityType, world);
        shake = 0;
    }

    public LifeStealScytheEntity(EntityType<? extends LifeStealScytheEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world);
        shake = 0;
    }

    public static LifeStealScytheEntity create(EntityType<LifeStealScytheEntity> entityType, World world) {
        return new LifeStealScytheEntity(entityType, world);
    }

    @Override
    public void setProperties(Entity user, float pitch, float yaw, float roll, float modifierZ, float modifierXYZ) {
        super.setProperties(user, pitch, yaw, roll, modifierZ, modifierXYZ);

        vec3d = getVelocity().multiply(0.7);
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void tick() {

        if (vec3d != null) {
            setVelocity(vec3d);
            this.velocityDirty = true;
            this.velocityModified = true;
        }

        if (!world.isClient) {

            LivingEntity owner = getOwner() instanceof LivingEntity livingEntity ? livingEntity : null;

            AtomicBoolean damaged = new AtomicBoolean(false);

            world.getOtherEntities(this, getBoundingBox()).forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity && livingEntity.isPartOfGame() && !livingEntity.equals(getOwner())) {
                    livingEntity.damage(DamageSource.thrownProjectile(this, owner), 10.5f);

                    damaged.set(true);
                    BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 15);
                }
            });

            if (damaged.get() && owner != null) {
                owner.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0, false, false, true));
                owner.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 1, false, false, true));
                BionicUtils.sendEntityStatus((ServerWorld) world, owner, HeartMoney.IDENTIFIER, 16);
            }

            if (age >= 60 || damaged.get() || getBlockStateAtPos().getMaterial().blocksMovement()) {
                for (int x = 0; x < 20; x++) {
                    double d = getParticleX(getWidth());
                    double e = getY() + world.getRandom().nextDouble() * getHeight();
                    double f = getParticleZ(getWidth());
                    ((ServerWorld) getEntityWorld()).spawnParticles(ParticleRegistry.HEART_SOUL, d, e, f, 1, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0.14);
                }

                for (int x = 0; x < 4; x++) {
                    double d = getParticleX(getWidth());
                    double e = getY() + world.getRandom().nextDouble() * getHeight();
                    double f = getParticleZ(getWidth());
                    ((ServerWorld) getEntityWorld()).spawnParticles(ParticleRegistry.HEART_EXPLOSION, d, e, f, 1, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0);
                }
                world.playSound(null, getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE, getSoundCategory(), 2f, 1);
                world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, getSoundCategory(), 0.5f, 3f);
                this.discard();
            }
        }

        super.tick();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {

    }

    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    protected SoundEvent getHitSound() {
        return null;
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

    }

    public boolean shouldRender(double distance) {
        double d = 64.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }
}