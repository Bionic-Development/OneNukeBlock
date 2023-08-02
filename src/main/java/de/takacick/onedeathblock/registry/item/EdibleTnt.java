package de.takacick.onedeathblock.registry.item;

import de.takacick.onedeathblock.access.PlayerProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EdibleTnt extends Item {

    public EdibleTnt(Settings settings) {
        super(settings.food(new FoodComponent.Builder().alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (!world.isClient) {
            if (user instanceof PlayerProperties playerProperties) {
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.PLAYERS, 1f, 1f);
                playerProperties.setTntExplosionTicks(80);
            }
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§emmm §cbubbly §ein stomach :3"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
