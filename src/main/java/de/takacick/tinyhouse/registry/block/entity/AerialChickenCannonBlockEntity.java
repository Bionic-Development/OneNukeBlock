package de.takacick.tinyhouse.registry.block.entity;

import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.entity.projectile.ChickenProjectileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AerialChickenCannonBlockEntity extends BlockEntity {

    private float yaw = 0;
    private int prevLoading = -1;
    private int loading = -1;
    private boolean loaded = false;

    public AerialChickenCannonBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.AERIAL_CHICKEN_CANNON, pos, state);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, AerialChickenCannonBlockEntity blockEntity) {
        blockEntity.prevLoading = blockEntity.loading;

        if (world.isClient) {
            if (blockEntity.loading > 0) {
                blockEntity.loading--;
            }
        } else if (blockEntity.loaded) {
            if (blockEntity.loading > -3) {
                blockEntity.loading--;
            } else {
                blockEntity.prevLoading = -1;
                blockEntity.loading = -1;
                blockEntity.loaded = false;

                Vec3d vec3d = blockEntity.getRotationVector();
                Vec3d pos = Vec3d.ofCenter(blockPos).add(vec3d);

                ChickenProjectileEntity chickenProjectileEntity = new ChickenProjectileEntity(world, pos.getX(), pos.getY() - 0.35f, pos.getZ(), null);
                chickenProjectileEntity.setProperties(-4.5f + (float) world.getRandom().nextGaussian() * 3f, blockEntity.getYaw() +  (float) world.getRandom().nextGaussian() * 10f, 0.0f, 1.0f + (float) world.getRandom().nextDouble(), 1f);
                world.spawnEntity(chickenProjectileEntity);

                world.syncWorldEvent(813923, blockPos, 5);

                world.updateListeners(blockPos, blockEntity.getCachedState(),
                        blockEntity.getCachedState(), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putFloat("yaw", this.yaw);
        nbt.putInt("prevLoading", this.prevLoading);
        nbt.putInt("loading", this.loading);
        nbt.putBoolean("loaded", this.loaded);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.yaw = nbt.getFloat("yaw");
        this.prevLoading = nbt.getInt("prevLoading");
        this.loading = nbt.getInt("loading");
        this.loaded = nbt.getBoolean("loaded");

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

    public boolean tryLoad() {
        boolean bl = this.loading <= -1 && !loaded;
        if (bl) {
            this.prevLoading = 5;
            this.loading = 5;
            this.loaded = true;
        }
        return bl;
    }

    public boolean canLoad(ItemStack itemStack) {
        return itemStack.isOf(Items.WHEAT_SEEDS);
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setLoading(int loading) {
        this.loading = loading;
    }

    public void setPrevLoading(int prevLoading) {
        this.prevLoading = prevLoading;
    }

    public float getLoadingProgress(float tickDelta) {
        return loading <= -1 ? 0f : (5f - MathHelper.lerp(tickDelta, (float) prevLoading, loading)) / 5f;
    }

    public Vec3d getRotationVector() {
        return getRotationVector(-2.5f, getYaw());
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

}
