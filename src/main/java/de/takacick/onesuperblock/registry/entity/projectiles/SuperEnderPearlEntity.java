package de.takacick.onesuperblock.registry.entity.projectiles;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.registry.EntityRegistry;
import de.takacick.onesuperblock.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SuperEnderPearlEntity
        extends ThrownItemEntity {
    public SuperEnderPearlEntity(EntityType<? extends SuperEnderPearlEntity> entityType, World world) {
        super(entityType, world);
    }

    public SuperEnderPearlEntity(World world, LivingEntity owner) {
        super(EntityRegistry.SUPER_ENDER_PEARL, owner, world);
    }

    public static SuperEnderPearlEntity create(EntityType<? extends SuperEnderPearlEntity> entityType, World world) {
        return new SuperEnderPearlEntity(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.SUPER_ENDER_PEARL;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.0f);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.world.isClient && !this.isRemoved()) {
            Entity entity = this.getOwner();
            if (entity != null) {
                BionicUtils.sendEntityStatus((ServerWorld) this.getWorld(), entity, OneSuperBlock.IDENTIFIER, 3);
                if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                    if (serverPlayerEntity.networkHandler.getConnection().isOpen() && serverPlayerEntity.world == this.world && !serverPlayerEntity.isSleeping()) {
                        if (entity.hasVehicle()) {
                            serverPlayerEntity.requestTeleportAndDismount(this.getX(), this.getY(), this.getZ());
                        } else {
                            entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                        }
                        entity.onLanding();
                    }
                } else if (entity != null) {
                    entity.requestTeleport(this.getX(), this.getY(), this.getZ());
                    entity.onLanding();
                }
                BionicUtils.sendEntityStatus((ServerWorld) this.getWorld(), entity, OneSuperBlock.IDENTIFIER, 4);
            }
            this.discard();
        }
    }

    protected float getGravity() {
        return 0f;
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.age > 10000) {
            this.discard();
        }

        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }
    }

    @Override
    @Nullable
    public Entity moveToWorld(ServerWorld destination) {
        Entity entity = this.getOwner();
        if (entity != null && entity.world.getRegistryKey() != destination.getRegistryKey()) {
            this.setOwner(null);
        }
        return super.moveToWorld(destination);
    }
}

