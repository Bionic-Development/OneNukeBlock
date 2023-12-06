package de.takacick.emeraldmoney.registry.item;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.registry.EntityRegistry;
import de.takacick.emeraldmoney.registry.entity.custom.VillagerNoseEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class VillagerNose extends Item {

    private static final List<TagKey<Block>> ORE_TAGS = Arrays.asList(BlockTags.LAPIS_ORES, BlockTags.IRON_ORES, BlockTags.GOLD_ORES,
            BlockTags.DIAMOND_ORES, BlockTags.COPPER_ORES, BlockTags.COAL_ORES);

    public VillagerNose(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(this)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }

        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if (!world.isClient) {
            if (remainingUseTicks % 15 == 0) {
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_SNIFFER_SCENTING, SoundCategory.PLAYERS, 0.6f, 1.25f);
            }
        }

        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (!world.isClient) {
            List<BlockPos> ores = new ArrayList<>(EmeraldMoney.generateSphere(user.getBlockPos(), 15, false));
            ores.removeIf(blockPos -> {
                BlockState blockState = world.getBlockState(blockPos);
                return blockState.isAir() || ORE_TAGS.stream().noneMatch(blockState::isIn);
            });

            if (ores.isEmpty()) {
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.PLAYERS, 1f, 1f);
                if (user instanceof PlayerEntity playerEntity) {
                    playerEntity.getItemCooldownManager().set(this, 10);
                }
            } else {
                BlockPos orePos = ores.get(world.getRandom().nextInt(ores.size()));
                double distance = user.getPos().distanceTo(Vec3d.ofCenter(orePos));

                BlockState blockState = world.getBlockState(orePos);

                Consumer<BlockPos> consumer = (blockPos) -> {
                    world.setBlockState(blockPos, Blocks.EMERALD_ORE.getDefaultState());

                    VillagerNoseEntity villagerNoseEntity = new VillagerNoseEntity(EntityRegistry.VILLAGER_NOSE, world);
                    villagerNoseEntity.setOwnerUuid(user.getUuid());
                    villagerNoseEntity.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                    world.spawnEntity(villagerNoseEntity);
                };

                consumer.accept(orePos);
                findNearbyOres(blockState.getBlock(), world, orePos, consumer, 1);

                Vec3d vec3d = user.getCameraPosVec(0).add(0, -0.75, 0);
                Vec3d vec3d2 = Vec3d.ofCenter(orePos).subtract(vec3d).normalize().multiply(0.25);

                if (user instanceof ServerPlayerEntity serverPlayerEntity) {
                    for (int i = 0; i < (distance * 4); i++) {
                        vec3d = vec3d.add(vec3d2);
                        ((ServerWorld) world).spawnParticles(serverPlayerEntity, ParticleTypes.HAPPY_VILLAGER, true, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 1, 0, 0, 0, 0);
                    }
                    if (!serverPlayerEntity.isCreative()) {
                        stack.decrement(1);
                    }
                }
                world.playSoundFromEntity(null, user, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 1f, 1f);
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 60;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9§oSniff Sniff Sniff"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    private void findNearbyOres(Block block, World world, BlockPos blockPos, Consumer<BlockPos> consumer, int step) {

        for (Direction direction : Direction.values()) {
            BlockState blockState = world.getBlockState(blockPos.add(direction.getVector()));
            if (blockState.isOf(block) && world.getRandom().nextInt(step + 1) <= 1) {
                consumer.accept(blockPos.add(direction.getVector()));
                findNearbyOres(block, world, blockPos, consumer, step + 1);
            }
        }

    }
}