package de.takacick.secretcraftbase.registry.item;

import de.takacick.secretcraftbase.SecretCraftBase;
import de.takacick.secretcraftbase.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class IronHeartCarver extends Item {

    public IronHeartCarver(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        PlayerProperties playerProperties = (PlayerProperties) user;

        if (playerProperties.getHeartRemovalTicks() > 0 || hand.equals(Hand.OFF_HAND)) {
            return TypedActionResult.fail(itemStack);
        }
        if (!world.isClient) {
            playerProperties.setHeartRemovalTicks(35);
            BionicUtils.sendEntityStatus(world, user, SecretCraftBase.IDENTIFIER, 1);
            playerProperties.setHeartCarverStack(itemStack);
        }

        return TypedActionResult.success(itemStack, false);
    }
}
