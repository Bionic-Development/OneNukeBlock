package de.takacick.everythinghearts.registry.entity.projectiles;

import de.takacick.everythinghearts.registry.ParticleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HeartScytheEntity extends ThrowProjectileEntity {

    protected Vec3d vec3d;

    public HeartScytheEntity(EntityType<? extends HeartScytheEntity> entityType, World world) {
        super(entityType, world);
        shake = 0;
    }

    public HeartScytheEntity(EntityType<? extends HeartScytheEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world);
        shake = 0;
    }

    public static HeartScytheEntity create(EntityType<HeartScytheEntity> entityType, World world) {
        return new HeartScytheEntity(entityType, world);
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

            boolean bl2 = false;

            BlockPos blockPos = this.getBlockPos().add(-1, -1, -1);

            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    for (int z = 0; z < 3; z++) {
                        BlockPos pos = blockPos.add(x, y, z);
                        BlockState blockState = world.getBlockState(pos);

                        if (!blockState.isAir() && blockState.getBlock().getBlastResistance() < Blocks.BEDROCK.getBlastResistance()) {
                            if (blockState.getBlock() instanceof OreBlock) {
                                world.breakBlock(pos, true, getOwner());
                            } else {
                                world.breakBlock(pos, true, getOwner());
                            }
                            bl2 = true;
                        }
                    }
                }
            }

            if (bl2) {
                Box box = getBoundingBox().stretch(2, 2, 2);
                int i = MathHelper.floor(box.minX - 0.75D);
                int j = MathHelper.floor(box.minY - 0.75D);
                int k = MathHelper.floor(box.minZ - 0.75D);
                int l = MathHelper.floor(box.maxX + 0.75D);
                int m = MathHelper.floor(box.maxY + 0.75D);
                int n = MathHelper.floor(box.maxZ + 0.75D);

                BlockPos o = new BlockPos(i + random.nextInt(l - i + 1), j + random.nextInt(m - j + 1), k + random.nextInt(n - k + 1));

                ((ServerWorld) world).spawnParticles(ParticleRegistry.HEART_EXPLOSION, (double) o.getX() + 0.5D, (double) o.getY() + 2.0D, (double) o.getZ() + 0.5D,
                        1, 0, 0, 0, 0.0);
            }

            if (age >= 60) {
                for (int x = 0; x < 10; x++) {
                    double d = getParticleX(getWidth());
                    double e = getY() + world.getRandom().nextDouble() * getHeight();
                    double f = getParticleZ(getWidth());
                    ((ServerWorld) getEntityWorld()).spawnParticles(ParticleRegistry.HEART_EXPLOSION, d, e, f, 1, (double) (MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F), 0.05000000074505806D, (double) (MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F), 0);
                }
                world.playSound(null, getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 1f, 1);
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