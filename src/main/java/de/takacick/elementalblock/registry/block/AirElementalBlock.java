package de.takacick.elementalblock.registry.block;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.utils.bossbar.BossBarUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AirElementalBlock extends ElementalBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.001, 0, 0.001, 15.999, 16, 15.999);
    public static final BossBar.Color COLOR = BossBarUtils.register(new Identifier(OneElementalBlock.MOD_ID, "air_elemental"))
            .texture(new Identifier(OneElementalBlock.MOD_ID, "textures/gui/air_elemental_bar.png"), 256, 10).alpha(0.7f).build();

    public AirElementalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public String getElement() {
        return "Air";
    }

    @Override
    public BossBar.Color getBossBarColor() {
        return COLOR;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.isOf(this)) {
            return true;
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}
