package de.takacick.onenukeblock.registry.item;

import de.takacick.onenukeblock.utils.data.AttachmentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class ExplosiveGummyBear extends Item {

    public ExplosiveGummyBear(Settings settings) {
        super(settings.food(new FoodComponent.Builder().nutrition(4)
                .saturationModifier(0.3f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 100, 0, false, false, true), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0, false, false, true), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0, false, false, true), 1f)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0, false, false, true), 1f)
                .alwaysEdible().snack().build()));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);

        if(user.hasAttached(AttachmentTypes.EXPLOSIVE_GUMMY_BEAR)) {
            return TypedActionResult.fail(itemStack);
        }

        return super.use(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (!world.isClient) {
            user.getAttachedOrCreate(AttachmentTypes.EXPLOSIVE_GUMMY_BEAR);
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        tooltip.add(Text.of("§7This gummy bear §cpacks a punch§7..."));

        super.appendTooltip(stack, context, tooltip, type);
    }
}
