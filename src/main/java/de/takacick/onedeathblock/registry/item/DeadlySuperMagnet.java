package de.takacick.onedeathblock.registry.item;

import de.takacick.onedeathblock.registry.entity.projectiles.BuildMeteorEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeadlySuperMagnet extends Item {
    public DeadlySuperMagnet(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {

        if (!world.isClient) {
            world.playSoundFromEntity(null, user, SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.PLAYERS, 1f, 1f);
            BlockPos blockPos = user.getBlockPos();

            BuildMeteorEntity buildMeteorEntity = new BuildMeteorEntity(world, blockPos.getX() + 0.5 + user.getRandom().nextGaussian() * 50, blockPos.getY() + 0.5 + 100, blockPos.getZ() + 0.5 + user.getRandom().nextGaussian() * 50, user);
            Vec3d velocity = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ())
                    .subtract(buildMeteorEntity.getPos().multiply(1, 1, 1)).normalize();
            buildMeteorEntity.setVelocity(velocity.multiply(3.5, 3.5, 3.5));
            buildMeteorEntity.setOwner(user);
            world.spawnEntity(buildMeteorEntity);
        }

        if (user instanceof PlayerEntity playerEntity && !playerEntity.isCreative()) {
            int damage = stack.getDamage();
            stack.damage(1, user, e -> {});
            stack.setDamage(damage + 1);
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user.age % 2 == 0) {
            world.playSoundFromEntity(null, user, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.PLAYERS, 1f, 1.6f);
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§cWarning: §eWill magnetize meteor from"));
        tooltip.add(Text.of("§eouter space!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
