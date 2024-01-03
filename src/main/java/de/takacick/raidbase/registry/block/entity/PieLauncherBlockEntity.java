package de.takacick.raidbase.registry.block.entity;

import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.raidbase.registry.block.PieLauncherBlock;
import de.takacick.raidbase.registry.entity.projectiles.PieEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PieLauncherBlockEntity extends BlockEntity {

    private final AnimationState SHOOT = new AnimationState();

    private int age = 0;

    private UUID owner;
    private int target = -1;
    private boolean synced = false;

    private int charge = 0;
    private int cooldown = 2;

    private float yaw = getCachedState().get(PieLauncherBlock.FACING).asRotation();
    private float pitch = 0;
    private float prevYaw = getCachedState().get(PieLauncherBlock.FACING).asRotation();
    private float prevPitch = 0;

    public PieLauncherBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.PIE_LAUNCHER, blockPos, blockState);
    }

    public static void tick(World world, BlockPos pos, BlockState state, PieLauncherBlockEntity blockEntity) {
        blockEntity.age++;
        if (!world.isClient) {
            Vec3d vec3d = blockEntity.getHorizontalRotationVec().multiply(0.5);

            Vec3d center = Vec3d.ofBottomCenter(pos).add(vec3d).add(0, 1.125, 0);

            LivingEntity target = world.getClosestEntity(LivingEntity.class,
                    TargetPredicate.createAttackable().setPredicate(livingEntity ->
                            blockEntity.canSee(center, livingEntity) &&
                            (livingEntity instanceof HostileEntity || livingEntity instanceof PlayerEntity)
                                    && !((livingEntity.getUuid().equals(blockEntity.owner)
                                    || (livingEntity instanceof Tameable tameable && (blockEntity.owner != null && blockEntity.owner.equals(tameable.getOwnerUuid()))))
                                    && Math.abs(livingEntity.getPos().add(0, livingEntity.getHeight() * 0.4, 0).subtract(center).normalize().getY()) < 0.95)),
                    null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    new Box(pos).expand(12));

            if (blockEntity.cooldown > 0) {
                blockEntity.cooldown--;
            } else {
                blockEntity.charge++;
            }

            if (target != null) {
                blockEntity.target = target.getId();
                if (blockEntity.lookAt(pos, target.getPos().add(0, target.getHeight() * 0.7, 0))
                        && blockEntity.getChargeProgress(0f) >= 1f) {

                    world.playSound(null, pos, SoundEvents.ENTITY_EGG_THROW, SoundCategory.HOSTILE, 1f, 1f);

                    PieEntity pieEntity = new PieEntity(world, center.getX(), center.getY(), center.getZ(), null);
                    setVelocity(pieEntity, blockEntity.pitch, blockEntity.yaw, 0, 1.8f, 1f);
                    pieEntity.setPosition(center.getX(), center.getY(), center.getZ());
                    world.spawnEntity(pieEntity);
                    blockEntity.charge = 0;
                    blockEntity.cooldown = world.getRandom().nextBetween(30, 40);
                    world.syncWorldEvent(634123, pos, 0);
                }

                blockEntity.markDirty();
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            } else {
                blockEntity.prevPitch = blockEntity.getPitch();
                blockEntity.prevYaw = blockEntity.getYaw();

                if (blockEntity.target != -1) {
                    blockEntity.target = -1;
                    blockEntity.markDirty();
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
            }
        } else {
            Entity target = world.getEntityById(blockEntity.target);

            if (blockEntity.cooldown > 0) {
                blockEntity.cooldown--;
            } else {
                blockEntity.charge++;
            }

            if (target != null) {
                blockEntity.lookAt(pos, target.getEyePos());
                blockEntity.markDirty();
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            } else {
                blockEntity.prevPitch = blockEntity.getPitch();
                blockEntity.prevYaw = blockEntity.getYaw();
            }

            blockEntity.synced = true;
        }
    }

    public boolean canSee(Vec3d pos, Entity entity) {
        if (entity.getWorld() != this.getWorld()) {
            return false;
        }
        Vec3d vec3d2 = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
        if (vec3d2.distanceTo(pos) > 128.0) {
            return false;
        }
        return this.getWorld().raycast(new RaycastContext(pos, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS;
    }

    public static void setVelocity(PieEntity pieEntity, float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -MathHelper.sin(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
        float g = -MathHelper.sin((pitch + roll) * ((float) Math.PI / 180));
        float h = MathHelper.cos(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
        pieEntity.setVelocity(f, g, h, speed, divergence);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.owner != null) {
            nbt.putUuid("ownerUuid", this.owner);
        }

        nbt.putFloat("pitch", this.pitch);
        nbt.putFloat("prevPitch", this.prevPitch);

        nbt.putFloat("yaw", this.yaw);
        nbt.putFloat("prevYaw", this.prevYaw);

        nbt.putInt("cooldown", this.cooldown);
        nbt.putInt("charge", this.charge);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("ownerUuid", NbtElement.INT_ARRAY_TYPE)) {
            this.owner = nbt.getUuid("ownerUuid");
        }

        if (nbt.contains("target", NbtElement.INT_TYPE)) {
            this.target = nbt.getInt("target");
        } else {
            this.target = -1;
        }

        if (nbt.contains("cooldown", NbtElement.INT_TYPE)) {
            this.cooldown = nbt.getInt("cooldown");
            this.charge = nbt.getInt("charge");
        }

        if (!this.synced) {
            this.synced = true;
            if (nbt.contains("yaw", NbtElement.FLOAT_TYPE)) {
                this.yaw = nbt.getFloat("yaw");
                this.prevYaw = nbt.getFloat("prevYaw");
            }

            if (nbt.contains("pitch", NbtElement.FLOAT_TYPE)) {
                this.pitch = nbt.getFloat("pitch");
                this.prevPitch = nbt.getFloat("prevPitch");
            }
        }
    }

    protected NbtCompound createSyncNbt(NbtCompound nbt) {
        nbt.putInt("target", this.target);

        writeNbt(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this, (blockEntity) -> createSyncNbt(new NbtCompound()));
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPrevPitch() {
        return prevPitch;
    }

    public float getPrevYaw() {
        return prevYaw;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean lookAt(BlockPos blockPos, Vec3d target) {
        Vec3d vec3d = Vec3d.ofCenter(blockPos);
        double d = target.x - vec3d.x;
        double e = target.y - vec3d.y;
        double f = target.z - vec3d.z;
        double g = Math.sqrt(d * d + f * f);

        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f);
        this.prevPitch = this.getPitch();
        this.prevYaw = this.getYaw();

        this.setPitch(this.changeAngle(this.prevPitch, pitch, 5));
        this.setYaw(this.changeAngle(this.prevYaw, yaw, 5));

        return this.changeAngle(this.prevPitch, pitch, 15) == this.changeAngle(this.prevPitch, pitch, 360)
                && this.changeAngle(this.prevYaw, yaw, 15) == this.changeAngle(this.prevYaw, yaw, 360);
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

    public final Vec3d getRotationVec() {
        return this.getRotationVector(this.getPitch(), this.getYaw());
    }

    public final Vec3d getHorizontalRotationVec() {
        return this.getRotationVector(0, this.getYaw());
    }

    protected final Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

    public float getChargeProgress(float tickDelta) {
        return Math.min(MathHelper.getLerpProgress(this.charge + tickDelta, 0, 20), 1f);
    }

    public int getAge() {
        return age;
    }

    public AnimationState getShootState() {
        return SHOOT;
    }
}

