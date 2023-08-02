package de.takacick.onedeathblock.registry.block.entity;

import de.takacick.onedeathblock.registry.EntityRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

public class SpikedBedBlockEntity extends BlockEntity {
    private DyeColor color;

    public SpikedBedBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SPIKED_BED, pos, state);
        this.color = ((BedBlock) state.getBlock()).getColor();
    }

    public SpikedBedBlockEntity(BlockPos pos, BlockState state, DyeColor color) {
        super(EntityRegistry.SPIKED_BED, pos, state);
        this.color = color;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }
}