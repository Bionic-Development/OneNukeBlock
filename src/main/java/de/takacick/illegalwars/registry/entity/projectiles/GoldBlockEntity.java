package de.takacick.illegalwars.registry.entity.projectiles;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GoldBlockEntity extends PersistentProjectileEntity {

    public GoldBlockEntity(EntityType<? extends GoldBlockEntity> entityType, World world) {
        super(entityType, world, ItemStack.EMPTY);
    }

    public GoldBlockEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.GOLD_BLOCK, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setOwner(owner);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static GoldBlockEntity create(EntityType<GoldBlockEntity> entityType, World world) {
        return new GoldBlockEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
            if (isOnGround()) {
                this.discard();
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();

            entity.damage(getWorld().getDamageSources().thrown(this, getOwner() instanceof LivingEntity livingEntity ? livingEntity : null), 5f);
            entity.addVelocity(new Vec3d(0, 0.5f, 0));
            entity.velocityModified = true;
            entity.velocityDirty = true;

            BionicUtils.sendEntityStatus(getWorld(), entityHitResult.getEntity(), IllegalWars.IDENTIFIER, 5);
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        explode();
    }

    private void explode() {
        if (!getWorld().isClient) {
            BionicUtils.sendEntityStatus(getWorld(), this, IllegalWars.IDENTIFIER, 5);
            this.discard();
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
        return !getType().equals(entity.getType()) && super.canHit(entity);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}