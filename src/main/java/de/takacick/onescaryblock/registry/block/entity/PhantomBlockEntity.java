package de.takacick.onescaryblock.registry.block.entity;

import de.takacick.onescaryblock.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PhantomBlockEntity extends BlockEntity {

    private int maxAge = Random.create().nextBetween(560, 600);
    private int age = 0;

    public PhantomBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.PHANTOM_BLOCK, blockPos, blockState);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, PhantomBlockEntity blockEntity) {
        blockEntity.age = MathHelper.clamp(blockEntity.getAge() + 1, 0, blockEntity.getMaxAge());

        Random random = world.getRandom();
        if (!world.isClient) {
            if (blockEntity.getAge() >= blockEntity.getMaxAge()) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                world.syncWorldEvent(236741293, blockPos, 1);
            }
        } else {
            if (world.getRandom().nextDouble() <= (float) blockEntity.getAge() / (float) blockEntity.getMaxAge() * 0.75) {
                world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 1.0f,
                        (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f, false);
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putInt("age", this.age);
        nbt.putInt("maxAge", this.maxAge);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        int age = this.age;
        if (nbt.contains("age", NbtElement.INT_TYPE)) {
            age = nbt.getInt("age");
        }

        int maxAge = this.age;
        if (nbt.contains("maxAge", NbtElement.INT_TYPE)) {
            maxAge = nbt.getInt("maxAge");
        }

        if (world == null || !world.isClient) {
            this.age = age;
            this.maxAge = maxAge;
        } else {
            if (Math.abs(this.age - age) > 1) {
                this.age = age;
            }

            this.maxAge = maxAge;
        }

        super.readNbt(nbt);
    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return this.maxAge;
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
}

