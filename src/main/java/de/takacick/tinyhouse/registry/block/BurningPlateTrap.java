package de.takacick.tinyhouse.registry.block;

import de.takacick.tinyhouse.access.LivingProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BurningPlateTrap extends WeightedPressurePlateBlock {
    private static final int MAX_CHARGE = 15;
    public static final IntProperty CHARGE = IntProperty.of("charge", 0, MAX_CHARGE);

    private final BlockSetType blockSetType;

    public BurningPlateTrap(Settings settings, BlockSetType blockSetType) {
        super(1, settings, blockSetType);
        this.blockSetType = blockSetType;
        this.setDefaultState(getDefaultState().with(CHARGE, MAX_CHARGE));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || !state.isOf(newState.getBlock())) {
            return;
        }

        if (this.getRedstoneOutput(newState) > 1 && this.getCharge(newState) > 0) {

            world.getEntitiesByClass(Entity.class, BOX.offset(pos), EntityPredicates.EXCEPT_SPECTATOR.and(entity -> !entity.canAvoidTraps())).forEach(entity -> {
                entity.setFrozenTicks(-1);
                entity.damage(world.getDamageSources().inFire(), 6);
                entity.setOnFireFor(5);
                if (entity instanceof LivingProperties livingProperties && entity.getFireTicks() > 0) {
                    livingProperties.setFrozenBody(0);
                    livingProperties.setBurning(true);
                    entity.setOnFireFor(5);

                    entity.setVelocity(0, 0.4, 0);
                    entity.velocityDirty = true;
                    entity.velocityModified = true;
                }
            });

            world.syncWorldEvent(813923, pos, 0);
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected int getTickRate() {
        return 1;
    }

    protected int getCharge(BlockState state) {
        return state.get(CHARGE);
    }

    @Override
    protected int getRedstoneOutput(BlockState state) {
        int charge = getCharge(state);
        return Math.max((charge < MAX_CHARGE) ? 1 : 0, super.getRedstoneOutput(state));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGE);

        super.appendProperties(builder);
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
        int prevCharge = getCharge(state);
        int charge = prevCharge;
        int i = output == 1 && (charge < MAX_CHARGE) ? 1 : Math.max((charge < MAX_CHARGE) ? 1 : 0, output > 1 && charge > 0 ? MAX_CHARGE : this.getRedstoneOutput(world, pos));
        boolean bl = output > 0;
        boolean bl3 = bl2 = i > 0;

        if (i > 1) {
            charge = MathHelper.clamp(charge - 1, 0, MAX_CHARGE);
        } else {
            charge = MathHelper.clamp(charge + 1, 0, MAX_CHARGE);
        }

        if (output != i || charge != prevCharge) {
            BlockState blockState = this.setRedstoneOutput(state, i);
            world.setBlockState(pos, blockState.with(CHARGE, charge), Block.NOTIFY_LISTENERS);
            this.updateNeighbors(world, pos);
            world.scheduleBlockRerenderIfNeeded(pos, state, blockState);
        }

        if (charge == 0 && charge != prevCharge) {
            if (!world.isClient) {
                world.syncWorldEvent(813923, pos, 1);
            }
        }

        if (!bl2 && bl) {
            world.playSound(null, pos, this.blockSetType.pressurePlateClickOff(), SoundCategory.BLOCKS);
            world.emitGameEvent(entity, GameEvent.BLOCK_DEACTIVATE, pos);
        } else if (bl2 && !bl) {
            world.playSound(null, pos, this.blockSetType.pressurePlateClickOn(), SoundCategory.BLOCKS);
            world.emitGameEvent(entity, GameEvent.BLOCK_ACTIVATE, pos);
        }
        if (charge < MAX_CHARGE && charge > 0 && i == 1) {
            world.syncWorldEvent(813923, pos, 2);
            world.scheduleBlockTick(new BlockPos(pos), this, 3);
        } else if (bl2) {
            world.scheduleBlockTick(new BlockPos(pos), this, this.getTickRate());
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int charge = getCharge(state);
        int i = this.getRedstoneOutput(state);
        if (i > 0 || (charge < MAX_CHARGE && charge > 0)) {
            this.updatePlateState(null, world, pos, state, i);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§cMelts §eanything at a single step!"));

        super.appendTooltip(stack, world, tooltip, options);
    }
}
