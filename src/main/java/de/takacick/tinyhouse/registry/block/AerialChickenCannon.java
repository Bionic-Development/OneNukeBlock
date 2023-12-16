package de.takacick.tinyhouse.registry.block;

import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.block.entity.AerialChickenCannonBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AerialChickenCannon extends BlockWithEntity {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(3, 0, 3, 13.0, 12.0, 13.0);

    public AerialChickenCannon(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);

        if (world.getBlockEntity(pos) instanceof AerialChickenCannonBlockEntity aerialChickenCannonBlockEntity) {
            if (itemStack.isEmpty() || !aerialChickenCannonBlockEntity.canLoad(itemStack)) {
                return ActionResult.PASS;
            }

            if (!world.isClient) {
                if (aerialChickenCannonBlockEntity.tryLoad()) {
                    if (!player.isCreative()) {
                        itemStack.decrement(1);
                    }

                    world.syncWorldEvent(813923, pos, 4);
                    world.updateListeners(pos, aerialChickenCannonBlockEntity.getCachedState(),
                            aerialChickenCannonBlockEntity.getCachedState(), Block.NOTIFY_ALL);
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if (world.getBlockEntity(pos) instanceof AerialChickenCannonBlockEntity aerialChickenCannonBlockEntity) {
            aerialChickenCannonBlockEntity.setYaw(placer.getYaw());

            world.updateListeners(pos, aerialChickenCannonBlockEntity.getCachedState(),
                    aerialChickenCannonBlockEntity.getCachedState(), Block.NOTIFY_ALL);
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AerialChickenCannonBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, EntityRegistry.AERIAL_CHICKEN_CANNON, AerialChickenCannonBlockEntity::tick);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§eIs it a bird? A place? NO IT'S AN"));
        tooltip.add(Text.of("§cEXPLOSIVE §fCHICKEN§e!"));

        super.appendTooltip(stack, world, tooltip, options);
    }
}
