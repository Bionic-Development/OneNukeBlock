package de.takacick.everythinghearts.registry.item;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.access.PlayerProperties;
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

public class MysticHeartTouch extends Item {

    public MysticHeartTouch(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!(user instanceof PlayerProperties playerProperties)) {
            return TypedActionResult.pass(itemStack);
        }

        if (!world.isClient) {
            world.playSound(null, user.getX(), user.getBodyY(0.5), user.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, user.getSoundCategory(), 1f, 1f);
            playerProperties.setHeartTransformTicks(37);
            BionicUtils.sendEntityStatus((ServerWorld) world, user, EverythingHearts.IDENTIFIER, 1);
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eBlesses the user with the touch of §c❤"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
