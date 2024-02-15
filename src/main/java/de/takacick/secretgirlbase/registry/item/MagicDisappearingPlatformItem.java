package de.takacick.secretgirlbase.registry.item;

import de.takacick.secretgirlbase.registry.block.MagicDisappearingPlatformBlock;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class MagicDisappearingPlatformItem extends BlockItem {
    public MagicDisappearingPlatformItem(Block block, Settings settings) {
        super(block, settings);
    }

    public ActionResult place(ItemPlacementContext context) {
        if (!context.canPlace()) {
            return ActionResult.FAIL;
        }
        ItemPlacementContext itemPlacementContext = this.getPlacementContext(context);
        if (itemPlacementContext == null) {
            return ActionResult.FAIL;
        }
        BlockState blockState = this.getPlacementState(itemPlacementContext);
        if (blockState == null) {
            return ActionResult.FAIL;
        }
        BlockPos blockPos = itemPlacementContext.getBlockPos();
        World world = itemPlacementContext.getWorld();

        Direction direction = blockState.get(MagicDisappearingPlatformBlock.PLACE);
        BlockPos pos  = blockPos;
        if(direction.getOffsetY() == 0) {
            pos = pos.add(direction.getVector());
        }

        List<BlockPos> positions = getPositions(pos);

        for (BlockPos position : positions) {
            if (!world.getEntityCollisions(null, new Box(position)).isEmpty()) {
                return ActionResult.FAIL;
            }

            if (!world.getBlockState(position).isReplaceable()) {
                return ActionResult.FAIL;
            }
        }

        if (!this.place(itemPlacementContext, blockState)) {
            return ActionResult.FAIL;
        }

        PlayerEntity playerEntity = itemPlacementContext.getPlayer();
        ItemStack itemStack = itemPlacementContext.getStack();
        BlockState blockState2 = world.getBlockState(blockPos);
        if (blockState2.isOf(blockState.getBlock())) {
            blockState2 = this.placeFromNbt(blockPos, world, itemStack, blockState2);
            this.postPlacement(blockPos, world, playerEntity, itemStack, blockState2);
            blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
            }
        }
        BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
        world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0f) / 2.0f, blockSoundGroup.getPitch() * 0.8f);
        world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(playerEntity, blockState2));
        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return ActionResult.success(world.isClient);
    }

    private BlockState placeFromNbt(BlockPos pos, World world, ItemStack stack, BlockState state) {
        BlockState blockState = state;
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null) {
            NbtCompound nbtCompound2 = nbtCompound.getCompound(BLOCK_STATE_TAG_KEY);
            StateManager<Block, BlockState> stateManager = blockState.getBlock().getStateManager();
            for (String string : nbtCompound2.getKeys()) {
                Property<?> property = stateManager.getProperty(string);
                if (property == null) continue;
                String string2 = nbtCompound2.get(string).asString();
                blockState = with(blockState, property, string2);
            }
        }
        if (blockState != state) {
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
        }
        return blockState;
    }

    private static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
        return property.parse(name).map(value -> state.with(property, value)).orElse(state);
    }

    public List<BlockPos> getPositions(BlockPos center) {

        int range = 3;
        List<BlockPos> positions = new ArrayList<>();
        for (int x = -1; x < range - 1; x++) {
            for (int z = -1; z < range - 1; z++) {
                BlockPos pos = center.add(x, 0, z);
                positions.add(pos);
            }
        }

        return positions;
    }
}
