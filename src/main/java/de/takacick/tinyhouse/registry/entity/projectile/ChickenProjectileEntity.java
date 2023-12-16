package de.takacick.tinyhouse.registry.entity.projectile;

import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.server.explosion.ChickenExplosionHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class ChickenProjectileEntity extends PersistentProjectileEntity {

    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0f;
    private int currentFuseTime;
    private int fuseTime = 1200;

    public ChickenProjectileEntity(EntityType<? extends ChickenProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ChickenProjectileEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.CHICKEN, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setOwner(owner);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public void setProperties(float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -MathHelper.sin(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
        float g = -MathHelper.sin((pitch + roll) * ((float) Math.PI / 180));
        float h = MathHelper.cos(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
        super.setVelocity(f, g, h, speed, divergence);
    }

    public static ChickenProjectileEntity create(EntityType<ChickenProjectileEntity> entityType, World world) {
        return new ChickenProjectileEntity(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            int i;
            if ((i = this.getFuseSpeed()) > 0 && this.currentFuseTime == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.5f);
                this.emitGameEvent(GameEvent.PRIME_FUSE);
            }
            this.currentFuseTime += i;
            if (this.currentFuseTime < 0) {
                this.currentFuseTime = 0;
            }
            if (this.currentFuseTime >= this.fuseTime) {
                this.currentFuseTime = this.fuseTime;
                this.explode();
                this.discard();
            }
        }
        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.isOnGround() ? -1.0f : 4.0f) * 0.3f;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0f, 1.0f);
        if (!this.isOnGround() && this.flapSpeed < 1.0f) {
            this.flapSpeed = 1.0f;
        }
        this.flapSpeed *= 0.9f;
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(0.9, 0.6, 0.9));
        }
        this.flapProgress += this.flapSpeed * 2.0f;

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
            ChickenExplosionHandler.createExplosion((ServerWorld) getWorld(), this, null, null,
                    getX(), getY(), getZ(), 4f, false, Explosion.DestructionType.DESTROY);
        }
    }

    public int getFuseSpeed() {
        return 1;
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

    public int getFuseTime() {
        return fuseTime;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return !getType().equals(entity.getType()) && super.canHit(entity);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    public int getCurrentFuseTime() {
        return currentFuseTime;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Fuse", (short) this.fuseTime);
        nbt.putInt("CurrentFuse", (short) this.currentFuseTime);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Fuse", NbtElement.NUMBER_TYPE)) {
            this.fuseTime = nbt.getShort("Fuse");
        }
        if (nbt.contains("CurrentFuse", NbtElement.INT_TYPE)) {
            this.currentFuseTime = nbt.getInt("CurrentFuse");
        }
    }
}