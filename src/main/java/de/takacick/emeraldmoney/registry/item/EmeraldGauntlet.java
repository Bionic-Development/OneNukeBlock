package de.takacick.emeraldmoney.registry.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmeraldGauntlet extends Item {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public EmeraldGauntlet(Settings settings) {
        super(settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 7, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return isSuitableFor(stack, state) ? 4.0f : 1.0f;
    }

    @Override
    public boolean isSuitableFor(BlockState state) {
        return state.isIn(BlockTags.PICKAXE_MINEABLE) || state.isIn(BlockTags.AXE_MINEABLE) || state.isIn(BlockTags.SHOVEL_MINEABLE);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§9Belonged to an §aEmerald God§9, now used"));
        tooltip.add(Text.of("§9to §epummel anything§9!"));

        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof PlayerEntity playerEntity) {
            List<BlockPos> list = new ArrayList<>();
            findPositions(world, miner, pos).forEach(blockPos -> {
                BlockState blockState = world.getBlockState(blockPos);
                float delta = blockState.getBlock().calcBlockBreakingDelta(blockState, playerEntity, world, blockPos);
                if (!blockState.isAir() && delta > 0) {
                    world.breakBlock(blockPos, true, miner);
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                    list.add(blockPos);
                }
            });

            for (int i = 0; i < world.getRandom().nextBetween(1, 2); i++) {
                if (list.isEmpty()) {
                    break;
                }

                BlockPos blockPos = list.get(world.getRandom().nextInt(list.size()));
                Block.dropStack(world, blockPos, Items.EMERALD.getDefaultStack());
            }

            for (int i = 0; i < world.getRandom().nextBetween(4, 7); i++) {
                if (list.isEmpty()) {
                    break;
                }

                BlockPos blockPos = list.get(world.getRandom().nextInt(list.size()));
                world.syncWorldEvent(821482, blockPos, 0);
                list.remove(blockPos);
            }
        }
        return true;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        if (!target.getWorld().isClient) {
            BionicUtils.sendEntityStatus((ServerWorld) target.getWorld(), target, EmeraldMoney.IDENTIFIER, 5);
        }

        return super.postHit(stack, target, attacker);
    }

    public List<BlockPos> findPositions(World world, LivingEntity playerEntity, BlockPos target) {
        Direction direction = playerEntity.getMovementDirection();
        Direction nextDirection = direction.rotateYClockwise();
        boolean horizontal = true;

        if (playerEntity.getPitch() > 80) {
            direction = Direction.DOWN;
            nextDirection = Direction.UP;
            horizontal = false;
        } else if (playerEntity.getPitch() < -80) {
            direction = Direction.UP;
            nextDirection = Direction.DOWN;
            horizontal = false;
        }

        int range = 3;
        BlockPos blockPos;
        int offsetX = horizontal ? direction.getOffsetX() == 0 ? nextDirection.getOffsetX() * -1 : direction.getOffsetX() : 1;
        int offsetY = horizontal ? 1 : direction.getOffsetY();
        int offsetZ = horizontal ? direction.getOffsetZ() == 0 ? nextDirection.getOffsetZ() * -1 : direction.getOffsetZ() : 1;

        if (direction.equals(Direction.DOWN) || direction.equals(Direction.UP)) {
            blockPos = target.add(-1, 0, -1);
        } else {
            blockPos = target.add(nextDirection.getOffsetX() * ((range - 1) / 2), -1, nextDirection.getOffsetZ() * ((range - 1) / 2));
        }

        List<BlockPos> positions = new ArrayList<>();

        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range; y++) {
                for (int z = 0; z < range; z++) {
                    BlockPos pos = blockPos.add(x * offsetX, y * offsetY, z * offsetZ);
                    positions.add(pos);
                }
            }
        }

        return positions;
    }
}
