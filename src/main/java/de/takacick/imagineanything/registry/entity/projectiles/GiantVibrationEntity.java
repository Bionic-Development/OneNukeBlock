package de.takacick.imagineanything.registry.entity.projectiles;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.ParticleRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GiantVibrationEntity extends ProjectileEntity {

    protected Vec3d vec3d;

    public GiantVibrationEntity(EntityType<? extends GiantVibrationEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public GiantVibrationEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.GIANT_VIBRATION, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        setOwner(owner);
    }

    public static GiantVibrationEntity create(EntityType<GiantVibrationEntity> entityType, World world) {
        return new GiantVibrationEntity(entityType, world);
    }

    @Override
    public void setVelocity(Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
        float g = -MathHelper.sin((pitch + roll) * ((float)Math.PI / 180));
        float h = MathHelper.cos(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
        this.setVelocity(f, g, h, speed, divergence);
        vec3d = getVelocity();
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
        if (vec3d != null) {
            setVelocity(vec3d);
            this.velocityDirty = true;
            this.velocityModified = true;
        }
        Vec3d vec3d = getVelocity();
        Vec3d vec3d3 = this.getPos();
        Vec3d vec3d4 = vec3d3.add(vec3d);
        HitResult hitResult = this.world.raycast(new RaycastContext(vec3d3, vec3d4, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (((HitResult) hitResult).getType() != HitResult.Type.MISS) {
            vec3d4 = ((HitResult) hitResult).getPos();
        }

        Entity owner = this.getOwner();
        if (!world.isClient) {
            if (((age >= 500 && owner != null))) {
                this.discard();
            }
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

        if (world.isClient) {
            this.world.addParticle(ParticleRegistry.GIANT_VIBRATION, true,
                    getX(), getY(), getZ(),
                    0.4, -getYaw(), -getPitch());
        }

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

        if (!hitResult.getType().equals(HitResult.Type.MISS) && !world.isClient) {

            if (!(hitResult instanceof BlockHitResult blockHitResult && world.getBlockState(blockHitResult.getBlockPos()).isOf(ItemRegistry.IMAGINED_GIANT_BEDROCK_SPEAKERS))) {

                this.discard();
            }
            return;
        }

        super.onCollision(hitResult);
    }

    private void explode() {

    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void onRemoved() {
        this.world.addParticle(ParticleRegistry.GIANT_VIBRATION, true,
                getX(), getY(), getZ(),
                0.4, -getYaw(), -getPitch());
        super.onRemoved();
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
        if (world.isClient) {
            return false;
        }

        return !getType().equals(entity.getType()) && !entity.equals(getOwner()) && super.canHit(entity);
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
