package de.takacick.everythinghearts.registry.item;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartBread extends Item {

    public HeartBread(Settings settings) {
        super(settings.food(new FoodComponent.Builder().hunger(5).saturationModifier(0.6f)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 200, 0), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0), 1f)
                .alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (!world.isClient) {
            BionicUtils.sendEntityStatus((ServerWorld) world, user, EverythingHearts.IDENTIFIER, 4);
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eBread in the shape of a heart!"));
        tooltip.add(Text.of("§eCute and filling..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
