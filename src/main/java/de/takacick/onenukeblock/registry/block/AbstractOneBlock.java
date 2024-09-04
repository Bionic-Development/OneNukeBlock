package de.takacick.onenukeblock.registry.block;

import de.takacick.onenukeblock.utils.oneblock.OneBlock;
import de.takacick.onenukeblock.utils.oneblock.OneBlockServerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;

import java.util.function.BiConsumer;

public abstract class AbstractOneBlock extends BlockWithEntity {

    public AbstractOneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onExploded(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        if (!world.isClient) {

            OneBlockServerState.getServerState(world.getServer()).ifPresent(serverState -> {
                serverState.getOneBlock().drop(world, pos);
            });
        }

        world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
    }

    public abstract OneBlock getOneBlock(OneBlockServerState oneBlockServerState);
}
