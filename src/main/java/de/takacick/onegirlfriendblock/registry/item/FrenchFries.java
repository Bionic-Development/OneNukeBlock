package de.takacick.onegirlfriendblock.registry.item;

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

public class FrenchFries extends Item {

    public FrenchFries(Settings settings) {
        super(settings.food(
                        new FoodComponent.Builder().hunger(6).saturationModifier(1.2f)
                                .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 100, 0), 1.0f)
                                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0), 1.0f)
                                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1), 1.0f)
                                .alwaysEdible()
                                .build()
                )
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§ePerfectly salted!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
