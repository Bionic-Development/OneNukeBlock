package de.takacick.everythinghearts.registry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BasicHeartBlock extends Block {
    private static final VoxelShape shape = createShape();

    public BasicHeartBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape;
    }

    public static VoxelShape createShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.216666875, 0.184895625, 0, 0.43888874999999994, 0.9626737499999996, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5499999999999999, 0.184895625, 0, 0.7722224999999999, 0.9626737499999996, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.10555562500000001, 0.2960068750000002, 0, 0.216666875, 0.8515624999999998, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.43888874999999994, -0.03732624999999999, 0, 0.5499999999999999, 0.8515624999999998, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.7722224999999999, 0.2960068750000002, 0, 0.8833331249999999, 0.8515624999999998, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.005555624999999998, 0.4071181250000001, 0, 0.10555562500000001, 0.7404512499999998, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.8833331249999999, 0.4071181250000001, 0, 0.9944443749999999, 0.7404512499999998, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.32777749999999994, 0.07378499999999993, 0, 0.43888874999999994, 0.184895625, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.5499999999999999, 0.07378499999999993, 0, 0.6611112499999999, 0.184895625, 1.010416875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.184895625, 0.227430625, 0.9996524999999999, 0.9626737499999996, 0.4496525));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.184895625, 0.56076375, 0.9996524999999999, 0.9626737499999996, 0.78298625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.2960068750000002, 0.116319375, 0.9996524999999999, 0.8515624999999998, 0.227430625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, -0.03732624999999999, 0.4496525, 0.9996524999999999, 0.8515624999999998, 0.56076375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.2960068750000002, 0.78298625, 0.9996524999999999, 0.8515624999999998, 0.8940975));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.4071181250000001, 0.005208125, 0.9996524999999999, 0.7404512499999998, 0.116319375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.4071181250000001, 0.8940975, 0.9996524999999999, 0.7404512499999998, 1.005208125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.07378499999999993, 0.338541875, 0.9996524999999999, 0.184895625, 0.4496525));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.01076375, 0.07378499999999993, 0.56076375, 0.9996524999999999, 0.184895625, 0.671875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.21840250000000003, -0.041145624999999984, 0.227430625, 0.9961806249999999, 1.0, 0.4496525));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.21840250000000003, -0.041145624999999984, 0.56076375, 0.9961806249999999, 1.0, 0.78298625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.32951374999999994, -0.041145624999999984, 0.116319375, 0.8850693749999999, 1.0, 0.227430625));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(-0.0038193749999999964, -0.041145624999999984, 0.4496525, 0.8850693749999999, 1.0, 0.56076375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.32951374999999994, -0.041145624999999984, 0.78298625, 0.8850693749999999, 1.0, 0.8940975));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.44062499999999993, -0.041145624999999984, 0.005208125, 0.7739581249999999, 1.0, 0.116319375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.44062499999999993, -0.041145624999999984, 0.8940975, 0.7739581249999999, 1.0, 1.005208125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.10729187500000002, -0.041145624999999984, 0.338541875, 0.21840250000000003, 1.0, 0.4496525));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.10729187500000002, -0.041145624999999984, 0.56076375, 0.21840250000000003, 1.0, 0.671875));

        return shape;
    }
}
