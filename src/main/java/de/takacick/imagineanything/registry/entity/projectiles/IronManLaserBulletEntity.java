package de.takacick.imagineanything.registry.entity.projectiles;

import de.takacick.imagineanything.network.IronManExplosionHandler;
import de.takacick.imagineanything.network.TelekinesisExplosionHandler;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ParticleRegistry;
import de.takacick.imagineanything.registry.particles.ColoredGlowParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class IronManLaserBulletEntity extends ThrowProjectileEntity {

    public Entity target = null;
    protected Vec3d vec3d;

    public IronManLaserBulletEntity(EntityType<? extends ThrowProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public IronManLaserBulletEntity(World world, LivingEntity owner) {
        super(EntityRegistry.IRON_MAN_LASER_BULLET, owner, world);
    }

    public static IronManLaserBulletEntity create(EntityType<IronManLaserBulletEntity> entityType, World world) {
        return new IronManLaserBulletEntity(entityType, world);
    }

    @Override
    public void setProperties(Entity user, float pitch, float yaw, float roll, float modifierZ, float modifierXYZ) {
        super.setProperties(user, pitch, yaw, roll, modifierZ, modifierXYZ);

        vec3d = getVelocity();
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient && age >= 5 && (target == null || !target.isAlive())) {

            double distance = 1000;
            Entity closest = null;

            for (Entity entity : world.getOtherEntities(getOwner(), getBoundingBox().expand(40))) {
                if (entity instanceof LivingEntity && entity.isAlive() && ((LivingEntity) entity).canSee(this)) {
                    double d = entity.distanceTo(this);
                    if (d < distance) {
                        closest = entity;
                        distance = d;
                    }
                }
            }

            target = closest;

            if (target == null && age >= 100) {
                explosion();
                return;
            }

            if (target != null) {
                Vec3d ownerPos = target.getPos();
                if (target.getType().equals(EntityType.ENDER_DRAGON)) {
                    ownerPos = ownerPos.multiply(1, 0, 1).add(0, target.getBodyY(0.25), 0);
                } else {
                    ownerPos = ownerPos.multiply(1, 0, 1).add(0, target.getBodyY(0.75), 0);
                }
                Vec3d thisPos = getPos().add(0, getHeight() / 2, 0);
                Vec3d difference = ownerPos.subtract(thisPos);

                vec3d = difference.normalize().multiply(1.8);
            }
        }

        if (target != null && !world.isClient) {

            Vec3d ownerPos = target.getPos();

            if (target.getType().equals(EntityType.ENDER_DRAGON)) {
                ownerPos = ownerPos.multiply(1, 0, 1).add(0, target.getBodyY(0.25), 0);
            } else {
                ownerPos = ownerPos.multiply(1, 0, 1).add(0, target.getBodyY(0.75), 0);
            }
            Vec3d thisPos = getPos();
            Vec3d difference = ownerPos.subtract(thisPos);

            vec3d = difference.normalize().multiply(1.8);
        }

        if (!world.isClient && age >= 300) {
            explosion();
        }

        if (vec3d != null) {
            setVelocity(vec3d.getX(), vec3d.getY(), vec3d.getZ(), 0.7f, 1.0F);
            this.velocityDirty = true;
            this.velocityModified = true;
        }
    }


    @Override
    public boolean shouldSave() {
        return false;
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {

        Entity entity = entityHitResult.getEntity();
        if (entity == getOwner()) {
            return;
        }
        if (!world.isClient) {
            Vec3f color = new Vec3f(Vec3d.unpackRgb(0xFF00000));

            for (int i = 0; i < 12; i++) {
                ((ServerWorld) getEntityWorld()).spawnParticles(new ColoredGlowParticleEffect(ParticleRegistry.GLOW_SPARK, color),
                        getParticleX(getWidth()), getRandomBodyY(), getParticleZ(getWidth()),
                        3, 0.0D, 0.0D, 0.0D, 0.45000000596046448D);
            }
            IronManExplosionHandler.createExplosion((ServerWorld) world, null, null, null, this.getX(), this.getBodyY(0.0625), this.getZ(), 2.3f, false, Explosion.DestructionType.BREAK);
        }
        double i = 5;

        Entity entity2 = this.getOwner();
        DamageSource damageSource2;
        if (entity2 == null) {
            damageSource2 = DamageSource.trident(this, this);
        } else {
            damageSource2 = DamageSource.trident(this, entity2);
            if (entity2 instanceof LivingEntity) {
                ((LivingEntity) entity2).onAttacking(entity);
            }
        }

        boolean bl = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getFireTicks();

        if (entity.damage(damageSource2, (float) i)) {
            if (bl) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;

                if (!this.world.isClient && entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity) entity2, livingEntity);
                }

                this.onHit(livingEntity);
                if (entity2 != null && livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity) entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, 0.0F));
                }
            }

            this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        } else {
            entity.kill();
            this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        }

        explosion();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!world.isClient) {

            Vec3f color = new Vec3f(Vec3d.unpackRgb(0xFF00000));
            for (int i = 0; i < 12; i++) {
                ((ServerWorld) getEntityWorld()).spawnParticles(new ColoredGlowParticleEffect(ParticleRegistry.GLOW_SPARK, color),
                        getParticleX(getWidth()), getRandomBodyY(), getParticleZ(getWidth()),
                        3, 0.0D, 0.0D, 0.0D, 0.45000000596046448D);
            }
            IronManExplosionHandler.createExplosion((ServerWorld) world, null, null, null, this.getX(), this.getBodyY(0.0625), this.getZ(), 2.3f, false, Explosion.DestructionType.BREAK);
        }
        this.discard();
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }

    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected void onBlockCollision(BlockState state) {

    }

    @Override
    public void onRemoved() {
        Vec3f color = new Vec3f(Vec3d.unpackRgb(0xFF00000));
        for (int i = 0; i < 12; i++) {
            double d = 0.45000000596046448D * this.random.nextGaussian();
            double e = 0.45000000596046448D * this.random.nextGaussian();
            double f = 0.45000000596046448D * this.random.nextGaussian();
            world.addParticle(new ColoredGlowParticleEffect(ParticleRegistry.GLOW_SPARK, color), getParticleX(getWidth()),
                    getRandomBodyY(), getParticleZ(getWidth()), d, e, f);
        }
        super.onRemoved();
    }

    public void explosion() {
        this.discard();
    }
}
