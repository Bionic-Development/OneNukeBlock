package de.takacick.raidbase.registry.block.entity;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class BeaconDeathLaserBlockEntity extends BlockEntity {
    public static final RegistryKey<DamageType> BEACON_DEATH_LASER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RaidBase.MOD_ID, "beacon_death_laser"));

    private boolean update = false;
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private boolean move = false;

    private int age;
    private int prevAge;
    private float yaw = 0;
    private float pitch = 0;

    private float length = 0f;

    public BeaconDeathLaserBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.BEACON_DEATH_LASER, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, BeaconDeathLaserBlockEntity blockEntity) {
        Vec3d vec3d = blockEntity.getRotationVector();
        Vec3d pos = new Vec3d(blockPos.getX() + 0.5 + blockEntity.x, blockPos.getY() + blockEntity.y, blockPos.getZ() + 0.5 + blockEntity.z);

        blockEntity.prevAge = blockEntity.age;

        if (blockEntity.move) {
            blockEntity.age++;
            blockEntity.update = true;
        }

        if (raycast(world, pos, vec3d, 20, false) instanceof BlockHitResult blockHitResult) {
            blockEntity.length = (float) blockHitResult.getPos().distanceTo(pos) + 0.125f;
        } else {
            blockEntity.length = 20.15f;
        }

        if (!world.isClient) {

            if (blockEntity.update) {
                world.updateListeners(blockEntity.getPos(), blockEntity.getCachedState(),
                        blockEntity.getCachedState(), Block.NOTIFY_ALL);
            }

            Set<Entity> targets = new HashSet<>();

            for (int i = 0; i < 10 * blockEntity.length; i++) {
                pos = pos.add(vec3d.multiply(0.1));

                world.getOtherEntities(null, new Box(pos.add(0.02, 0.02, 0.02), pos.add(-0.02, -0.02, -0.02))).forEach(targets::add);
            }

            if (!targets.isEmpty()) {

                targets.forEach(entity -> {
                    if (entity.isInvisible() || entity.isSpectator()
                            || entity.isInvulnerable() || !entity.isAlive()
                            || entity instanceof PlayerEntity player && player.isCreative()) {
                        return;
                    }

                    BionicUtils.sendEntityStatus((ServerWorld) world, entity, RaidBase.IDENTIFIER, 1);
                    entity.damage(world.getDamageSources().create(BEACON_DEATH_LASER), 2f);
                    if (!entity.isLiving()) {
                        entity.discard();
                    }
                });
            }
        }
    }

    public static HitResult raycast(World world, Vec3d pos, Vec3d rotation, double maxDistance, boolean includeFluids) {
        Vec3d vec3d3 = pos.add(rotation.x * maxDistance, rotation.y * maxDistance, rotation.z * maxDistance);
        return world.raycast(new RaycastContext(pos, vec3d3, RaycastContext.ShapeType.COLLIDER, includeFluids
                ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE, null));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("prevAge", this.prevAge);
        nbt.putInt("age", this.age);
        nbt.putDouble("offsetX", this.x);
        nbt.putDouble("offsetY", this.y);
        nbt.putDouble("offsetZ", this.z);
        nbt.putBoolean("move", this.move);

        nbt.putFloat("yaw", this.yaw);
        nbt.putFloat("pitch", this.pitch);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if(Math.abs(this.age - nbt.getInt("age")) > 1) {
            this.age = nbt.getInt("age");
        }
        if (this.world != null && !this.world.isClient) {
            this.age = nbt.getInt("age");
            this.prevAge = nbt.getInt("prevAge");
        }
        this.x = nbt.getDouble("offsetX");
        this.y = nbt.getDouble("offsetY");
        this.z = nbt.getDouble("offsetZ");
        this.move = nbt.getBoolean("move");

        this.yaw = nbt.getFloat("yaw");
        this.pitch = nbt.getFloat("pitch");

        super.readNbt(nbt);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void setOffset(Vec3d offset) {
        this.x = MathHelper.clamp(offset.getX(), -0.45, 0.45);
        this.y = MathHelper.clamp(offset.getY(), 0.05, 0.95);
        this.z = MathHelper.clamp(offset.getZ(), -0.45, 0.45);
    }

    public Vec3d getOffset() {
        return new Vec3d(this.x, this.y, this.z);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw(float tickDelta) {
        return (float) (Math.sin((MathHelper.lerp(tickDelta, (float) this.prevAge, this.age) * 0.01) * Math.PI) * 45f + this.yaw);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getPitch() {
        return pitch;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getLength() {
        return length;
    }

    public Vec3d getRotationVector() {
        return getRotationVector(getPitch(), getYaw(1f) - 180);
    }

    public void setMoving(boolean move) {
        this.move = move;
    }

    public boolean isMoving() {
        return this.move;
    }

    public void markUpdate() {
        this.update = true;
    }

    protected static final Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float) Math.PI / 180);
        float g = -yaw * ((float) Math.PI / 180);
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

}
