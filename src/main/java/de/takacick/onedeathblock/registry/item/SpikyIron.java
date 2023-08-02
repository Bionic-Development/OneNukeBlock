package de.takacick.onedeathblock.registry.item;

import de.takacick.onedeathblock.damage.DeathDamageSources;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpikyIron extends Item implements Wearable {

    public SpikyIron(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if (!world.isClient) {
            if (entity instanceof LivingEntity livingEntity && selected) {
                livingEntity.damage(DeathDamageSources.SPIKY_IRON, 1f);
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eNot safe for kids to handle!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
