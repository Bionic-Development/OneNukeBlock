package de.takacick.onenukeblock.registry.block;

import com.mojang.serialization.MapCodec;
import de.takacick.onenukeblock.registry.block.entity.BladedTntBlockEntity;
import de.takacick.onenukeblock.registry.entity.custom.BladedTntEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class BladedTntBlock extends AbstractTntBlock {

    public static final MapCodec<BladedTntBlock> CODEC = AbstractTntBlock.createCodec(BladedTntBlock::new);

    public MapCodec<BladedTntBlock> getCodec() {
        return CODEC;
    }

    public BladedTntBlock(Settings settings) {
        super(settings);
    }

    public void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (world.isClient) {
            return;
        }
        BladedTntEntity tntEntity = new BladedTntEntity(world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, igniter);
        world.spawnEntity(tntEntity);
        world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BladedTntBlockEntity(pos, state);
    }
}
