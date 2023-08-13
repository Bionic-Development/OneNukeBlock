package de.takacick.onedeathblock.registry.item;

import de.takacick.onedeathblock.OneDeathBlock;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ExplosivePlacing extends Item {

    public ExplosivePlacing(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            BionicUtils.sendEntityStatus((ServerWorld) world, user, OneDeathBlock.IDENTIFIER, 4);
            user.damage(DamageSource.explosion(user), Float.MAX_VALUE);
            user.sendMessage(Text.of("§eUnlocked §cExplosive Placing Ability§e!"));
        }
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return super.use(world, user, hand);
    }
}
