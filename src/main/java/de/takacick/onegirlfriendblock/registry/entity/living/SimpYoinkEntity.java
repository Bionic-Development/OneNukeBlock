package de.takacick.onegirlfriendblock.registry.entity.living;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.UUID;

public class SimpYoinkEntity extends PathAwareEntity {

    private float yaw = 0f;
    private float pitch = 0f;
    private float prevYaw = 0f;
    private float prevPitch = 0f;
    private int passengerTicks = 0;

    private static final TrackedData<Optional<UUID>> TARGET = DataTracker.registerData(SimpYoinkEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Vector3f> VECTOR = DataTracker.registerData(SimpYoinkEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
    public Entity target = null;

    public SimpYoinkEntity(EntityType<? extends SimpYoinkEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(TARGET, Optional.empty());
        getDataTracker().startTracking(VECTOR, new Vector3f(0, 0, 0));
    }

    @Override
    public void tick() {
        super.tick();
        UUID targetUuid = getYoinkTarget();

        if (targetUuid != null) {
            this.target = getWorld().getPlayerByUuid(targetUuid);
        } else if (!getWorld().isClient) {
            this.discard();
        }

        if (this.target != null) {
            if (!hasPassengers()) {
                Vec3d ownerPos = target.getPos().add(0, -target.getHeight() / 2, 0);

                ownerPos = ownerPos.multiply(1, 0, 1).add(0, target.getBodyY(0.5), 0);

                Vec3d thisPos = getPos();
                Vec3d difference = ownerPos.subtract(thisPos);

                getDataTracker().set(VECTOR, difference.normalize().multiply(1.3).toVector3f());

                if (!getWorld().isClient) {
                    getWorld().getOtherEntities(this, getBoundingBox().expand(0.2)).forEach(entity -> {
                        if (entity.equals(this.target)) {
                            this.target.startRiding(this, true);
                            getWorld().playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_HORSE_SADDLE, SoundCategory.PLAYERS, 1f, 1f);
                        }
                    });
                }
                lookAt(getEyePos(), this.target.getEyePos());
            } else {
                this.passengerTicks++;
                Vec3d vec3d = new Vec3d(getDataTracker().get(VECTOR));

                if (vec3d.getY() != 0) {
                    vec3d = vec3d.multiply(1, 0, 1).normalize();
                }

                getDataTracker().set(VECTOR, vec3d.multiply(1.001f).toVector3f());
                lookAt(getEyePos(), getEyePos().add(vec3d.multiply(5)));

                if (this.passengerTicks > 200 && !getWorld().isClient) {
                    getPassengerList().forEach(entity -> {
                        if (entity instanceof ServerPlayerEntity playerEntity) {
                            playerEntity.changeGameMode(GameMode.SPECTATOR);
                        }
                    });
                    this.discard();
                }
            }
        } else {
            this.discard();
        }

        Vec3d vec3d = new Vec3d(getDataTracker().get(VECTOR));

        if (vec3d != null) {
            setVelocity(vec3d.getX(), vec3d.getY(), vec3d.getZ());
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (amount > 10000) {
            discard();
        }

        return false;
    }

    public void setYoinkTarget(LivingEntity livingEntity) {
        getDataTracker().set(TARGET, livingEntity == null ? Optional.empty() : Optional.of(livingEntity.getUuid()));
    }

    public UUID getYoinkTarget() {
        return getDataTracker().get(TARGET).orElse(null);
    }

    public void lookAt(Vec3d eyePos, Vec3d target) {
        double d = target.x - eyePos.x;
        double e = target.y - eyePos.y;
        double f = target.z - eyePos.z;
        double g = Math.sqrt(d * d + f * f);

        float maxChange = this.age <= 1 ? 180 : 5;

        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f);
        this.prevPitch = this.pitch;
        this.prevYaw = this.yaw;
        this.pitch = this.changeAngle(this.prevPitch, pitch, maxChange);
        this.yaw = this.changeAngle(this.prevYaw, yaw, maxChange);

        if (this.age <= 1) {
            this.prevPitch = this.pitch;
            this.prevYaw = this.yaw;
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public float getClientYaw(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevYaw, this.yaw);
    }

    public float getClientPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevPitch, this.pitch);
    }

    private float changeAngle(float from, float to, float max) {
        float f = MathHelper.wrapDegrees(to - from);
        if (f > max) {
            f = max;
        }
        if (f < -max) {
            f = -max;
        }
        return from + f;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }
}