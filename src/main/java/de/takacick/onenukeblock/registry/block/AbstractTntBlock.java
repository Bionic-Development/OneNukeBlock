package de.takacick.onenukeblock.registry.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTntBlock extends BlockWithEntity {

    public static final BooleanProperty UNSTABLE = Properties.UNSTABLE;

    public AbstractTntBlock(Settings settings) {
        super(settings.nonOpaque()
                .solidBlock((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
        );
        this.setDefaultState(this.getDefaultState().with(UNSTABLE, false));
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        if (world.isReceivingRedstonePower(pos)) {
            primeTnt(world, pos);
            world.removeBlock(pos, false);
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            primeTnt(world, pos);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient() && !player.isCreative() && state.get(UNSTABLE).booleanValue()) {
            primeTnt(world, pos);
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (world.isClient) {
            return;
        }
        TntEntity tntEntity = new TntEntity(world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, explosion.getCausingEntity());
        int i = tntEntity.getFuse();
        tntEntity.setFuse((short) (world.random.nextInt(i / 4) + i / 8));
        world.spawnEntity(tntEntity);
    }

    public void primeTnt(World world, BlockPos pos) {
        primeTnt(world, pos, null);
    }

    public abstract void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter);

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE)) {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        }
        primeTnt(world, pos, player);
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
        Item item = stack.getItem();
        if (stack.isOf(Items.FLINT_AND_STEEL)) {
            stack.damage(1, player, LivingEntity.getSlotForHand(hand));
        } else {
            stack.decrementUnlessCreative(1, player);
        }
        player.incrementStat(Stats.USED.getOrCreateStat(item));
        return ItemActionResult.success(world.isClient);
    }

    @Override
    protected void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            BlockPos blockPos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.canModifyAt(world, blockPos)) {
                primeTnt(world, blockPos, entity instanceof LivingEntity ? (LivingEntity) entity : null);
                world.removeBlock(blockPos, false);
            }
        }
    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UNSTABLE);
    }
}
