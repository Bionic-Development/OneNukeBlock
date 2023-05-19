package de.takacick.heartmoney.registry.item;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.entity.custom.BlockBreakEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StrongHeartSucker extends Item {

    public StrongHeartSucker(Settings settings) {
        super(settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient && user instanceof PlayerEntity playerEntity) {

            Vec3d rotation = user.getRotationVecClient();
            Vec3d startPos = user.getCameraPosVec(0);
            Vec3d pos = startPos;
            HitResult blockHit = playerEntity.raycast(20, 0, true);

            for (int i = 0; i < blockHit.getPos().distanceTo(startPos); i++) {
                pos = pos.add(rotation);

                HeartMoney.generateSphere(new BlockPos(pos), 2, false).forEach(blockPos -> {
                    BlockState blockState = world.getBlockState(blockPos);

                    if (!blockState.isAir() && blockState.getBlock().getBlastResistance() < Blocks.BEDROCK.getBlastResistance() && world.getRandom().nextDouble() <= 0.7) {
                        damageBlock(user, world, blockPos, blockState);
                    }
                });

                world.getOtherEntities(user, new Box(pos, pos).expand(1)).forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity && !(entity instanceof PlayerEntity)) {
                        if (user.getRandom().nextDouble() <= 0.1) {
                            if (livingEntity.getHealth() <= 2) {
                                BionicUtils.sendEntityStatus((ServerWorld) world, livingEntity, HeartMoney.IDENTIFIER, 11);
                                entity.discard();
                            } else {
                                livingEntity.setHealth(livingEntity.getHealth() - 2);
                            }

                            if (world.getRandom().nextDouble() <= 0.5) {
                                ItemEntity itemEntity = new ItemEntity(world, livingEntity.getX(), livingEntity.getBodyY(0.5), livingEntity.getZ(), ItemRegistry.HEART.getDefaultStack(), 0, 0, 0);
                                itemEntity.setOwner(livingEntity.getUuid());
                                world.spawnEntity(itemEntity);
                            }
                        }
                    }
                });

                world.getOtherEntities(user, new Box(pos, pos).expand(4)).forEach(entity -> {
                    if (entity instanceof ItemEntity itemEntity) {
                        itemEntity.setPickupDelay(0);
                        if (distanceTo(itemEntity, playerEntity) <= 0.8) {
                            itemEntity.setPickupDelay(0);
                            entity.setVelocity(entity.getVelocity().multiply(0.05));
                            entity.velocityDirty = true;
                            entity.velocityModified = true;
                        } else {
                            Vec3d vector = entity.getPos().subtract(user.getPos().add(0, user.getHeight() / 2, 0)).normalize().multiply(-1.1);
                            entity.setVelocity(vector);
                            entity.velocityDirty = true;
                            entity.velocityModified = true;
                        }
                    }
                });
            }
        } else {
            Vec3d rotation = user.getRotationVecClient();
            Vec3d pos = user.getCameraPosVec(0);

            for (int i = 0; i < 20; i++) {
                pos = pos.add(rotation);

                AtomicBoolean found = new AtomicBoolean(false);

                world.getOtherEntities(user, new Box(pos, pos).expand(1)).forEach(entity -> {
                    if ((entity instanceof LivingEntity livingEntity && livingEntity.isPartOfGame()) || entity instanceof ItemEntity) {
                        found.set(true);

                        if (world.getRandom().nextDouble() <= 0.2) {
                            Vec3d vector = entity.getPos().subtract(user.getPos()).normalize().multiply(-1.1);
                            world.addImportantParticle(ParticleRegistry.HEART_POOF, true,
                                    entity.getX() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getBodyY(0.5f) + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    entity.getZ() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.2,
                                    vector.getX() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.05,
                                    vector.getY() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.05,
                                    vector.getZ() + (world.getRandom().nextDouble() - world.getRandom().nextDouble()) * 0.05);
                        }
                    }
                });

                if (found.get()) {
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.BLOCKS, 0.5f, 1.4f, false);
                }
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    public float distanceTo(Entity entity, Entity entity1) {
        float f = (float) (entity.getX() - entity1.getX());
        float g = (float) (entity.getBodyY(0.5) - entity1.getBodyY(0.5));
        float h = (float) (entity.getZ() - entity1.getZ());
        return MathHelper.sqrt(f * f + g * g + h * h);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 7200;
    }

    public void damageBlock(LivingEntity livingEntity, World world, BlockPos blockPos, BlockState blockState) {
        for (int x = 0; x < 13; x++) {
            double d = (double) blockPos.getX() + 0.6 * world.getRandom().nextGaussian();
            double e = (double) blockPos.getY() + 0.6 * world.getRandom().nextGaussian();
            double f = (double) blockPos.getZ() + 0.6 * world.getRandom().nextGaussian();
            ((ServerWorld) world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK,
                            blockState), d, e, f,
                    1, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F,
                    0.05000000074505806D, MathHelper.nextBetween(world.getRandom(), -1.0F, 1.0F) * 0.083333336F,
                    0);
        }
        world.playSound(null, blockPos, world.getBlockState(blockPos).getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

        for (Entity entity : world.getOtherEntities(null, new Box(blockPos).expand(1))) {
            if (entity instanceof BlockBreakEntity blockBreakEntity && entity.isAlive() && entity.getBlockPos().equals(blockPos)) {
                blockBreakEntity.setDamage(blockBreakEntity.getDamage() + 25);
                blockBreakEntity.setLastDamage(0);
                if (blockBreakEntity.getDamage() >= 100) {
                    entity.discard();
                    ItemEntity itemEntity = new ItemEntity(world, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, ItemRegistry.HEART.getDefaultStack(), 0, 0, 0);
                    itemEntity.setOwner(livingEntity.getUuid());
                    world.spawnEntity(itemEntity);

                    world.breakBlock(blockPos, false);
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                }
                return;
            }
        }

        BlockBreakEntity blockBreakEntity = new BlockBreakEntity(world,
                blockPos.getX() + 0.5,
                blockPos.getY() + 0.5,
                blockPos.getZ() + 0.5);
        world.spawnEntity(blockBreakEntity);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eA powerful vacuum that sucks the love"));
        tooltip.add(Text.of("§eout of anything!"));
        tooltip.add(Text.of("§5§oEpic Tier"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
