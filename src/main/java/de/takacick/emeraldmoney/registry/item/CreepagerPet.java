package de.takacick.emeraldmoney.registry.item;

import de.takacick.emeraldmoney.registry.EntityRegistry;
import de.takacick.emeraldmoney.registry.entity.living.CreepagerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class CreepagerPet extends Item {

    public CreepagerPet(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        }
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.SPAWNER) && world.getBlockEntity(blockPos) instanceof MobSpawnerBlockEntity) {
            return ActionResult.FAIL;
        }
        BlockPos blockPos2 = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);

        CreepagerEntity creepagerEntity = new CreepagerEntity(EntityRegistry.CREEPAGER, world);
        creepagerEntity.setYaw(context.getPlayer() == null ? (world.getRandom().nextFloat() * 360f) - 180f :
                context.getPlayer().getYaw() - 180f);
        creepagerEntity.refreshPositionAndAngles(blockPos2.getX() + 0.5, blockPos2.getY() + 0.001, blockPos2.getZ() + 0.5, creepagerEntity.getYaw(), 0f);
        creepagerEntity.setBodyYaw(creepagerEntity.getYaw());
        creepagerEntity.setHeadYaw(creepagerEntity.getYaw());
        if (context.getPlayer() != null) {
            creepagerEntity.setOwner(context.getPlayer());
        }
        world.spawnEntity(creepagerEntity);
        creepagerEntity.playSpawnEffects();
        if (context.getPlayer() == null || !context.getPlayer().getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);

        return ActionResult.CONSUME;
    }
}
