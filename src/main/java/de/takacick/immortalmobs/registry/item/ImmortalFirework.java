package de.takacick.immortalmobs.registry.item;

import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalFireworkEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmortalFirework extends Item implements ImmortalItem {

    public ImmortalFirework(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient) {
            ItemStack itemStack = context.getStack();
            Vec3d vec3d = context.getHitPos();
            Direction direction = context.getSide();
            ImmortalFireworkEntity fireworkRocketEntity = new ImmortalFireworkEntity(world, context.getPlayer(), vec3d.x + (double) direction.getOffsetX() * 0.15, vec3d.y + (double) direction.getOffsetY() * 0.15, vec3d.z + (double) direction.getOffsetZ() * 0.15, itemStack);
            world.spawnEntity(fireworkRocketEntity);
            itemStack.decrement(1);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isFallFlying()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (!world.isClient) {
                ImmortalFireworkEntity fireworkRocketEntity = new ImmortalFireworkEntity(world, itemStack, user);
                world.spawnEntity(fireworkRocketEntity);
                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eFirework that is packing"));
        tooltip.add(Text.of("§eexplosive §dImmortality§e!"));
        tooltip.add(Text.of("§c§oWarning: Dangerous"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
