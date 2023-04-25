package de.takacick.everythinghearts.registry.entity.living;

import de.takacick.everythinghearts.mixin.LivingEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProtoEntity extends WolfEntity {

    public ProtoEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1f;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!isTamed()) {
            setTamed(true);
            setOwner(player);
        }

        if (hasPassengers()) {
            return ActionResult.FAIL;
        }

        player.startRiding(this, true);
        this.setSitting(false);
        return ActionResult.SUCCESS;
    }

    @Override
    public WolfEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    @Nullable
    @Override
    public Entity getPrimaryPassenger() {
        return getFirstPassenger();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (!this.isAlive()) {
            return;
        }

        if (!this.hasPassengers() || !(this.getPrimaryPassenger() instanceof LivingEntity livingEntity)) {
            this.airStrafingSpeed = 0.02f;
            super.travel(movementInput);
            return;
        }

        setJumping(((LivingEntityAccessor) livingEntity).isJumping());
        this.setYaw(livingEntity.getYaw());
        this.prevYaw = this.getYaw();
        this.setPitch(livingEntity.getPitch() * 0.5f);
        this.setRotation(this.getYaw(), this.getPitch());
        this.headYaw = this.bodyYaw = this.getYaw();
        float f = livingEntity.sidewaysSpeed * 0.5f;
        float g = livingEntity.forwardSpeed;
        if (g <= 0.0f) {
            g *= 0.25f;
        }
        if (!this.onGround && !this.jumping) {
            f = 0.0f;
            g = 0.0f;
        }
        if (this.onGround && this.jumping) {
            double d = 0.42f * this.getJumpVelocityMultiplier();
            double e = d + this.getJumpBoostVelocityModifier();
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, e, vec3d.z);
            this.velocityDirty = true;
            if (g > 0.0f) {
                float h = MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
                float i = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
                this.setVelocity(this.getVelocity().add(-0.4f * h, 0.0, 0.4f * i));
            }
        }

        this.airStrafingSpeed = this.getMovementSpeed() * 0.1f;
        if (this.isLogicalSideForUpdatingMovement()) {
            this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            super.travel(new Vec3d(f, movementInput.y, g));
        } else if (livingEntity instanceof PlayerEntity) {
            this.setVelocity(Vec3d.ZERO);
        }

        this.updateLimbs(this, false);
        this.tryCheckBlockCollision();
    }

    @Override
    public void setTamed(boolean tamed) {
        byte b = this.dataTracker.get(TAMEABLE_FLAGS);
        if (tamed) {
            this.dataTracker.set(TAMEABLE_FLAGS, (byte) (b | 4));
        } else {
            this.dataTracker.set(TAMEABLE_FLAGS, (byte) (b & 0xFFFFFFFB));
        }
        this.onTamedChanged();
    }
}