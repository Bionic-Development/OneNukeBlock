package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.access.PlayerProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExplosiveCakePop extends Item {

    public ExplosiveCakePop(Settings settings) {
        super(settings.food(new FoodComponent.Builder().hunger(4).saturationModifier(1.2f)
                .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 80, 4), 1.0f)
                .alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if(!world.isClient) {
            if (user instanceof PlayerProperties playerProperties) {
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.PLAYERS, 1f,1f);
                playerProperties.setCakeExplosionTicks(80);
            }
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Mmm what an §cexplosive §7sweet taste :3"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
