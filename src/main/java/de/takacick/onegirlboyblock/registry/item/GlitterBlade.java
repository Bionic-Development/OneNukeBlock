package de.takacick.onegirlboyblock.registry.item;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class GlitterBlade extends SwordItem implements HandheldItem {

    public static ToolMaterial GLITTER_BLADE_MATERIAL = new GlitterBladeMaterial();

    public GlitterBlade(Settings settings) {
        super(GLITTER_BLADE_MATERIAL, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        double d;
        if (target instanceof LivingEntity) {
            d = target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        } else {
            d = 0.0;
        }

        double d2 = d;
        double e = Math.max(0.0, 1.0 - d2);
        target.setVelocity(target.getVelocity().multiply(1, 1, 1).add(0.0, (double) 0.45f * e, 0.0));
        target.velocityModified = true;
        target.velocityDirty = true;

        EventHandler.sendEntityStatus(target.getWorld(), target, OneGirlBoyBlock.IDENTIFIER, 2, 0);

        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal("Making sure your §cbattles").withColor(0xFFCCFF));
        tooltip.add(Text.literal("are as §efabulous §ras your are!").withColor(0xFFCCFF));

        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public boolean showRightArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return false;
    }

    @Override
    public boolean showLeftArm(LivingEntity livingEntity, ItemStack stack, Arm itemArm) {
        return false;
    }

    @Override
    public boolean allowVanillaUsageAnimation() {
        return false;
    }

    public static class GlitterBladeMaterial implements ToolMaterial {

        @Override
        public int getDurability() {
            return 1561;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 8.0f;
        }

        @Override
        public float getAttackDamage() {
            return 3.0f;
        }

        @Override
        public TagKey<Block> getInverseTag() {
            return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        }

        @Override
        public int getEnchantability() {
            return 10;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.empty();
        }
    }
}
