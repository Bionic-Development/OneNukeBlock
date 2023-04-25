package de.takacick.everythinghearts.registry.entity.projectiles;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.PlayerProperties;
import de.takacick.everythinghearts.registry.EntityRegistry;
import de.takacick.everythinghearts.registry.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class HeartEntity extends ProjectileEntity {

    public int heartLevel = 1;

    public HeartEntity(EntityType<? extends HeartEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public HeartEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityRegistry.HEART, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static HeartEntity create(EntityType<HeartEntity> entityType, World world) {
        return new HeartEntity(entityType, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
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
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d4 = hitResult.getPos();
        }

        while (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d4);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }

            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
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

    @Override
    protected void onCollision(HitResult hitResult) {

        if (!hitResult.getType().equals(HitResult.Type.MISS)) {
            explode(new BlockPos(hitResult.getPos()));
            this.discard();
            return;
        }

        super.onCollision(hitResult);
    }

    private void explode(BlockPos target) {
        if (!world.isClient) {
            EverythingHearts.generateSphere(target, 4, false).forEach(blockPos -> {
                BlockState blockState = world.getBlockState(blockPos);
                EverythingHearts.replaceBlock(world, blockState, blockPos, heartLevel);
            });
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

    public void setHeartLevel(int heartLevel) {
        this.heartLevel = heartLevel;
    }
}


