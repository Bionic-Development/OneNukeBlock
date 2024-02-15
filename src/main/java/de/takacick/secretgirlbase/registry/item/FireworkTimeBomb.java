package de.takacick.secretgirlbase.registry.item;

import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.entity.custom.FireworkTimeBombEntity;
import net.minecraft.entity.Entity;
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

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class FireworkTimeBomb extends Item {

    private static final Predicate<Entity> COLLIDER = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::isCollidable);

    public FireworkTimeBomb(Settings settings) {
        super(settings);
    }

    @Override
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
                Iterator var11 = list.iterator();

                while (var11.hasNext()) {
                    Entity entity = (Entity) var11.next();
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
                    FireworkTimeBombEntity fireworkTimeBombEntity = EntityRegistry.FIREWORK_TIME_BOMB.create(world);
                    fireworkTimeBombEntity.refreshPositionAndAngles(center.getX(), center.getY() + 0.0001, center.getZ(), 0f, 0f);
                    world.spawnEntity(fireworkTimeBombEntity);
                    world.playSound(null, fireworkTimeBombEntity.getX(), fireworkTimeBombEntity.getY(), fireworkTimeBombEntity.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1f,1f);
                    world.emitGameEvent(user, GameEvent.ENTITY_PLACE, BlockPos.ofFloored(hitResult.getPos()));
                    if (!user.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                }

                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack, world.isClient());
            } else {
                return TypedActionResult.pass(itemStack);
            }
        }
    }
}
