package de.takacick.elementalblock.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Worm extends Item {

    public Worm(Settings settings) {
        super(settings.food(new FoodComponent.Builder().hunger(1).saturationModifier(0.3F)
                .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 60, 0), 1F)
                .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, 0), 1F)
                .meat().snack().alwaysEdible().build()));
    }


    @Override
    public int getMaxUseTime(ItemStack stack) {
        return super.getMaxUseTime(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§ewiggle wiggle wiggle"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
