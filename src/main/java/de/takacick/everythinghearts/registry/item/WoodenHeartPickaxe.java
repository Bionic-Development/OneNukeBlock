package de.takacick.everythinghearts.registry.item;

import de.takacick.everythinghearts.EverythingHearts;
import de.takacick.everythinghearts.registry.ItemRegistry;
import de.takacick.everythinghearts.registry.block.HeartmondOreBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class WoodenHeartPickaxe extends PickaxeItem {

    public WoodenHeartPickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return state.isOf(ItemRegistry.BASIC_HEART_BLOCK) || state.isOf(ItemRegistry.MULTI_HEART_BLOCK) ? this.miningSpeed : super.getMiningSpeedMultiplier(stack, state);
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return state.isOf(ItemRegistry.BASIC_HEART_BLOCK) || state.isOf(ItemRegistry.MULTI_HEART_BLOCK) || super.canMine(state, world, pos, miner);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getBlock() instanceof HeartmondOreBlock) {
            world.syncWorldEvent(102391239, pos, 1);
        } else if (EverythingHearts.HEART_BLOCKS.contains(state.getBlock())) {
            for (int i = 0; i < 1; i++) {
                miner.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                        .addPersistentModifier(new EntityAttributeModifier(UUID.randomUUID(), "heart", 2.0f, EntityAttributeModifier.Operation.ADDITION));
                miner.setHealth(miner.getHealth() + 2);
            }
            world.syncWorldEvent(102391239, pos, 0);
            miner.sendMessage(Text.of("§a[+1 §c❤§a]"));
        }

        return super.postMine(stack, world, state, pos, miner);
    }
}