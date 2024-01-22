package de.takacick.illegalwars.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.illegalwars.access.PiglinProperties;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.block.entity.PiglinGoldTurretBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PiglinGoldTurretBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = Block.createCuboidShape(3.5, 0.0, 3.5, 12.5, 18, 12.5);

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public PiglinGoldTurretBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        if (!world.isClient) {
            if (entity instanceof PiglinEntity piglinEntity
                    && piglinEntity.isAdult()
                    && entity instanceof PiglinProperties piglinProperties
                    && !piglinProperties.isUsingPiglinGoldTurret()) {
                if (world.getBlockEntity(pos) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity
                        && (piglinGoldTurretBlockEntity.getShooter() == null)) {
                    piglinProperties.setPiglinGoldTurret(pos);
                    piglinGoldTurretBlockEntity.setShooter(piglinEntity);
                    world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                }
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (placer != null) {
            if (world.getBlockEntity(pos) instanceof PiglinGoldTurretBlockEntity piglinGoldTurretBlockEntity) {
                piglinGoldTurretBlockEntity.setOwner(placer.getUuid());
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PiglinGoldTurretBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.PIGLIN_GOLD_TURRET, PiglinGoldTurretBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
