package de.takacick.elementalblock.registry.item;

import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.utils.UtilsProperties;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.UUID;

public class DirtHeart extends Item {

    public DirtHeart(Settings settings) {
        super(settings.food(new FoodComponent.Builder().alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof UtilsProperties utilsProperties) {
            for (int i = 0; i < 1; i++) {
                user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                        .addPersistentModifier(new EntityAttributeModifier(UUID.randomUUID(), "custom.heart", 2.0f, EntityAttributeModifier.Operation.ADDITION));
                user.setHealth(user.getHealth() + 2);
                if (user.getMaxHealth() - (int) (user.getMaxHealth()) >= 0.5) {
                    utilsProperties.addHeart((int) ((Math.ceil(user.getMaxHealth()) / 2f)) - 1, ItemRegistry.DIRT_HEART);
                } else {
                    utilsProperties.addHeart((int) ((Math.floor(user.getMaxHealth()) / 2f)) - 1, ItemRegistry.DIRT_HEART);
                }
            }
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.BLOCK_ROOTED_DIRT_BREAK;
    }
}
