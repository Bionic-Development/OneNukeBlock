package de.takacick.immortalmobs.registry.item;

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

public class ImmortalPorkchop extends Item implements ImmortalItem {

    public ImmortalPorkchop(Settings settings) {
        super(settings.food(new FoodComponent.Builder().hunger(8)
                .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 600), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600), 1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600), 1.0f)
                .saturationModifier(0.8f).alwaysEdible().meat().build()));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eEternally filling..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
