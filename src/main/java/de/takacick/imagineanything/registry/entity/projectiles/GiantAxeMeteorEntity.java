package de.takacick.imagineanything.registry.entity.projectiles;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.custom.GiantAxeMeteorShockwaveEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class GiantAxeMeteorEntity extends ProjectileEntity {

    @Nullable
    private LivingEntity causingEntity;
    public BlockState blockState;

    public GiantAxeMeteorEntity(EntityType<? extends GiantAxeMeteorEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public GiantAxeMeteorEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityRegistry.GIANT_AXE_METEOR, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    public static GiantAxeMeteorEntity create(EntityType<GiantAxeMeteorEntity> entityType, World world) {
        return new GiantAxeMeteorEntity(entityType, world);
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
    protected void initDataTracker() {

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
        world.addParticle(ParticleTypes.SMOKE, getX(), getBodyY(1), getZ(), vec3d.getX(), 0, vec3d.getZ());

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
            explode();
            this.discard();
            return;
        }

        super.onCollision(hitResult);
    }

    private void explode() {
        if (!world.isClient) {
            world.createExplosion(getOwner(), getX(), getY(), getZ(), 2.0f, Explosion.DestructionType.NONE);

            //  GigaNukeExplosionEntity gigaNukeExplosionEntity = new GigaNukeExplosionEntity(EntityRegistry.GIGA_EXPLOSION, world);
            //    gigaNukeExplosionEntity.setPos(getX(), getY() - 1, getZ());
            //   world.spawnEntity(gigaNukeExplosionEntity);

            for (int d = 0; d < 40; d++) {
                double x = getX();
                double z = getZ();

                double increment = (Math.PI) / 20;
                double angle = (d) * increment;
                x = x + (2 * Math.cos(angle));
                z = z + (2 * Math.sin(angle));

                GiantAxeMeteorShockwaveEntity giantAxeMeteorShockwaveEntity = new GiantAxeMeteorShockwaveEntity(world, new Vec3d(x, getY(), z).subtract(getPos()).normalize(), x, getY() + 0.5, z);
                giantAxeMeteorShockwaveEntity.setPos(x, getBodyY(0.5), z);
                world.spawnEntity(giantAxeMeteorShockwaveEntity);
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
        return !getType().equals(entity.getType()) && super.canHit(entity);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }
}