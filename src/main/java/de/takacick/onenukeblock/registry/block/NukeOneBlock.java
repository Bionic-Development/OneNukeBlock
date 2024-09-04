package de.takacick.onenukeblock.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.onenukeblock.registry.EntityRegistry;
import de.takacick.onenukeblock.registry.block.entity.NukeOneBlockEntity;
import de.takacick.onenukeblock.registry.inventory.NukeOneBlockScreenHandler;
import de.takacick.onenukeblock.utils.oneblock.OneBlock;
import de.takacick.onenukeblock.utils.oneblock.OneBlockServerState;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class NukeOneBlock extends AbstractOneBlock {

    public static final MapCodec<NukeOneBlock> CODEC = NukeOneBlock.createCodec(NukeOneBlock::new);

    public NukeOneBlock(Settings settings) {
        super(settings.nonOpaque()
                .solidBlock((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
        );
    }

    @Override
    protected MapCodec<? extends NukeOneBlock> getCodec() {
        return CODEC;
    }

    @Override
    public OneBlock getOneBlock(OneBlockServerState oneBlockServerState) {
        return oneBlockServerState.getOneBlock();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NukeOneBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.NUKE_ONE_BLOCK, NukeOneBlockEntity::tick);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new NukeOneBlockScreenHandler(syncId, inventory, new SimpleInventory(2), ScreenHandlerContext.create(world, pos)), Text.of(""));
    }

}
