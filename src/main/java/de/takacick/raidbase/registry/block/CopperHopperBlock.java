package de.takacick.raidbase.registry.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import de.takacick.raidbase.registry.EntityRegistry;
import de.takacick.raidbase.registry.ItemRegistry;
import de.takacick.raidbase.registry.block.entity.CopperHopperBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CopperHopperBlock extends HopperBlock implements Oxidizable {

    public static final Supplier<BiMap<Block, Block>> OXIDATION_LEVEL_INCREASES = Suppliers.memoize(() -> ((ImmutableBiMap.Builder) ImmutableBiMap.builder()
            .put(ItemRegistry.COPPER_HOPPER, ItemRegistry.EXPOSED_COPPER_HOPPER))
            .put(ItemRegistry.EXPOSED_COPPER_HOPPER, ItemRegistry.WEATHERED_COPPER_HOPPER)
            .put(ItemRegistry.WEATHERED_COPPER_HOPPER, ItemRegistry.OXIDIZED_COPPER_HOPPER)
            .build());
    public static final Supplier<BiMap<Block, Block>> OXIDATION_LEVEL_DECREASES = Suppliers.memoize(() -> OXIDATION_LEVEL_INCREASES.get().inverse());

    public static final Supplier<BiMap<Block, Block>> UNWAXED_TO_WAXED_BLOCKS = Suppliers.memoize(() -> ((ImmutableBiMap.Builder) ImmutableBiMap.builder()
            .put(ItemRegistry.COPPER_HOPPER, ItemRegistry.WAXED_COPPER_HOPPER))
            .put(ItemRegistry.EXPOSED_COPPER_HOPPER, ItemRegistry.WAXED_EXPOSED_COPPER_HOPPER)
            .put(ItemRegistry.WEATHERED_COPPER_HOPPER, ItemRegistry.WAXED_WEATHERED_COPPER_HOPPER)
            .put(ItemRegistry.OXIDIZED_COPPER_HOPPER, ItemRegistry.WAXED_OXIDIZED_COPPER_HOPPER)
            .build());
    public static final Supplier<BiMap<Block, Block>> WAXED_TO_UNWAXED_BLOCKS = Suppliers.memoize(() -> UNWAXED_TO_WAXED_BLOCKS.get().inverse());

    private final Oxidizable.OxidationLevel oxidationLevel;

    public CopperHopperBlock(Oxidizable.OxidationLevel oxidationLevel, Settings settings) {
        super(settings);
        this.oxidationLevel = oxidationLevel;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CopperHopperBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : CopperHopperBlock.checkType(type, EntityRegistry.COPPER_HOPPER, CopperHopperBlockEntity::serverTick);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.tickDegradation(state, world, pos, random);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }

    @Override
    public Oxidizable.OxidationLevel getDegradationLevel() {
        return this.oxidationLevel;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName() && world.getBlockEntity(pos) instanceof CopperHopperBlockEntity copperHopperBlockEntity) {
            copperHopperBlockEntity.setCustomName(itemStack.getName());
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof CopperHopperBlockEntity copperHopperBlockEntity) {
            player.openHandledScreen(copperHopperBlockEntity);
            player.incrementStat(Stats.INSPECT_HOPPER);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (newState.getBlock() instanceof CopperHopperBlock) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperHopperBlockEntity copperHopperBlockEntity) {
            ItemScatterer.spawn(world, pos, copperHopperBlockEntity);
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CopperHopperBlockEntity copperHopperBlockEntity) {
            CopperHopperBlockEntity.onEntityCollided(world, pos, state, entity, copperHopperBlockEntity);
        }
    }

}
