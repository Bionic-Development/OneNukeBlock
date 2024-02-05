package de.takacick.secretcraftbase.registry.item;

import de.takacick.secretcraftbase.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.function.Predicate;

public class SchematicItem extends Item {

    private final EntityType<?> entityType;
    private static final Predicate<Entity> COLLIDER = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::isCollidable);

    public SchematicItem(EntityType<?> entityType, Settings settings) {
        super(settings);
        this.entityType = entityType;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        } else {
            Vec3d vec3d = user.getRotationVec(1.0F);
            double d = 5.0D;
            List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0D)).expand(1.0D), COLLIDER);
            if (!list.isEmpty()) {
                Vec3d vec3d2 = user.getEyePos();
                var var11 = list.iterator();

                while (var11.hasNext()) {
                    Entity entity = var11.next();
                    Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                    if (box.contains(vec3d2)) {
                        return TypedActionResult.pass(itemStack);
                    }
                }
            }

            if (hitResult instanceof BlockHitResult blockHitResult) {
                BlockPos blockPos = blockHitResult.getBlockPos().add(blockHitResult.getSide().getVector());
                Vec3d center = Vec3d.ofBottomCenter(blockPos);

                if (!world.isClient) {
                    Entity entity = this.entityType.create(world);
                    entity.updatePositionAndAngles(center.getX(), center.getY(), center.getZ(), 0f, 0f);
                    world.spawnEntity(entity);

                    world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.PLAYERS, 1f, 1f);
                    if (entityType.equals(EntityRegistry.XP_FARM)) {
                        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);
                    } else if (entityType.equals(EntityRegistry.IRON_GOLEM_FARM)) {
                        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_IRON_GOLEM_REPAIR, SoundCategory.PLAYERS, 0.7f, 1f);
                    } else if (entityType.equals(EntityRegistry.ARMORY_ROOM)) {
                        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.PLAYERS, 0.7f, 1f);
                    } else if (entityType.equals(EntityRegistry.TREASURY_ROOM)) {
                        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_METAL_PLACE, SoundCategory.PLAYERS, 0.7f, 1f);
                    }
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, BlockPos.ofFloored(hitResult.getPos()));
                    if (!user.getAbilities().creativeMode) {
                        itemStack.damage(1, user, playerEntity -> {
                        });
                    }
                }

                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack, world.isClient());
            } else {
                return TypedActionResult.pass(itemStack);
            }
        }
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }
}
