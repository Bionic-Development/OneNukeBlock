package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.registry.entity.custom.BlockBreakEntity;
import de.takacick.deathmoney.registry.entity.projectiles.DangerousBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
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

public class DangerousMinerMagnet extends Item {

    public DangerousMinerMagnet(Settings settings) {
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

                DeathMoney.generateSphere(new BlockPos(pos), 2, false).forEach(blockPos -> {
                    BlockState blockState = world.getBlockState(blockPos);

                    if (!blockState.isAir() && blockState.getBlock().getBlastResistance() < Blocks.BEDROCK.getBlastResistance() && world.getRandom().nextDouble() <= 0.7) {
                        damageBlock(world, blockPos, blockState, user, stack);
                    }
                });

                world.getOtherEntities(user, new Box(pos, pos).expand(4)).forEach(entity -> {
                    if (entity instanceof DangerousBlockEntity) {
                        Vec3d vector = entity.getPos().subtract(user.getPos().add(0, user.getHeight() * 0.35, 0)).normalize().multiply(-0.9);
                        entity.setVelocity(vector);
                        entity.velocityDirty = true;
                        entity.velocityModified = true;
                    }
                });
            }
        } else {
            Vec3d rotation = user.getRotationVecClient();
            Vec3d pos = user.getCameraPosVec(0);

            for (int i = 0; i < 20; i++) {
                pos = pos.add(rotation);

                AtomicBoolean found = new AtomicBoolean(false);

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

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 7200;
    }

    public void damageBlock(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
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
        if (world.getRandom().nextDouble() <= 0.75) {
            world.playSound(null, blockPos, world.getBlockState(blockPos).getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 0.5F, 1.0F);
        }

        for (Entity entity : world.getOtherEntities(null, new Box(blockPos).expand(1))) {
            if (entity instanceof BlockBreakEntity blockBreakEntity && entity.isAlive() && entity.getBlockPos().equals(blockPos)) {
                blockBreakEntity.setDamage(blockBreakEntity.getDamage() + 5);
                blockBreakEntity.setLastDamage(0);

                if (blockBreakEntity.getDamage() >= 100) {
                    entity.discard();
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());

                    DangerousBlockEntity dangerousBlockEntity = new DangerousBlockEntity(world,
                            blockPos.getX() + 0.5,
                            blockPos.getY() + 0.5,
                            blockPos.getZ() + 0.5);
                    dangerousBlockEntity.setItemStack(blockState, blockState.getBlock().asItem().getDefaultStack());
                    dangerousBlockEntity.drop = true;
                    world.spawnEntity(dangerousBlockEntity);

                    itemStack.damage(1, livingEntity, en -> en.sendToolBreakStatus(en.getActiveHand()));
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

        tooltip.add(Text.of("§7Pulls in blocks with a §cdeadly"));
        tooltip.add(Text.of("§cforce§7..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}