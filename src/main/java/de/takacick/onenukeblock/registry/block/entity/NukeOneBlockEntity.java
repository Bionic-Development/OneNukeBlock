package de.takacick.onenukeblock.registry.block.entity;

import de.takacick.onenukeblock.registry.EntityRegistry;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class NukeOneBlockEntity extends BlockEntity {

    private int delay = 0;
    private int fuse = 0;
    private int maxFuse = 200;
    private boolean ignited = false;

    public NukeOneBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.NUKE_ONE_BLOCK, blockPos, blockState);
        this.delay = Random.create(blockPos.asLong()).nextBetween(2400, 6000);
    }

    public static void tick(World world, BlockPos pos, BlockState state, NukeOneBlockEntity blockEntity) {
        blockEntity.tick(world, pos, state);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (this.delay > 0) {
            this.delay--;
            if (this.delay <= 0) {
                this.fuse = this.maxFuse;
            }
        } else if (this.fuse > 0) {
            if (this.fuse % 20 == 0) {
                Vec3d vec3d = pos.toCenterPos();

                world.playSound(vec3d.getX(), vec3d.getY(), vec3d.getZ(),
                        ParticleRegistry.NUKE_ALERT, SoundCategory.AMBIENT, 6f, 1f, false);
            }

            this.fuse--;
            this.ignited = true;

            if (this.fuse <= 0) {
                this.delay = Random.create(pos.asLong() + world.getTime()).nextBetween(2400, 6000);
            }
        }

        this.ignited = this.fuse > 0 && this.delay <= 0;

        world.updateListeners(getPos(), getCachedState(),
                getCachedState(), Block.NOTIFY_ALL);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);

        nbt.putInt("delay", this.delay);
        nbt.putInt("fuse", this.fuse);
        nbt.putBoolean("ignited", this.ignited);
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

        if (nbt.contains("delay", NbtElement.INT_TYPE)) {
            this.delay = nbt.getInt("delay");
        }

        if (nbt.contains("fuse", NbtElement.INT_TYPE)) {
            int fuse = nbt.getInt("fuse");
            if (MathHelper.abs(fuse - this.fuse) > 2) {
                this.fuse = fuse;
            }
        }

        this.ignited = nbt.getBoolean("ignited");
    }

    public void ignite() {
        if (isIgnited()) {
            return;
        }

        this.fuse = 200;
        this.delay = 0;
    }

    public boolean isIgnited() {
        return this.ignited;
    }

    public int getFuse() {
        return this.fuse;
    }

    public int getMaxFuse() {
        return this.maxFuse;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}

