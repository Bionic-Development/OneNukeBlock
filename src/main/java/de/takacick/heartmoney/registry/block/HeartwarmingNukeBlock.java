package de.takacick.heartmoney.registry.block;

import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.block.entity.HeartwarmingNukeBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartwarmingNukeBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = createShape();

    public HeartwarmingNukeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof HeartwarmingNukeBlockEntity heartwarmingNukeBlockEntity && !heartwarmingNukeBlockEntity.enabled) {
            heartwarmingNukeBlockEntity.setEnabled(true);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, EntityRegistry.HEARTWARMING_NUKE_BLOCK, HeartwarmingNukeBlockEntity::tick);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HeartwarmingNukeBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§eWhat a nice way to show your love"));
        tooltip.add(Text.of("§eglobally!"));
        tooltip.add(Text.of("§c§oHigh Tier"));

        super.appendTooltip(stack, world, tooltip, options);
    }

    public static VoxelShape createShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0.40625, 0.125, 0.125, 0.59375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0, 0.40625, 0.25, 0.25, 0.59375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.0625, 0.40625, 0.375, 0.3125, 0.59375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0, 0.40625, 1, 0.125, 0.59375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.75, 0, 0.40625, 0.875, 0.25, 0.59375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.625, 0.0625, 0.40625, 0.75, 0.3125, 0.59375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.3125, 0.375, 0.625, 0.4375, 0.625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.40625, 0, 0, 0.59375, 0.125, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.40625, 0, 0.875, 0.59375, 0.125, 1));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.40625, 0, 0.125, 0.59375, 0.25, 0.25));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.40625, 0.0625, 0.25, 0.59375, 0.3125, 0.375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.40625, 0, 0.75, 0.59375, 0.25, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.40625, 0.0625, 0.625, 0.59375, 0.3125, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.4375, 0.1875, 0.375, 0.5625, 0.3125, 0.625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5625, 0.1875, 0.4375, 0.625, 0.3125, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 0.1875, 0.4375, 0.4375, 0.3125, 0.5625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 0.4375, 0.25, 0.75, 0.5625, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.5625, 0.1875, 0.8125, 0.6875, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.6875, 0.125, 0.875, 0.8125, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.8125, 0.0625, 0.9375, 1.5, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 1.5, 0.125, 0.875, 1.625, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 1.625, 0.1875, 0.8125, 1.75, 0.8125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.25, 1.75, 0.25, 0.75, 1.875, 0.75));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.375, 1.875, 0.375, 0.625, 2, 0.625));

        return shape;
    }
}

