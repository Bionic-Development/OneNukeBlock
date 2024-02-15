package de.takacick.secretgirlbase.registry.item;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.registry.EntityRegistry;
import de.takacick.secretgirlbase.registry.entity.living.ZukoEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
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
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class Zuko extends Item {

    private static final Predicate<Entity> COLLIDER = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::isCollidable);

    public Zuko(Settings settings) {
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
                    ZukoEntity zukoEntity = EntityRegistry.ZUKO.create(world);
                    float yaw = user.getYaw() - 180f;
                    zukoEntity.setYaw(yaw);
                    zukoEntity.setHeadYaw(yaw);
                    zukoEntity.setBodyYaw(yaw);
                    zukoEntity.refreshPositionAndAngles(center.getX(), center.getY() + 0.0001, center.getZ(), yaw, 0);
                    zukoEntity.setOwner(user);
                    zukoEntity.playAmbientSound();

                    world.spawnEntity(zukoEntity);
                    BionicUtils.sendEntityStatus(world, zukoEntity, SecretGirlBase.IDENTIFIER, 1);
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

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("§9§omeow :3"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
