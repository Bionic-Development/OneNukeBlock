package de.takacick.imagineanything.registry.entity.projectiles;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.access.LivingProperties;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GiantNetheriteFeatherEntity extends ThrowProjectileEntity {

    protected Vec3d vec3d;

    public GiantNetheriteFeatherEntity(EntityType<? extends GiantNetheriteFeatherEntity> entityType, World world) {
        super(entityType, world);
        shake = 0;
    }

    public GiantNetheriteFeatherEntity(EntityType<? extends GiantNetheriteFeatherEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world);
        shake = 0;
    }

    public GiantNetheriteFeatherEntity(World world, LivingEntity owner) {
        super(EntityRegistry.GIANT_NETHERITE_FEATHER, owner, world);
        shake = 0;
    }

    public static GiantNetheriteFeatherEntity create(EntityType<GiantNetheriteFeatherEntity> entityType, World world) {
        return new GiantNetheriteFeatherEntity(entityType, world);
    }

    @Override
    public void setProperties(Entity user, float pitch, float yaw, float roll, float modifierZ, float modifierXYZ) {
        super.setProperties(user, pitch, yaw, roll, modifierZ, modifierXYZ);

        vec3d = getVelocity();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    @Override
    public void tick() {

        if (vec3d != null) {
            setVelocity(vec3d);
            this.velocityDirty = true;
            this.velocityModified = true;
        }

        Entity owner = this.getOwner();
        if (!world.isClient) {

            if (getBlockStateAtPos().getMaterial().blocksMovement()) {
                this.discard();
            }

            world.getOtherEntities(getOwner(), new Box(getPos(), getPos()).expand(2.3)).forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity && livingEntity.isPartOfGame()) {
                    if (entity.damage(DamageSource.thrownProjectile(this, owner), 0.0001f)) {
                        if (livingEntity instanceof PlayerEntity playerEntity) {
                            BionicUtils.sendEntityStatus((ServerWorld) world, playerEntity, ImagineAnything.IDENTIFIER, 15);

                            if (world.getRandom().nextDouble() <= 0.1) {
                                BionicUtils.sendEntityStatus((ServerWorld) world, playerEntity, ImagineAnything.IDENTIFIER, 16);
                                Vec3d velocity = playerEntity.getRotationVector().multiply(-0.4, 0, -0.4);
                                ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.34), getZ(), ItemRegistry.POOP_ITEM.getDefaultStack(), velocity.getX(), velocity.getY(), velocity.getZ());
                                itemEntity.setToDefaultPickupDelay();
                                world.spawnEntity(itemEntity);
                            }
                        } else {
                            ((LivingProperties) livingEntity).setVibratingTicks(60);
                        }

                        this.discard();
                    }
                }
            });

            if (((age >= 14 && owner != null))) {
                this.discard();
            }
        }

        super.tick();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {

    }

    @Override
    protected void onCollision(HitResult hitResult) {

        if (!world.isClient) {
            if (hitResult.getType().equals(HitResult.Type.BLOCK)) {

                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockState blockState = world.getBlockState(blockHitResult.getBlockPos());

                if (!blockState.isAir()) {
                    for (int x = 0; x < 10; x++) {
                        double d = getParticleX(getWidth());
                        double e = getY() + world.getRandom().nextDouble() * getHeight();
                        double f = getParticleZ(getWidth());
                        ((ServerWorld) getEntityWorld()).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 1, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0);
                    }
                    for (int x = 0; x < 10; x++) {
                        double d = getParticleX(getWidth());
                        double e = getY() + world.getRandom().nextDouble() * getHeight();
                        double f = getParticleZ(getWidth());
                        ((ServerWorld) getEntityWorld()).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.NETHERITE_SWORD.getDefaultStack()), d, e, f, 1, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F, 0);
                    }
                    world.playSound(null, getBlockPos(), blockState.getSoundGroup().getHitSound(), SoundCategory.AMBIENT, 1f, 1);
                    this.discard();
                }
            }
        }
    }

    @Override
    public double getDamage() {
        return 4.0f;
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

    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
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
