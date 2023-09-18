package de.takacick.onesuperblock.registry.item;

import de.takacick.onesuperblock.OneSuperBlock;
import de.takacick.onesuperblock.registry.EntityRegistry;
import de.takacick.onesuperblock.registry.entity.living.SuperProtoEntity;
import de.takacick.superitems.registry.item.SuperItem;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SuperProto extends Item implements SuperItem {

    public SuperProto(Settings settings) {
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
        EntityType<?> entityType = EntityRegistry.SUPER_PROTO;
        SuperProtoEntity protoEntity = (SuperProtoEntity) entityType.spawnFromItemStack((ServerWorld) world, itemStack, context.getPlayer(), (BlockPos) blockEntity, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, blockEntity) && direction == Direction.UP);
        if (protoEntity != null) {

            BionicUtils.sendEntityStatus((ServerWorld) world, protoEntity, OneSuperBlock.IDENTIFIER, 1);

            if (playerEntity != null) {
                protoEntity.setOwner(playerEntity);
                protoEntity.setTamed(true);
                itemStack.decrement(1);
            }
            world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);

        if (((HitResult) hitResult).getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        }
        if (!(world instanceof ServerWorld)) {
            return TypedActionResult.success(itemStack);
        }

        EntityType<SuperProtoEntity> entityType = EntityRegistry.SUPER_PROTO;
        Entity entity = spawnFromItemStack(entityType, (ServerWorld) world, itemStack, user, new Vec3d(hitResult.getPos().x, hitResult.getPos().y + 0.01, hitResult.getPos().z), SpawnReason.SPAWN_EGG, false, false);
        if (entity == null) {
            return TypedActionResult.pass(itemStack);
        }

        if (entity instanceof SuperProtoEntity protoEntity) {
            protoEntity.setOwner(user);
            protoEntity.setTamed(true);
        }
        BionicUtils.sendEntityStatus((ServerWorld) world, entity, OneSuperBlock.IDENTIFIER, 1);

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
        return TypedActionResult.consume(itemStack);
    }

    @Nullable
    public Entity spawnFromItemStack(EntityType entityType, ServerWorld world, @Nullable ItemStack stack, @Nullable PlayerEntity player, Vec3d pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        return this.spawn(entityType, world, stack == null ? null : stack.getSubNbt("entityData"), stack != null && stack.hasCustomName() ? stack.getName() : null, player, pos, spawnReason, alignPosition, invertY);
    }

    @Nullable
    public Entity spawn(EntityType entityType, ServerWorld world, @Nullable NbtCompound itemNbt, @Nullable Text name, @Nullable PlayerEntity player, Vec3d pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        Entity entity = this.create(entityType, world, itemNbt, name, player, pos, spawnReason, alignPosition, invertY);
        if (itemNbt != null && entity instanceof LivingEntity livingEntity) {
            livingEntity.readCustomDataFromNbt(itemNbt);
        }

        if (entity != null) {
            world.spawnEntityAndPassengers(entity);
        }
        return entity;
    }

    @Nullable
    public Entity create(EntityType entityType, ServerWorld world, @Nullable NbtCompound itemNbt, @Nullable Text name, @Nullable PlayerEntity player, Vec3d pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        double d;
        Entity entity = entityType.create(world);
        if (entity == null) {
            return null;
        }

        entity.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), MathHelper.wrapDegrees(world.random.nextFloat() * 360.0f), 0.0f);
        if (entity instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) entity;
            mobEntity.headYaw = mobEntity.getYaw();
            mobEntity.bodyYaw = mobEntity.getYaw();
            mobEntity.initialize(world, world.getLocalDifficulty(mobEntity.getBlockPos()), spawnReason, null, itemNbt);
            mobEntity.playAmbientSound();
        }
        if (name != null && entity instanceof LivingEntity) {
            entity.setCustomName(name);
        }
        EntityType.loadFromEntityNbt(world, player, entity, itemNbt);
        return entity;
    }
}