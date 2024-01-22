package de.takacick.illegalwars.registry.block;

import de.takacick.illegalwars.IllegalWarsClient;
import de.takacick.illegalwars.registry.block.entity.CommandBlockPressurePlateBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class CommandBlockPressurePlateBlock extends PressurePlateBlock implements BlockEntityProvider {

    public CommandBlockPressurePlateBlock(BlockSetType type, Settings settings) {
        super(type, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient) {
            return;
        }

        int i = this.getRedstoneOutput(state);
        if (i == 0) {
            this.updatePlateState(entity, world, pos, state, i);
        }
    }

    private void updatePlateState(@Nullable Entity entity, World world, BlockPos pos, BlockState state, int output) {
        boolean bl2;
        int i = this.getRedstoneOutput(world, pos);
        boolean bl = output > 0;
        boolean bl3 = bl2 = i > 0;
        if (output != i) {
            BlockState blockState = this.setRedstoneOutput(state, i);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            this.updateNeighbors(world, pos);
            world.scheduleBlockRerenderIfNeeded(pos, state, blockState);
        }
        if (!bl2 && bl) {
            world.playSound(null, pos, this.blockSetType.pressurePlateClickOff(), SoundCategory.BLOCKS);
            world.emitGameEvent(entity, GameEvent.BLOCK_DEACTIVATE, pos);
        } else if (bl2 && !bl) {

            if (!world.isClient && world.getBlockEntity(pos) instanceof CommandBlockPressurePlateBlockEntity commandBlockPressurePlateBlockEntity) {
                execute(commandBlockPressurePlateBlockEntity, world);
            }

            world.playSound(null, pos, this.blockSetType.pressurePlateClickOn(), SoundCategory.BLOCKS);
            world.emitGameEvent(entity, GameEvent.BLOCK_ACTIVATE, pos);
        }
        if (bl2) {
            world.scheduleBlockTick(new BlockPos(pos), this, this.getTickRate());
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CommandBlockPressurePlateBlockEntity(pos, state);
    }

    private void execute(CommandBlockPressurePlateBlockEntity commandBlockPressurePlateBlockEntity, World world) {
        CommandBlockExecutor executor = commandBlockPressurePlateBlockEntity.getCommandExecutor();
        boolean hasCommand = !StringHelper.isEmpty(executor.getCommand());
        if (hasCommand) {
            executor.execute(world);
        } else {
            executor.setSuccessCount(0);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof CommandBlockPressurePlateBlockEntity commandBlockEntity && player.isCreativeLevelTwoOp()) {
            if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.networkHandler.sendPacket(BlockEntityUpdateS2CPacket.create(commandBlockEntity, BlockEntity::createNbt));
            } else if (world.isClient) {
                IllegalWarsClient.openCommandBlockScreen(commandBlockEntity);
            }
            return ActionResult.success(world.isClient);
        }
        return ActionResult.PASS;
    }

}
