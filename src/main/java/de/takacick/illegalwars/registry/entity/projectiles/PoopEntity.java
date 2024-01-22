package de.takacick.illegalwars.registry.entity.projectiles;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.IllegalWarsClient;
import de.takacick.illegalwars.access.LivingProperties;
import de.takacick.illegalwars.registry.EntityRegistry;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PoopEntity extends PersistentProjectileEntity {

    public PoopEntity(EntityType<? extends PoopEntity> entityType, World world) {
        super(entityType, world, ItemStack.EMPTY);
    }

    public PoopEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.POOP, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setOwner(owner);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public static PoopEntity create(EntityType<PoopEntity> entityType, World world) {
        return new PoopEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
            if (isOnGround()) {
                this.discard();
            }
        } else {
            IllegalWarsClient.addPotion(getPos().add(0, getHeight() / 2, 0), 0x462A14, 1);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!getWorld().isClient) {

            if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1, false, false, true));
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 1, false, false, true));

                if (livingEntity instanceof LivingProperties livingProperties) {
                    livingProperties.setPoopTicks(60);
                }
            }

            entityHitResult.getEntity().damage(getWorld().getDamageSources().thrown(this, getOwner() instanceof LivingEntity livingEntity ? livingEntity : null), 2f);
            BionicUtils.sendEntityStatus(getWorld(), this, IllegalWars.IDENTIFIER, 3);
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        explode();
    }

    private void explode() {
        if (!getWorld().isClient) {
            BionicUtils.sendEntityStatus(getWorld(), this, IllegalWars.IDENTIFIER, 3);
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