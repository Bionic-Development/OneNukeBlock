package de.takacick.onesuperblock.registry.entity.projectiles;

import de.takacick.onesuperblock.registry.EntityRegistry;
import de.takacick.onesuperblock.registry.ItemRegistry;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SuperBridgeEggEntity
        extends ThrownItemEntity {

    private Vec3d prevPos = null;

    public SuperBridgeEggEntity(EntityType<? extends SuperBridgeEggEntity> entityType, World world) {
        super(entityType, world);
    }

    public SuperBridgeEggEntity(World world, LivingEntity owner) {
        super(EntityRegistry.SUPER_BRIDGE_EGG, owner, world);
    }

    public SuperBridgeEggEntity(World world, double x, double y, double z) {
        super(EntityRegistry.SUPER_BRIDGE_EGG, x, y, z, world);
    }

    public static SuperBridgeEggEntity create(EntityType<? extends SuperBridgeEggEntity> entityType, World world) {
        return new SuperBridgeEggEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.world.isClient && this.age > 2) {

            if (this.age > 80) {
                this.discard();
            }

            if (this.prevPos == null) {
                this.prevPos = getPos();
            }

            Direction side = getMovementDirection().rotateYClockwise();

            Vec3d vec3d = this.prevPos.subtract(getPos()).normalize().multiply(0.25);
            Vec3d pos = this.prevPos;
            this.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0f, 3.0f);

            for (int i = 0; i <= (this.prevPos.distanceTo(getPos()) * 4); i++) {
                pos = pos.add(vec3d);

                boolean particle = false;

                for (int x = -1; x <= 1; x++) {
                    BlockPos position = new BlockPos(pos).add(side.getOffsetX() * x, -1, side.getOffsetZ() * x);
                    BlockState blockState = this.world.getBlockState(position);
                    if (!(blockState.isOf(ItemRegistry.SUPER_WOOL) || blockState.isOf(ItemRegistry.SUPER_BLOCK))) {
                        this.world.setBlockState(position, ItemRegistry.SUPER_WOOL.getDefaultState());
                        if (this.world.getRandom().nextDouble() <= 0.35 && !particle) {
                            this.world.syncWorldEvent(521783128, position, 1);
                            particle = true;
                        }
                    }
                }
            }

            this.prevPos = getPos();
        }
        this.setVelocity(this.getVelocity().multiply(1.01));
    }

    @Override
    public boolean isTouchingWater() {
        return false;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            double g = getX();
            double j = getZ();

            for (int i = 0; i < 5; ++i) {
                double h = getBodyY(0.45 + random.nextDouble() * 0.5);

                double d = random.nextGaussian();
                double e = random.nextGaussian();
                double f = random.nextGaussian();

                world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_CONFETTI, world.getRandom().nextInt(24000), true), true,
                        g + d * 0.2, h + e * 0.2, j + f * 0.2,
                        d * 0.15, e * 0.15, f * 0.15);
            }

            for (int i = 0; i < 5; ++i) {
                double h = getBodyY(0.45 + random.nextDouble() * 0.5);

                double d = random.nextGaussian();
                double e = random.nextGaussian();
                double f = random.nextGaussian();

                world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SPARK, world.getRandom().nextInt(24000), false), true,
                        g + d * 0.2, h + e * 0.2, j + f * 0.2,
                        d * 0.15, e * 0.15, f * 0.15);
            }
            for (int i = 0; i < 15; ++i) {
                double h = getBodyY(0.45 + random.nextDouble() * 0.5);

                double d = random.nextGaussian();
                double e = random.nextGaussian();
                double f = random.nextGaussian();

                world.addParticle(new RainbowParticleEffect(de.takacick.superitems.registry.ParticleRegistry.RAINBOW_SMOKE, world.getRandom().nextInt(24000), false), true,
                        g + d * 0.2, h + e * 0.2, j + f * 0.2,
                        d * 0.05, e * 0.05, f * 0.05);
            }
            world.playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.5f, 1.0f + world.getRandom().nextFloat() * 0.2f, true);

            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.0f);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            if (this.world.getBlockState(blockHitResult.getBlockPos()).isOf(ItemRegistry.SUPER_WOOL)) {
                return;
            }
        }

        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
            this.discard();
        }
    }


    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.SUPER_BRIDGE_EGG;
    }
}

