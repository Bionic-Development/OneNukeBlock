package de.takacick.onegirlfriendblock.registry.item;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HeartItem extends Item {

    public HeartItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            BionicUtils.sendEntityStatus(world, user, OneGirlfriendBlock.IDENTIFIER, 1);

            user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                    .setBaseValue(user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue() + 2);
            user.setHealth(user.getHealth() + 2);
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
