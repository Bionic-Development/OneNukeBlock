package de.takacick.imagineanything.registry.item;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.living.AlfredThePickaxeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;

public class ImaginedAlfredThePickaxe extends Item {

    public ImaginedAlfredThePickaxe(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity user = context.getPlayer();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            BlockState blockState = world.getBlockState(blockPos);

            BlockPos blockPos3;
            if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
                blockPos3 = blockPos;
            } else {
                blockPos3 = blockPos.offset(direction);
            }

            EntityType<AlfredThePickaxeEntity> entityType2 = EntityRegistry.ALFRED_THE_PICKAXE;
            AlfredThePickaxeEntity entity = (AlfredThePickaxeEntity) entityType2.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), blockPos3, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos3) && direction == Direction.UP);

            if (entity != null) {
                entity.setOwner(user);
                entity.setTamed(true);
                world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            }

            if (!user.getAbilities().creativeMode) {
                itemStack.damage(1, user, e -> e.sendToolBreakStatus(context.getHand()));
            }

            return ActionResult.CONSUME;
        }
    }
}

