package de.takacick.heartmoney.registry.item;

import de.takacick.heartmoney.registry.ParticleRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartPulseBlaster extends Item {

    public HeartPulseBlaster(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient) {
            for (int i = 0; i < 2; i++) {
                world.addParticle(ParticleRegistry.HEART_PORTAL, user.getX(), user.getBodyY(1) + 0.2, user.getZ(),
                        (float) world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8);
            }

            if (remainingUseTicks % 4 == 0) {
                world.playSound(user.getX(), user.getBodyY(0.5), user.getZ(), SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.PLAYERS, 0.15F, 2.0F, false);
            }
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity playerEntity, int remainingUseTicks) {
        if (!world.isClient) {
            float f = 1;
            Vec3d vec3d = playerEntity.getPos().add(0, playerEntity.getHeight() / 2, 0);
            Vec3d vec3d3 = playerEntity.getRotationVector();
            Vec3d vec3d4;
            world.playSound(null, playerEntity.getX(), playerEntity.getBodyY(0.5), playerEntity.getZ(), SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1.0f, 1.0f);
            for (int h = 1; h < MathHelper.floor(f * 15) + f * 7; ++h) {
                vec3d4 = vec3d.add(vec3d3.multiply(h));
                if (h > 4) {
                    if (world.getBlockState(new BlockPos(vec3d4)).getMaterial().blocksMovement()) {
                        break;
                    }
                }
                ((ServerWorld) world).spawnParticles(ParticleRegistry.HEART_SONIC_BOOM, vec3d4.x, vec3d4.y, vec3d4.z,
                        1, 0.0, 0.0, 0.0, 0.0);
                if (h > 3) {
                    world.getOtherEntities(playerEntity, Box.from(vec3d4).expand(2.75)).forEach(entity -> {
                        if (entity instanceof LivingEntity livingEntity
                                && livingEntity.isPartOfGame()) {
                            livingEntity.damage(DamageSource.mob(playerEntity), 7f);
                            livingEntity.setVelocity(vec3d3.add(0, 1.4, 0));
                        }
                    });
                }
            }
        }

        super.onStoppedUsing(stack, world, playerEntity, remainingUseTicks);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eBlasts a powerful shockwave to show"));
        tooltip.add(Text.of("§eyour enemies how much you love them!"));
        tooltip.add(Text.of("§5§oEpic Tier"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
