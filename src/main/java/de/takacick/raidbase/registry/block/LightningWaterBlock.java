package de.takacick.raidbase.registry.block;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.LivingProperties;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightningWaterBlock extends FluidBlock {
    public static final RegistryKey<DamageType> LIGHTNING_WATER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RaidBase.MOD_ID, "lightning_water"));

    public LightningWaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient
                && entity instanceof LivingProperties livingProperties) {
            if (!(entity.isSpectator()
                    || entity.isInvulnerableTo(world.getDamageSources().create(LIGHTNING_WATER))
                    || entity instanceof PlayerEntity playerEntity && playerEntity.isCreative())) {
                livingProperties.setWaterElectroShock(5);
            }
        }

        super.onEntityCollision(state, world, pos, entity);
    }
}
