package de.takacick.tinyhouse.registry.block;

import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.block.entity.SpinningPeepeeChoppaBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpinningPeepeeChoppa extends BlockWithEntity {

    public static final BooleanProperty POWERED = Properties.POWERED;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 20.0, 11.0);

    public SpinningPeepeeChoppa(Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SpinningPeepeeChoppaBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, EntityRegistry.SPINNING_PEEPEE_CHOPPA, SpinningPeepeeChoppaBlockEntity::tick);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        updateBlockState(world, state, pos);

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        updateBlockState(world, state, pos);
    }

    private void updateBlockState(World world, BlockState state, BlockPos pos) {
        boolean bl = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean bl2 = state.get(POWERED);

        if (bl) {
            if (!bl2) {
                world.setBlockState(pos, state.with(POWERED, true));
            } else {
                world.setBlockState(pos, state.with(POWERED, false));
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {

        builder.add(POWERED);

        super.appendProperties(builder);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§eWill §chop off §eany limb within a blink"));
        tooltip.add(Text.of("§eof an eye when spinning!"));
        super.appendTooltip(stack, world, tooltip, options);
    }
}
