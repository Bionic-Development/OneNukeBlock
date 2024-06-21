package de.takacick.onescaryblock.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.block.entity.Item303BlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Item303Block extends BlockWithEntity {

    public static final MapCodec<Item303Block> CODEC = Item303Block.createCodec(Item303Block::new);

    public Item303Block(Settings settings) {
        super(settings.nonOpaque()
                .solidBlock((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
        );
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new Item303BlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.ITEM_303, Item303BlockEntity::tick);
    }
}
