package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.access.PlayerProperties;
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

public class MeteorShower extends Item {

    public MeteorShower(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            world.playSoundFromEntity(null, user, SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, SoundCategory.AMBIENT, 1f, 1f);
            ((PlayerProperties) user).setMeteorTicks(100);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.damage(1, user, playerEntity -> {});
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7What nice §cdeadly weather §7we're"));
        tooltip.add(Text.of("§7having!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
