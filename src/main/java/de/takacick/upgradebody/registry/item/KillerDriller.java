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

public class KillerDriller extends Item {

    public KillerDriller(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (user instanceof PlayerProperties playerProperties
                && playerProperties.isUpgrading()) {
            if (!playerProperties.hasBodyPart(BodyParts.KILLER_DRILLER)) {
                if (!world.isClient) {
                    playerProperties.setBodyPart(BodyParts.KILLER_DRILLER, true);
                    BionicUtils.sendEntityStatus((ServerWorld) user.getWorld(), user, UpgradeBody.IDENTIFIER, 12);
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

        tooltip.add(Text.of("§eLET IT RIP§9!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
