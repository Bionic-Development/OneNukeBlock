package de.takacick.illegalwars.registry.block;

import de.takacick.illegalwars.IllegalWars;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SludgeLiquidBlock extends FluidBlock {
    public static final RegistryKey<DamageType> SLUDGE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(IllegalWars.MOD_ID, "sludge"));

    public static final TagKey<Fluid> SLUDGE = TagKey.of(RegistryKeys.FLUID, new Identifier(IllegalWars.MOD_ID, "sludge"));

    public SludgeLiquidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient
                && entity instanceof LivingEntity livingEntity) {
            livingEntity.damage(world.getDamageSources().create(SLUDGE_DAMAGE), 2f);

            if (!livingEntity.hasStatusEffect(StatusEffects.POISON)
                    || livingEntity.getStatusEffect(StatusEffects.POISON).getDuration() <= 175) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }
}
