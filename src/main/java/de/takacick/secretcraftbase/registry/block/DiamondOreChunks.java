package de.takacick.secretcraftbase.registry.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.BlockView;

public class DiamondOreChunks extends ExperienceDroppingBlock {

    public DiamondOreChunks(IntProvider experienceDropped, Settings settings) {
        super(experienceDropped, settings);
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
