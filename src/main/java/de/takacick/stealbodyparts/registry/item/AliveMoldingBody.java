package de.takacick.stealbodyparts.registry.item;

import de.takacick.stealbodyparts.registry.EntityRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.command.argument.EntityAnchorArgumentType;
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
import net.minecraft.sound.SoundEvents;
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

import java.util.List;

public class AliveMoldingBody extends Item {

    public AliveMoldingBody(Settings settings) {
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

        EntityType<?> entityType = EntityRegistry.ALIVE_MOLDING_BODY;
        Entity entity = spawnFromItemStack(entityType, (ServerWorld) world, itemStack, user, new Vec3d(hitResult.getPos().x, hitResult.getPos().y + 0.01, hitResult.getPos().z), SpawnReason.SPAWN_EGG, false, false);
        if (entity == null) {
            return TypedActionResult.pass(itemStack);
        }

        world.playSound(null, entity.getX(), entity.getBodyY(0.5), entity.getZ(), SoundEvents.BLOCK_MUD_PLACE, entity.getSoundCategory(), 1f, 1f);

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eStick body parts together to create"));
        tooltip.add(Text.of("§ethe ultimate cursed monster..."));
        tooltip.add(Text.of("§c§oWarning: Dangerous"));

        super.appendTooltip(stack, world, tooltip, context);
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
            if (player != null) {
                entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, player.getEyePos());
            }
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
        if (entity instanceof MobEntity mobEntity) {
            mobEntity.headYaw = mobEntity.getYaw();

            if (player != null) {
                mobEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, player.getEyePos());
            }

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
