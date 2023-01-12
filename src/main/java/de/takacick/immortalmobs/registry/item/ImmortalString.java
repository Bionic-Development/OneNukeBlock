package de.takacick.immortalmobs.registry.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmortalString extends Item implements ImmortalItem {

    public ImmortalString(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if (entity instanceof LivingEntity livingEntity && !world.isClient) {
            if (livingEntity.getMainHandStack().equals(stack) || livingEntity.getOffHandStack().equals(stack)) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 22, 4, false, false, true));
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eFabric of §dImmortality"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
