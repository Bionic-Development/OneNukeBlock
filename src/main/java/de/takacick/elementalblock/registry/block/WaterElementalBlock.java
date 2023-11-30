package de.takacick.elementalblock.registry.block;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.ParticleRegistry;
import de.takacick.elementalblock.registry.particles.ColoredParticleEffect;
import de.takacick.utils.bossbar.BossBarUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class WaterElementalBlock extends ElementalBlock {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.001, 0, 0.001, 15.999, 16, 15.999);
    public static final BossBar.Color COLOR = BossBarUtils.register(new Identifier(OneElementalBlock.MOD_ID, "water_elemental"))
            .texture(new Identifier(OneElementalBlock.MOD_ID, "textures/gui/water_elemental_bar.png"), 256, 10).build();

    public WaterElementalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public String getElement() {
        return "Water";
    }

    @Override
    public BossBar.Color getBossBarColor() {
        return COLOR;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0f;
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {

        if (random.nextInt(64) == 0) {
            world.playSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25f + 0.75f, random.nextFloat() + 0.5f, false);
        }

        Vector3f color = Vec3d.unpackRgb(4159204).toVector3f();

        for (int i = 0; i < 3; ++i) {
            double h = pos.getY() + random.nextDouble() * 1;
            double d = random.nextGaussian() * 0.2;
            double e = random.nextDouble() * 0.3;
            double f = random.nextGaussian() * 0.2;

            world.addParticle(new ColoredParticleEffect(ParticleRegistry.FALLING_WATER, color),
                    true, pos.getX() + 0.5 + d, h + e, pos.getZ() + 0.5 + f, d * 0.3, e * 0.2, f * 0.3);
        }
        super.randomDisplayTick(state, world, pos, random);
    }
}
