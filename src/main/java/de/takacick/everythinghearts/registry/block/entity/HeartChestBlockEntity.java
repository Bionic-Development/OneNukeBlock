package de.takacick.everythinghearts.registry.block.entity;

import de.takacick.everythinghearts.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class HeartChestBlockEntity extends ChestBlockEntity {

    private static final Text TITLE = Text.translatable("block.everythinghearts.heart_chest");

    public HeartChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityRegistry.HEART_CHEST, blockPos, blockState);
    }

    @Override
    public Text getName() {
        return TITLE;
    }
}
