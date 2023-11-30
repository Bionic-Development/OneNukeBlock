package de.takacick.elementalblock.registry.item;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.entity.projectile.WhisperwindEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WhisperwindBow extends BowItem {

    public WhisperwindBow(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float f;
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        if ((double) (f = WhisperwindBow.getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks)) < 0.1) {
            return;
        }
        if (!world.isClient) {
            List<LivingEntity> targets = new ArrayList<>();

            for (int j = 0; j < 3; j++) {
                LivingEntity livingEntity = world.getClosestEntity(world.getEntitiesByClass(LivingEntity.class,
                        user.getBoundingBox().expand(25), entity -> !targets.contains(entity)), TargetPredicate.DEFAULT, user, user.getX(), user.getEyeY(), user.getZ());
                if (livingEntity != null) {
                    targets.add(livingEntity);
                }
                WhisperwindEntity whisperwindEntity = new WhisperwindEntity(world, playerEntity, livingEntity);
                whisperwindEntity.setPos(user.getX(), user.getY() + user.getHeight() * 0.75, user.getZ());
                whisperwindEntity.setVelocity(playerEntity, playerEntity.getPitch() + (float) world.getRandom().nextGaussian() * 65, playerEntity.getYaw() + (float) world.getRandom().nextGaussian() * 65, 0, f * 3.0f, 1.0f);

                world.spawnEntity(whisperwindEntity);
                world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
            }
            BionicUtils.sendEntityStatus((ServerWorld) world, playerEntity, OneElementalBlock.IDENTIFIER, 2);
        }
        world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    public static float getPullProgress(int useTicks) {
        float f = (float) useTicks / 20.0f;
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eShoots a dangerous homing projectile"));
        tooltip.add(Text.of("§emade from the §fsharpest winds§e!"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
