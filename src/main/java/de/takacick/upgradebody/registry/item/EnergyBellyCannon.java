package de.takacick.upgradebody.registry.item;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.bodypart.BodyParts;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergyBellyCannon extends Item {

    public EnergyBellyCannon(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()) {
            if (!playerProperties.hasBodyPart(BodyParts.ENERGY_BELLY_CANNON)) {
                if (!world.isClient) {
                    playerProperties.setBodyPart(BodyParts.ENERGY_BELLY_CANNON, true);
                    BionicUtils.sendEntityStatus((ServerWorld) user.getWorld(), user, UpgradeBody.IDENTIFIER, 11);
                }

                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                return TypedActionResult.consume(itemStack);
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Use your hunger to channel a §edeadly"));
        tooltip.add(Text.of("§eenergy blast§9"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
