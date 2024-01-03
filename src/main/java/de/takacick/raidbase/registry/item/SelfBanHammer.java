package de.takacick.raidbase.registry.item;

import de.takacick.raidbase.access.LivingProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SelfBanHammer extends Item {

    public SelfBanHammer(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if(attacker instanceof LivingProperties livingProperties) {
            livingProperties.setGettingBanned(151);
        }

        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§49999 Ban Power"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
