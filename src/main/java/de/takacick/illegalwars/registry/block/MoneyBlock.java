package de.takacick.illegalwars.registry.block;

import de.takacick.illegalwars.registry.entity.custom.MoneyBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MoneyBlock extends Block {

    public MoneyBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            Vec3d entityPos = Vec3d.ofBottomCenter(pos);

            MoneyBlockEntity moneyBlockEntity = new MoneyBlockEntity(world, entityPos.getX(), entityPos.getY(), entityPos.getZ());
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            world.spawnEntity(moneyBlockEntity);
        }

        return ActionResult.SUCCESS;
    }
}
