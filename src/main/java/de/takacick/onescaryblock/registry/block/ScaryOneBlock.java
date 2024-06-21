package de.takacick.onescaryblock.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.block.entity.ScaryOneBlockBlockEntity;
import de.takacick.onescaryblock.registry.inventory.ScaryOneBlockScreenHandler;
import de.takacick.onescaryblock.utils.oneblock.OneBlockHandler;
import de.takacick.onescaryblock.utils.oneblock.OneBlockServerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class ScaryOneBlock extends BlockWithEntity {

    public static final MapCodec<ScaryOneBlock> CODEC = ScaryOneBlock.createCodec(ScaryOneBlock::new);

    public ScaryOneBlock(Settings settings) {
        super(settings.nonOpaque().dynamicBounds()
                .solidBlock((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
        );
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public void onExploded(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        if (!world.isClient) {

            OneBlockServerState.getServerState(world.getServer()).ifPresent(serverState -> {
                if (serverState.isScaryOneBlockRitual()) {
                    return;
                }
                OneBlockHandler.getInstance().drop(serverState, world, pos);
            });
        }

        world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        if (world.getBlockEntity(pos) instanceof ScaryOneBlockBlockEntity scaryOneBlockEntity && scaryOneBlockEntity.isRitualRunning() && scaryOneBlockEntity.getRitualProgress(0f) >= 1f) {
            return VoxelShapes.empty();
        }

        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new ScaryOneBlockScreenHandler(syncId, inventory, new SimpleInventory(1), ScreenHandlerContext.create(world, pos)), Text.of(""));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ScaryOneBlockBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.SCARY_ONE_BLOCK_BLOCK_ENTITY, ScaryOneBlockBlockEntity::tick);
    }
}
