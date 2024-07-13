package de.takacick.onegirlboyblock.registry.item;

import de.takacick.onegirlboyblock.registry.EntityRegistry;
import de.takacick.onegirlboyblock.registry.entity.living.TurboBoardEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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

import java.util.List;
import java.util.Optional;

public class TurboBoard extends Item {

    public TurboBoard(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.SUCCESS;
        }
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockState blockState = world.getBlockState(blockPos);

        BlockPos blockPos2 = blockState.getCollisionShape(world, blockPos).isEmpty() ? blockPos : blockPos.offset(direction);

        Vec3d pos = Vec3d.ofBottomCenter(blockPos2);
        var optional = Optional.ofNullable(EntityRegistry.TURBO_BOARD.create(world));

        if (optional.isEmpty()) {
            return ActionResult.CONSUME;
        }

        float yaw = context.getPlayerYaw() - 180f;

        TurboBoardEntity entity = optional.get();
        entity.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), yaw, 0.0f);
        entity.bodyYaw = yaw;
        entity.prevBodyYaw = yaw;
        entity.headYaw = yaw;
        entity.prevHeadYaw = yaw;
        entity.itemDamage = itemStack.getDamage();
        serverWorld.spawnEntityAndPassengers(entity);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.AMBIENT, 1f, 1f);

        itemStack.decrement(1);
        world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);

        return ActionResult.CONSUME;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);

        if (((HitResult) hitResult).getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        }

        if (!(world instanceof ServerWorld serverWorld)) {
            return TypedActionResult.success(itemStack);
        }

        Vec3d pos = Vec3d.ofBottomCenter(hitResult.getBlockPos()).add(0, 1.001f, 0);
        var optional = Optional.ofNullable(EntityRegistry.TURBO_BOARD.create(world));

        if (optional.isEmpty()) {
            return TypedActionResult.pass(itemStack);
        }
        float yaw = user.getYaw() - 180f;

        TurboBoardEntity entity = optional.get();
        entity.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), MathHelper.wrapDegrees(world.random.nextFloat() * 360.0f), 0.0f);
        entity.bodyYaw = yaw;
        entity.prevBodyYaw = yaw;
        entity.headYaw = yaw;
        entity.prevHeadYaw = yaw;
        entity.itemDamage = itemStack.getDamage();
        serverWorld.spawnEntityAndPassengers(entity);

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.AMBIENT, 1f, 1f);

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        world.emitGameEvent(user, GameEvent.ENTITY_PLACE, entity.getPos());
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Make the §bsky §ryour §eskate park§r!").withColor(0x0066FF));

        super.appendTooltip(stack, context, tooltip, type);
    }
}
