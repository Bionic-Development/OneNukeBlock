package de.takacick.emeraldmoney.registry.entity.projectile;

import de.takacick.emeraldmoney.registry.EntityRegistry;
import de.takacick.emeraldmoney.server.explosion.PillagerExplosionHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class PillagerProjectileEntity extends PersistentProjectileEntity {

    public PillagerProjectileEntity(EntityType<? extends PillagerProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = false;
    }

    public PillagerProjectileEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.PILLAGER, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setOwner(owner);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static PillagerProjectileEntity create(EntityType<PillagerProjectileEntity> entityType, World world) {
        return new PillagerProjectileEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (isOnGround() && !getWorld().isClient) {
            this.explode();
            this.discard();
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
        if (!getWorld().isClient) {
            PillagerExplosionHandler.createExplosion((ServerWorld) getWorld(), this, null, null,
                    getX(), getY(), getZ(), 4f, false, Explosion.DestructionType.DESTROY);
            PillagerExplosionHandler.createExplosion((ServerWorld) getWorld(), this, null, null,
                    getX(), getY(), getZ(), 3f, false, Explosion.DestructionType.KEEP);
        }
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.15f;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public byte getPierceLevel() {
        return (byte) 0;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return !entity.getType().equals(entity.getType()) && super.canHit(entity);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }
}