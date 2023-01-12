package de.takacick.immortalmobs.registry.entity.projectiles;

import de.takacick.immortalmobs.registry.EntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class CustomBlockEntity extends ProjectileEntity {
    @Nullable
    private LivingEntity causingEntity;
    private static final TrackedData<ItemStack> ITEMSTACK;
    public boolean explode = false;
    public BlockState blockState;
    public boolean drop = false;

    static {
        ITEMSTACK = DataTracker.registerData(CustomBlockEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    }

    public CustomBlockEntity(EntityType<? extends CustomBlockEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public CustomBlockEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityRegistry.FALLING_BLOCK, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    public static CustomBlockEntity create(EntityType<CustomBlockEntity> entityType, World world) {
        return new CustomBlockEntity(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ITEMSTACK, Items.DIRT.getDefaultStack());
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean collides() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        super.tick();

        Vec3d vec3d = getVelocity();
        Vec3d vec3d3 = this.getPos();
        Vec3d vec3d4 = vec3d3.add(vec3d);
        HitResult hitResult = this.world.raycast(new RaycastContext(vec3d3, vec3d4, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (((HitResult) hitResult).getType() != HitResult.Type.MISS) {
            vec3d4 = ((HitResult) hitResult).getPos();
        }

        while (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d4);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }

            if (hitResult != null && ((HitResult) hitResult).getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitResult).getEntity();
                Entity entity2 = this.getOwner();
                if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity) entity2).shouldDamagePlayer((PlayerEntity) entity)) {
                    hitResult = null;
                    entityHitResult = null;
                }
            }

            if (hitResult != null) {
                this.onCollision((HitResult) hitResult);
                this.velocityDirty = true;
            }

            if (entityHitResult == null || this.getPierceLevel() <= 0) {
                break;
            }

            hitResult = null;
        }

        this.checkBlockCollision();

        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
        }
        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98));
        if (this.onGround) {
            this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
        }
    }

    public ItemStack getItemStack() {
        return getDataTracker().get(ITEMSTACK);
    }

    public void setItemStack(BlockState blockState, ItemStack itemStack) {
        this.blockState = blockState;
        getDataTracker().set(ITEMSTACK, itemStack);
    }

    @Override
    protected void onCollision(HitResult hitResult) {

        if (!hitResult.getType().equals(HitResult.Type.MISS)) {
            explode();
            this.discard();
            return;
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

    @Nullable
    public LivingEntity getCausingEntity() {
        return this.causingEntity;
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15f;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public byte getPierceLevel() {
        return (byte) 0;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return !entity.getType().equals(entity.getType()) && super.canHit(entity);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}


