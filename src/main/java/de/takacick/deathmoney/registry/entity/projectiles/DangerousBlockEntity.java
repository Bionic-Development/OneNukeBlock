package de.takacick.deathmoney.registry.entity.projectiles;

import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.registry.EntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class DangerousBlockEntity extends ThrownEntity {

    private static final TrackedData<ItemStack> ITEMSTACK = DataTracker.registerData(DangerousBlockEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    public boolean explode = false;
    public BlockState blockState;
    public boolean drop = false;

    public DangerousBlockEntity(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public DangerousBlockEntity(World world, double x, double y, double z) {
        this(EntityRegistry.DANGEROUS_BLOCK, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static DangerousBlockEntity create(EntityType<DangerousBlockEntity> entityType, World world) {
        return new DangerousBlockEntity(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ITEMSTACK, Items.DIRT.getDefaultStack());
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    public boolean collides() {
        return !this.isRemoved();
    }


    public ItemStack getItemStack() {
        return getDataTracker().get(ITEMSTACK);
    }

    public void setItemStack(BlockState blockState, ItemStack itemStack) {
        this.blockState = blockState;
        getDataTracker().set(ITEMSTACK, itemStack);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {

        if (entityHitResult.getEntity() instanceof PlayerProperties playerProperties) {
            playerProperties.resetDamageDelay();
        }
        entityHitResult.getEntity().damage(DeathDamageSources.DANGEROUS_BLOCK, 2);

        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onCollision(HitResult hitResult) {

        if (!world.isClient) {
            if (!hitResult.getType().equals(HitResult.Type.MISS)) {
                explode();
                this.discard();
            }
        }

        super.onCollision(hitResult);
    }

    private void explode() {
        if (!world.isClient) {
            if (blockState != null) {
                for (int x = 0; x < 13; x++) {
                    double d = getParticleX(getWidth());
                    double e = getRandomBodyY();
                    double f = getParticleZ(getWidth());
                    ((ServerWorld) world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK,
                                    blockState), d, e, f,
                            1, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F,
                            0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F,
                            0);
                }
            }

            BlockSoundGroup blockSoundGroup = BlockSoundGroup.STONE;

            if (getItemStack().getItem() instanceof BlockItem) {
                Block block = ((BlockItem) getItemStack().getItem()).getBlock();
                blockSoundGroup = block.getSoundGroup(block.getDefaultState());
            }

            world.playSound(null, getBlockPos(), blockSoundGroup.getBreakSound(),
                    SoundCategory.BLOCKS, 1f, 1f);

            if (drop && blockState != null) {
                Block.getDroppedStacks(blockState, (ServerWorld) world, getBlockPos(), null).forEach(this::dropStack);
            }

            if (explode) {
                world.createExplosion(getOwner(), getX(), getY(), getZ(), 2f, false, Explosion.DestructionType.DESTROY);
            }
        }
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15f;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    protected boolean canHit(Entity entity) {
        return !entity.getType().equals(getType()) && super.canHit(entity);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}


