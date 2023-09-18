package de.takacick.onesuperblock.registry.item;

import de.takacick.onesuperblock.registry.entity.projectiles.SuperBridgeEggEntity;
import de.takacick.superitems.registry.item.SuperItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SuperBridgeEgg extends Item implements SuperItem {

    public SuperBridgeEgg(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            SuperBridgeEggEntity superBridgeEggEntity = new SuperBridgeEggEntity(world, user);
            superBridgeEggEntity.setItem(itemStack);
            superBridgeEggEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.9f, 1.0f);
            world.spawnEntity(superBridgeEggEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eLeaves a Super Trail of rainbow"));
        tooltip.add(Text.of("§eblocks!"));
        tooltip.add(Text.of("§9§oBetter in Bedwards?"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
