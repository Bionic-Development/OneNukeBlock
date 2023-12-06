package de.takacick.emeraldmoney.registry.item;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.UtilsProperties;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class EmeraldHealthJar extends Item {
    public EmeraldHealthJar(Settings settings) {
        super(settings.food(new FoodComponent.Builder().alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof UtilsProperties utilsProperties) {
            for (int i = 0; i < 10; i++) {
                user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                        .addPersistentModifier(new EntityAttributeModifier(UUID.randomUUID(), "custom.heart", 2.0f, EntityAttributeModifier.Operation.ADDITION));
                user.setHealth(user.getHealth() + 2);
                if (user.getMaxHealth() - (int) (user.getMaxHealth()) >= 0.5) {
                    utilsProperties.addHeart((int) ((Math.ceil(user.getMaxHealth()) / 2f)) - 1, ItemRegistry.EMERALD_HEART);
                } else {
                    utilsProperties.addHeart((int) ((Math.floor(user.getMaxHealth()) / 2f)) - 1, ItemRegistry.EMERALD_HEART);
                }
            }
            BionicUtils.sendEntityStatus((ServerWorld) world, user, EmeraldMoney.IDENTIFIER, 7);
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Crack this open to obtain the §aEmerald"));
        tooltip.add(Text.of("§aHearts§9!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
