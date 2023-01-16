package de.takacick.imagineanything.registry.item;

import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.entity.living.MysteriousEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
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

public class MysteriousLamp extends Item {
    public MysteriousLamp(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (remainingUseTicks % 10 == 0) {
            world.playSoundFromEntity(null, user, SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT, 1, 1);
        }

        for (int i = 0; i < 5; i++) {
            world.addParticle(ParticleTypes.ENCHANT, user.getX(), user.getY() + 1.78, user.getZ(),
                    ((float) world.getRandom().nextGaussian() * 8), world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8);
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }


    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        Vec3d vec3d = user.getRotationVector().multiply(3, 1, 3).add(user.getX(), user.getEyeY(), user.getZ());

        if (!world.isClient) {
            EntityType<MysteriousEntity> entityType2 = EntityRegistry.MYSTERIOUS_ENTITY;
            MysteriousEntity entity = (MysteriousEntity) entityType2.spawnFromItemStack((ServerWorld) world, null, null, new BlockPos(vec3d), SpawnReason.EVENT, true, false);

            if (entity != null) {
                BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, ImagineAnything.IDENTIFIER, 4);
                world.playSoundFromEntity(null, entity, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.AMBIENT, 1, 1);
            }
        }

        stack.decrement(1);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9An ultra rare relic containing a"));
        tooltip.add(Text.of("§5Mysterious Entity §9inside..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
