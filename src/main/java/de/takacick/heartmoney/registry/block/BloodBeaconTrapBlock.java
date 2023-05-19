package de.takacick.heartmoney.registry.block;

import de.takacick.heartmoney.registry.EntityRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.block.entity.BloodBeaconTrapBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodBeaconTrapBlock extends BlockWithEntity implements Stainable {

    public BloodBeaconTrapBlock(Settings settings) {
        super(settings);
    }

    public DyeColor getColor() {
        return DyeColor.WHITE;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BloodBeaconTrapBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, EntityRegistry.BLOOD_BEACON_TRAP, BloodBeaconTrapBlockEntity::tick);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BloodBeaconTrapBlockEntity bloodBeaconTrapBlockEntity) {
            bloodBeaconTrapBlockEntity.toggled = !bloodBeaconTrapBlockEntity.toggled;

            if (bloodBeaconTrapBlockEntity.toggled) {
                for (int i = 0; i < 20; i++) {
                    ((ServerWorld) world).spawnParticles(ParticleRegistry.HEART,
                            pos.getX() + 0.5d,
                            pos.getY() + 0.9d,
                            pos.getZ() + 0.5d, 1,
                            0.2,
                            0.2,
                            0.2,
                            0.4);
                }
                
                world.playSound(null, pos.add(0, 1, 0), SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.BLOCKS,
                        3f, 2f);
            }

            BloodBeaconTrapBlockEntity.tick(world, pos, state, bloodBeaconTrapBlockEntity);
            blockEntity.markDirty();
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {

        tooltip.add(Text.of("§eSometimes you need to trap them to"));
        tooltip.add(Text.of("§eshow your love!"));
        tooltip.add(Text.of("§5§oEpic Tier"));

        super.appendTooltip(stack, world, tooltip, options);
    }
}
