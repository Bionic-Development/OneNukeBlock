package de.takacick.immortalmobs.registry.entity.projectiles;

import de.takacick.immortalmobs.registry.EntityRegistry;
import de.takacick.immortalmobs.registry.entity.custom.BlackHoleEntity;
import de.takacick.immortalmobs.registry.entity.living.ImmortalIronGolemEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ImmortalArrowEntity extends ArrowEntity {
    public ImmortalArrowEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public ImmortalArrowEntity(World world, LivingEntity owner) {
        this(EntityRegistry.IMMORTAL_ARROW, world);
        this.setPos(owner.getX(), owner.getEyeY() - (double) 0.1f, owner.getZ());
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.ALLOWED;
        }
    }

    @Override
    public void tick() {
        age++;

        super.tick();
    }

    @Override
    protected void onCollision(HitResult hitResult) {

        if (!world.isClient && !hitResult.getType().equals(HitResult.Type.MISS)) {

            if (!(hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof ImmortalIronGolemEntity)) {
                BlackHoleEntity blackHoleEntity = new BlackHoleEntity(EntityRegistry.BLACK_HOLE, world);
                blackHoleEntity.setPos(getX(), getBodyY(0.5), getZ());
                blackHoleEntity.setPlayer(getOwner());
                world.spawnEntity(blackHoleEntity);
                world.playSoundFromEntity(null, blackHoleEntity, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.HOSTILE, 2.0F, 1.0F);
            }
            this.discard();
        }

        super.onCollision(hitResult);
    }

    @Override
    public double getDamage() {
        return super.getDamage() + 500f;
    }


    public static ImmortalArrowEntity create(EntityType<ImmortalArrowEntity> entityType, World world) {
        return new ImmortalArrowEntity(entityType, world);
    }
}
