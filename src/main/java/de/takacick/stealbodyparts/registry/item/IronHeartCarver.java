package de.takacick.stealbodyparts.registry.item;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.stealbodyparts.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
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

        if (playerProperties.removedHeart() || hand.equals(Hand.OFF_HAND)) {
            return TypedActionResult.fail(itemStack);
        }
        if (!world.isClient) {
            playerProperties.setRemovedHeart(true);
            playerProperties.setHeartRemovalTicks(30);
            BionicUtils.sendEntityStatus((ServerWorld) world, user, StealBodyParts.IDENTIFIER, 1);
        }

        return TypedActionResult.success(itemStack, false);
    }
}
