package de.takacick.onedeathblock.registry.block;

import de.takacick.onedeathblock.damage.DeathDamageSources;
import de.takacick.onedeathblock.registry.block.entity.SpikedBedBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpikedBedBlock extends BedBlock {

    public SpikedBedBlock(DyeColor color, Settings settings) {
        super(color, settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {

        if (!world.isClient) {
            if (!(entity instanceof PlayerEntity livingEntity) || !(livingEntity.getSleepingPosition().isPresent()
                    && world.getBlockEntity(livingEntity.getSleepingPosition().get()) instanceof SpikedBedBlockEntity)) {
                entity.damage(DeathDamageSources.SPIKED_BED, 1f);
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SpikedBedBlockEntity(pos, state, getColor());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§eThis can't be very comfortable..."));

        super.appendTooltip(stack, world, tooltip, options);
    }
}
