package de.takacick.onedeathblock.registry.block;

import de.takacick.onedeathblock.access.PlayerProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class ElectricutionerDoor extends DoorBlock {

    public ElectricutionerDoor(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        state = state.cycle(OPEN);
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
        world.syncWorldEvent(player, state.get(OPEN) != false ? this.getOpenSoundEventId() : this.getCloseSoundEventId(), pos, 0);
        world.emitGameEvent(player, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

        if (player instanceof PlayerProperties playerProperties) {
            world.playSoundFromEntity(null, player, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 0.25f, 1.25f);
            playerProperties.setShockedTicks(80);
        }

        return ActionResult.success(world.isClient);
    }

    private int getCloseSoundEventId() {
        return this.material == Material.METAL ? WorldEvents.IRON_DOOR_CLOSES : WorldEvents.WOODEN_DOOR_CLOSES;
    }

    private int getOpenSoundEventId() {
        return this.material == Material.METAL ? WorldEvents.IRON_DOOR_OPENS : WorldEvents.WOODEN_DOOR_OPENS;
    }


}
