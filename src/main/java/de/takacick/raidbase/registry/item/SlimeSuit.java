package de.takacick.raidbase.registry.item;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlimeSuit extends Item {

    public SlimeSuit(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack itemStack = user.getStackInHand(hand);
        if (user instanceof PlayerProperties playerProperties
                && !playerProperties.hasSlimeSuit()) {
            if (!world.isClient) {
                BionicUtils.sendEntityStatus((ServerWorld) world, user, RaidBase.IDENTIFIER, 6);
                playerProperties.setSlimeSuit(true);
                user.getWorld().playSoundFromEntity(null, user, SoundEvents.ITEM_ARMOR_EQUIP_IRON, user.getSoundCategory(), 1f, 1f);
            }

            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            return TypedActionResult.success(itemStack, world.isClient());
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eNow that this §eSlime §ahas been scopped,"));
        tooltip.add(Text.of("§eit's your §asticky meat shield§e!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
