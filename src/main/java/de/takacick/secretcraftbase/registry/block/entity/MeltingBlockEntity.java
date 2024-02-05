package de.takacick.secretcraftbase.registry.block.entity;

import com.mojang.serialization.DataResult;
import de.takacick.secretcraftbase.registry.EntityRegistry;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MeltingBlockEntity extends BlockEntity {

    private BlockState blockState;
    private int maxAge = 10;
    private int age = 0;
    private int laserTicks = 3;

    public MeltingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.MELTING_BLOCK, blockPos, blockState);
    }

    public static void tick(World world, BlockPos pos, BlockState state, MeltingBlockEntity blockEntity) {
        if (blockEntity.blockState.isIn(BlockTags.DIAMOND_ORES)) {
            blockEntity.maxAge = 30;
        }

        if (blockEntity.laserTicks > 0) {
            blockEntity.laserTicks--;
            blockEntity.age++;
        } else {
            blockEntity.age--;
            if (!world.isClient && blockEntity.age <= 0) {
                world.setBlockState(pos, blockEntity.blockState, ~Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS | Block.FORCE_STATE | Block.REDRAW_ON_MAIN_THREAD | ~Block.NO_REDRAW);
                blockEntity.markRemoved();
            }
            return;
        }

        if (!world.isClient) {
            if (blockEntity.getMeltingProgress(0f) >= 1.0f) {
                if (blockEntity.blockState.isIn(BlockTags.DIAMOND_ORES)) {
                    world.setBlockState(pos, ItemRegistry.DIAMOND_ORE_CHUNKS.getDefaultState());
                } else {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }

                blockEntity.markRemoved();
            }
        }
    }

    public BlockState getBlockState() {
        return blockState;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (this.blockState != null) {
            nbt.put("blockState", NbtHelper.fromBlockState(this.blockState));
        }
        nbt.putInt("age", this.age);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("blockState", NbtElement.COMPOUND_TYPE)) {
            DataResult<BlockState> blockStateResult = BlockState.CODEC.parse(NbtOps.INSTANCE, nbt.getCompound("blockState"));
            this.blockState = blockStateResult.result().orElse(null);
        }
        this.age = nbt.getInt("age");
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

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public float getMeltingProgress(float tickDelta) {
        return Math.max(Math.min(MathHelper.getLerpProgress(this.age + (this.laserTicks <= 0 ? -tickDelta : tickDelta), 0, this.maxAge), 1f), 0f);
    }

    public void setLaserTicks(int laserTicks) {
        this.laserTicks = laserTicks;
    }
}

