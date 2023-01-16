package de.takacick.imagineanything.registry.entity.custom;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.living.HeadEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ThoughtEntity extends MobEntity {
    private static final TrackedData<Integer> OWNER;
    public int thinkingTime = 0;

    static {
        OWNER = DataTracker.registerData(ThoughtEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    public ThoughtEntity(EntityType<? extends ThoughtEntity> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.setNoGravity(true);
    }

    public ThoughtEntity(World world, double x, double y, double z) {
        this(EntityRegistry.THOUGHT, world);
        this.setPosition(x, y, z);
        this.ignoreCameraFrustum = true;
        this.setNoGravity(true);
    }

    public void setOwner(Entity player) {
        if (player != null) {
            getDataTracker().set(OWNER, player.getId());
        } else {
            getDataTracker().set(OWNER, -1);
        }
    }

    public Integer getOwner() {
        return dataTracker.get(OWNER);
    }

    public Entity getOwnerEntity() {
        return world.getEntityById(getOwner());
    }

    public static ThoughtEntity create(EntityType<ThoughtEntity> entityType, World world) {
        return new ThoughtEntity(entityType, world);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(OWNER, -1);
    }

    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 4.0D;
        if (Double.isNaN(d)) {
            d = 4.0D;
        }

        d *= 64.0D;
        return distance < d * d;
    }

    @Override
    public void tick() {
        setThinkingTime(thinkingTime + 3);

        if (!world.isClient) {
            if (thinkingTime <= 0) {
                this.discard();
            }
        }

        Entity owner = getEntityWorld().getEntityById(getOwner());
        if (owner == null || !owner.isAlive()) {
            remove(RemovalReason.DISCARDED);
            return;
        }

        tickRiding();
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public void tickRiding() {
        Entity owner = getEntityWorld().getEntityById(getOwner());
        if (owner != null) {
            updatePassengerPosition(owner, this);
        }
    }

    public void updatePassengerPosition(Entity owner, Entity passenger) {
        updatePassengerPosition(owner, passenger, (Entity::setPosition));
    }

    private void updatePassengerPosition(Entity owner, Entity passenger, PositionUpdater positionUpdater) {
        Vec3d vec3d = owner.getClientCameraPosVec(0);
        positionUpdater.accept(passenger, vec3d.getX(), owner.getY() + owner.getHeight(), vec3d.getZ());
    }

    @Nullable
    @Override
    public Entity getPrimaryPassenger() {
        return super.getPrimaryPassenger();
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public boolean isPartOfGame() {
        return false;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public static float getThinkProgress(int useTicks) {
        float f = (float) (useTicks) / 20.0f;
        if ((f = (f * f + f * 2.0f) / 2.9f) > 1.0f) {
            f = 1.0f;
        }

        return f;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (source.getSource() != null && source.getSource() instanceof PlayerEntity && !world.isClient) {
            Entity owner = getOwnerEntity();
            if (owner instanceof HeadEntity head) {
                dropStack(head.getThoughtStack());
                BionicUtils.sendEntityStatus((ServerWorld) world, this, ImagineAnything.IDENTIFIER, 12);
            }

            this.discard();
        }

        return false;
    }

    public void setThinkingTime(int thinkingTime) {
        this.thinkingTime = Math.min(thinkingTime, 100);
    }
}