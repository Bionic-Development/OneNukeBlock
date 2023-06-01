package de.takacick.deathmoney.registry.item;

import de.takacick.deathmoney.access.PlayerProperties;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.registry.entity.custom.DeathMinerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeathDropMiner extends PickaxeItem {

    public DeathDropMiner(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        miner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, pos.getY() - 1, 2, false, false, true));

        DeathMinerEntity deathMinerEntity = new DeathMinerEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        world.spawnEntity(deathMinerEntity);
        deathMinerEntity.tick();
        world.playSound(null, deathMinerEntity.getX(), deathMinerEntity.getY(), deathMinerEntity.getZ(), ParticleRegistry.DEATH_SHOP_EMERGE, SoundCategory.BLOCKS, 1f, 1f);

        if (miner instanceof PlayerProperties playerProperties) {
            playerProperties.setDeathDrop(true);
        }

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.add(Text.of("§7Mine an entire chunk under your feet!"));
        tooltip.add(Text.of("§cBeware the fall§7..."));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
