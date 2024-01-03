package de.takacick.raidbase.registry.entity.projectiles;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.LivingProperties;
import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PieEntity extends PersistentProjectileEntity {

    public PieEntity(EntityType<? extends PieEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = false;
    }

    public PieEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.PIE, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setOwner(owner);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static PieEntity create(EntityType<PieEntity> entityType, World world) {
        return new PieEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (isOnGround() && !getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!getWorld().isClient) {

            if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0, false, false, true));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 0, false, false, true));

                if (livingEntity instanceof LivingProperties livingProperties) {
                    livingProperties.setPieTicks(60);
                }
            }

            entityHitResult.getEntity().damage(getWorld().getDamageSources().thrown(this, null), 2f);
            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), entityHitResult.getEntity(), RaidBase.IDENTIFIER, 4);
            explode();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        explode();
    }

    private void explode() {
        if (!getWorld().isClient) {
            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, RaidBase.IDENTIFIER, 4);
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