package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.entity.custom.BlackHoleEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlackHole extends Item {

    public BlackHole(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        Vec3d vec3d = user.getRotationVector().multiply(2, 1, 2).add(user.getX(), user.getEyeY(), user.getZ());
        if (!world.isClient) {
            BlackHoleEntity blackHoleEntity = new BlackHoleEntity(EntityRegistry.BLACK_HOLE, world);
            blackHoleEntity.setPos(vec3d.getX(), vec3d.getY(), vec3d.getZ());
            blackHoleEntity.setYaw(-user.getYaw());
            world.spawnEntity(blackHoleEntity);
            BionicUtils.sendEntityStatus((ServerWorld) world, blackHoleEntity, DeathMoney.IDENTIFIER, 12);
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Not even stars can escape the §cdeath"));
        tooltip.add(Text.of("§7from a §5Black Hole§7..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}