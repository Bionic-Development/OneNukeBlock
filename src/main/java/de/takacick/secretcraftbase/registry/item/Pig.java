package de.takacick.secretcraftbase.registry.item;

import de.takacick.secretcraftbase.server.utils.EntityNbtHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class Pig extends Item {

    public Pig(Settings settings) {
        super(settings);
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

        EntityType<?> entityType = EntityNbtHelper.getEntityType(itemStack);

        Vec3d pos = Vec3d.ofBottomCenter(hitResult.getBlockPos()).add(0, 1.001f, 0);

        Entity entity = spawnFromItemStack(entityType, (ServerWorld) world, itemStack, user, pos, SpawnReason.SPAWN_EGG, false, false);
        if (entity == null) {
            return TypedActionResult.pass(itemStack);
        }

        if (entity instanceof MobEntity mobEntity) {
            mobEntity.playAmbientSound();
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return EntityNbtHelper.getEntityType(stack).getTranslationKey();
    }

    @Nullable
    public Entity spawnFromItemStack(EntityType entityType, ServerWorld world, @Nullable ItemStack stack, @Nullable PlayerEntity player, Vec3d pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        return this.spawn(entityType, world, stack == null ? null : stack.getSubNbt("entityData"), stack != null && stack.hasCustomName() ? stack.getName() : null, player, pos, spawnReason, alignPosition, invertY);
    }

    @Nullable
    public Entity spawn(EntityType entityType, ServerWorld world, @Nullable NbtCompound itemNbt, @Nullable Text name, @Nullable PlayerEntity player, Vec3d pos, SpawnReason spawnReason, boolean alignPosition, boolean invertY) {
        Entity entity = this.create(entityType, world, itemNbt, name, player, pos, spawnReason, alignPosition, invertY);
        if (itemNbt != null && entity instanceof LivingEntity livingEntity) {
            livingEntity.readNbt(itemNbt);
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
