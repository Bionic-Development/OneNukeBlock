package de.takacick.secretgirlbase.registry.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

import java.util.function.Supplier;

public class RedButtonBlock extends AbstractButtonBlock {
    public static final IntProperty TYPE = IntProperty.of("type", 0, 3);

    public RedButtonBlock(AbstractBlock.Settings settings) {
        super(false, settings);

        setDefaultState(getDefaultState().with(TYPE, 0));
    }

    @Override
    protected MapCodec<? extends WallMountedBlock> getCodec() {
        return null;
    }

    public int getPressTicks() {
        return 10;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(POWERED).booleanValue()) {
            return ActionResult.CONSUME;
        }

        ItemStack itemStack = player.getStackInHand(hand);

        int type = state.get(TYPE);
        if (type == 0) {
            if (itemStack.isOf(Items.GUNPOWDER)) {
                world.setBlockState(pos, state.with(TYPE, 1));
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                }
                if (!world.isClient) {
                    world.syncWorldEvent(9412934, pos, 1);
                }
                return ActionResult.success(world.isClient);
            } else if (itemStack.isOf(Items.FIREWORK_STAR)) {
                world.setBlockState(pos, state.with(TYPE, 2));
                if (!player.isCreative()) {
                    itemStack.decrement(1);
                }
                if (!world.isClient) {
                    world.syncWorldEvent(9412934, pos, 2);
                }
                return ActionResult.success(world.isClient);
            }
        } else {
            if (!world.isClient) {
                world.syncWorldEvent(9412934, pos, type + 2);

                Vec3d center = Vec3d.ofCenter(pos);

                if (type == 1) {
                    world.createExplosion(null, center.getX(), center.getY(), center.getZ(), 1.5f, World.ExplosionSourceType.NONE);
                }
            }
        }


        this.powerOn(state, world, pos);
        this.playClickSound(player, world, pos, true);
        world.emitGameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
        return ActionResult.success(world.isClient);
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return powered ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    public void dropItemStack(World world, BlockPos pos, Direction direction, ItemStack stack) {
        int i = direction.getOffsetX();
        int j = direction.getOffsetY();
        int k = direction.getOffsetZ();
        float f = EntityType.ITEM.getWidth() / 2.0f;
        float g = EntityType.ITEM.getHeight() / 2.0f;
        double d = (double) ((float) pos.getX() + 0.5f) + (i == 0 ? MathHelper.nextDouble(world.random, -0.05, 0.05) : (double) ((float) i * (0.7f + f)));
        double e = (double) ((float) pos.getY() + 0.5f) + (j == 0 ? MathHelper.nextDouble(world.random, -0.05, 0.05) : (double) ((float) j * (0.7f + g))) - (double) g;
        double h = (double) ((float) pos.getZ() + 0.5f) + (k == 0 ? MathHelper.nextDouble(world.random, -0.05, 0.05) : (double) ((float) k * (0.7f + f)));
        double l = i == 0 ? MathHelper.nextDouble(world.random, -0.03, 0.03) : (double) i * 0.03;
        double m = j == 0 ? MathHelper.nextDouble(world.random, 0.03, 0.03) : (double) j * 0.03 + 0.03;
        double n = k == 0 ? MathHelper.nextDouble(world.random, -0.03, 0.03) : (double) k * 0.03;
        dropStack(world, () -> new ItemEntity(world, d, e, h, stack, l, m, n), stack);
    }

    private static void dropStack(World world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack) {
        if (world.isClient || stack.isEmpty() || !world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            return;
        }
        ItemEntity itemEntity = itemEntitySupplier.get();
        itemEntity.setToDefaultPickupDelay();
        world.spawnEntity(itemEntity);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE);

        super.appendProperties(builder);
    }
}