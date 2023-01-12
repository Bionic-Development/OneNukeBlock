package de.takacick.immortalmobs.registry.item;

import de.takacick.immortalmobs.registry.EntityRegistry;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalPickaxeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmortalPickaxe extends PickaxeItem implements ImmortalItem {

    public ImmortalPickaxe(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            world.playSound(null, new BlockPos(user.getX(), user.getY(), user.getZ()), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, 1.0F);

            ImmortalPickaxeEntity immortalPickaxeEntity = new ImmortalPickaxeEntity(EntityRegistry.IMMORTAL_PICKAXE, world, user);
            immortalPickaxeEntity.setPos(user.getX(), user.getY() + 2, user.getZ());
            immortalPickaxeEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 1.25F, 0.5F);
            world.spawnEntity(immortalPickaxeEntity);
        }
        return super.use(world, user, hand);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        return 10000f;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§eA pickaxe crafted in §dImmortality"));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
