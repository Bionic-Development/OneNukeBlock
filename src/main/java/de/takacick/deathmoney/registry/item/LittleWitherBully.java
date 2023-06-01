package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.entity.living.LittleWitherBullyEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Objects;

public class LittleWitherBully extends Item {

    public LittleWitherBully(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Object blockEntity;
        World world = context.getWorld();
        if (!(world instanceof ServerWorld)) {
            return ActionResult.SUCCESS;
        }
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.SPAWNER) && world.getBlockEntity(blockPos) instanceof MobSpawnerBlockEntity) {
            return ActionResult.FAIL;
        }
        blockEntity = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);
        EntityType<LittleWitherBullyEntity> entityType = EntityRegistry.LITTLE_WITHER_BULLY;
        LittleWitherBullyEntity littleWitherBullyEntity = (LittleWitherBullyEntity) entityType.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), (BlockPos) blockEntity, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, blockEntity) && direction == Direction.UP);
        if (littleWitherBullyEntity != null) {
            if (playerEntity != null) {
                littleWitherBullyEntity.playSpawnEffects();
                littleWitherBullyEntity.getLookControl().lookAt(playerEntity, 100, 100);
                littleWitherBullyEntity.setOwner(playerEntity);
                itemStack.decrement(1);
            }
            world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        Vec3d vec3d = user.getRotationVector().multiply(2, 1, 2).add(user.getX(), user.getEyeY(), user.getZ());
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            EntityType<LittleWitherBullyEntity> entityType = EntityRegistry.LITTLE_WITHER_BULLY;
            LittleWitherBullyEntity littleWitherBullyEntity = (LittleWitherBullyEntity) entityType.spawnFromItemStack((ServerWorld) world, itemStack, user, new BlockPos(vec3d), SpawnReason.SPAWN_EGG, true, false);
            if (littleWitherBullyEntity != null) {
                littleWitherBullyEntity.playSpawnEffects();
                littleWitherBullyEntity.setOwner(user);
                itemStack.decrement(1);

                littleWitherBullyEntity.getLookControl().lookAt(user, 100, 100);
            }
        }

        return super.use(world, user, hand);
    }
}

