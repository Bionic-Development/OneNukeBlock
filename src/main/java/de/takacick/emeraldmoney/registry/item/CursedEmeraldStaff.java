package de.takacick.emeraldmoney.registry.item;

import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.emeraldmoney.registry.entity.custom.VillagerSpikeEntity;
import de.takacick.emeraldmoney.registry.particles.TargetParticleEffect;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CursedEmeraldStaff extends Item {

    public CursedEmeraldStaff(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user.getWorld() instanceof ServerWorld serverWorld) {
            Vec3d vec3d = user.getPos();
            Vec3d vec3d2 = user.getRotationVector();
            Vec3d vec3d3 = vec3d2.normalize().multiply(1, 0, 1);
            vec3d = vec3d.add(vec3d3);

            for (int i = 1; i < MathHelper.floor(vec3d2.length()) + 15; ++i) {
                Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));

                float height = getBlockHeightAtVec3d(serverWorld, vec3d4);

                if (height <= -100) {
                    break;
                }
                vec3d = new Vec3d(vec3d.getX(), height, vec3d.getZ());
                VillagerSpikeEntity villagerSpikeEntity = new VillagerSpikeEntity(serverWorld, vec3d4.x, height, vec3d4.z, user.getYaw() + (float) user.getRandom().nextGaussian() * 15f, (float) user.getRandom().nextGaussian() * 15f, i * 1, user);
                villagerSpikeEntity.setKnockback(vec3d3.multiply(0.2).add(0, 0.118, 0));
                villagerSpikeEntity.setVillagerData(new VillagerData(Registries.VILLAGER_TYPE.getRandom(world.getRandom()).get().value(),
                        Registries.VILLAGER_PROFESSION.getRandom(world.getRandom()).get().value(), 0));
                serverWorld.spawnEntity(villagerSpikeEntity);
            }
            world.playSound(null, user.getX(), user.getBodyY(0.5), user.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 3f, 1f);
        }

        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if (world.isClient) {

            if (world.getRandom().nextDouble() <= 0.3 && remainingUseTicks % 2 == 0) {
                world.playSound(user.getX() + world.getRandom().nextGaussian() * 2, user.getBodyY(0.5) + world.getRandom().nextGaussian() * 2, user.getZ() + world.getRandom().nextGaussian() * 2, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 0.1f, 1f + world.getRandom().nextFloat() * 0.3f, true);
            }

            float progress = BowItem.getPullProgress(this.getMaxUseTime(stack) - remainingUseTicks);

            for (int i = 0; i <= Math.min(1 * progress, 2); i++) {
                double angle = user.getWorld().getRandom().nextDouble() * 360;
                double x = (user.getWorld().getRandom().nextDouble() * 1.5 * Math.cos(angle));
                double z = (user.getWorld().getRandom().nextDouble() * 1.5 * Math.sin(angle));

                world.addParticle(new TargetParticleEffect(ParticleRegistry.EMERALD_SPELL, user.getId()), x + user.getX(), user.getY(), z + user.getZ(), 0, 0, 0);
            }
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Rumored to be forged from the"));
        tooltip.add(Text.of("§egreediest Villagers§9, resulting in"));
        tooltip.add(Text.of("§9cursed §aemerald §9magic!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public static float getBlockHeightAtVec3d(World world, Vec3d vec3d) {
        float ylevel = -100;

        BlockPos blockPos;
        BlockState blockState;
        for (int y = 1; y >= -4; y--) {
            blockPos = BlockPos.ofFloored(vec3d.add(0, y, 0));
            blockState = world.getBlockState(blockPos);
            VoxelShape shape = blockState.getCollisionShape(world, blockPos);
            float height = shape.isEmpty() ? 0f : (float) shape.getBoundingBox().maxY;
            if (!blockState.isAir() &&
                    (((AbstractBlockSettingsAccessor) ((AbstractBlockAccessor)
                            blockState.getBlock()).getSettings()).getCollidable() ||
                            height <= 0.7)) {
                ylevel = blockPos.getY() + height;
                break;
            }
        }

        return ylevel;
    }
}
