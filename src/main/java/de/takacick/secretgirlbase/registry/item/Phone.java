package de.takacick.secretgirlbase.registry.item;

import de.takacick.secretgirlbase.SecretGirlBase;
import de.takacick.secretgirlbase.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Phone extends Item {

    public Phone(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Vec3d vec3d = user.getRotationVector().multiply(2, 0, 2).add(user.getX(), user.getY() + 0.01f, user.getZ());

        if (!world.isClient) {
            if (user instanceof PlayerProperties playerProperties) {
                playerProperties.getPhonePlayer().ifPresent(uuid -> {
                    ServerPlayerEntity target = user.getServer().getPlayerManager().getPlayer(uuid);
                    if (target != null) {
                        target.teleport((ServerWorld) user.getWorld(), vec3d.getX(), vec3d.getY(), vec3d.getZ(), user.getYaw() - 180f, 0f);
                        SecretGirlBase.updateEntityHealth(user, user.getMaxHealth() - 2f, false);
                        user.damage(world.getDamageSources().generic(), 0.001f);
                        target.changeGameMode(GameMode.CREATIVE);
                        BionicUtils.sendEntityStatus(world, target, SecretGirlBase.IDENTIFIER, 2);
                    }
                });
            }
        }

        return TypedActionResult.success(user.getStackInHand(hand), false);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of("§eUsed to summon the §b§omost handsome"));
        tooltip.add(Text.of("§b§oman §ein history, specifically to"));
        tooltip.add(Text.of("§aguide §eyou with §6tips §eor §6hints§e!"));
        tooltip.add(Text.of("§a"));
        tooltip.add(Text.of("§cWarning: A heart will be sacrificed"));
        tooltip.add(Text.of("§cper use!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
