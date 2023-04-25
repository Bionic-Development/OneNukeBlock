package de.takacick.everythinghearts.registry.item;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class HeartItem extends Item {

    public HeartItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            BionicUtils.sendEntityStatus((ServerWorld) world, user, EverythingHearts.IDENTIFIER, 3);
            for (int i = 0; i < 1; i++) {
                user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                        .addPersistentModifier(new EntityAttributeModifier(UUID.randomUUID(), "heart", 2.0f, EntityAttributeModifier.Operation.ADDITION));
                user.setHealth(user.getHealth() + 2);
            }
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}
