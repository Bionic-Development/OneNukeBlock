package de.takacick.heartmoney.registry.item;

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

public class Heartpple extends Item {

    public Heartpple(Settings settings) {
        super(settings.food(new FoodComponent.Builder().hunger(6)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 0), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 0), 1f)
                .saturationModifier(1.2f).build()));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eIt's a heart, but also an apple!"));
        tooltip.add(Text.of("§9§oBasic Tier"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
