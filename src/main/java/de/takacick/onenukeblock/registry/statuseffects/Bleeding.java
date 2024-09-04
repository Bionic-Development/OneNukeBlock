package de.takacick.onenukeblock.registry.statuseffects;

import de.takacick.onenukeblock.registry.ParticleRegistry;
import de.takacick.onenukeblock.OneNukeBlock;
import de.takacick.onenukeblock.registry.ParticleRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Bleeding extends StatusEffect {
    public static final RegistryKey<DamageType> BLEEDING = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(OneNukeBlock.MOD_ID, "bleeding"));

    public Bleeding() {
        super(StatusEffectCategory.HARMFUL, 0x820A0A);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient) {
            livingEntity.damage(livingEntity.getWorld().getDamageSources().create(BLEEDING), 2);
        } else {
            double g = livingEntity.getX();
            double h = livingEntity.getBodyY(0.5);
            double j = livingEntity.getZ();

            for (int i = 0; i < 3; ++i) {
                double d = livingEntity.getRandom().nextGaussian() * 0.2;
                double e = livingEntity.getRandom().nextDouble() * 0.3;
                double f = livingEntity.getRandom().nextGaussian() * 0.2;

                livingEntity.getWorld().addParticle(ParticleRegistry.FALLING_BLOOD,
                        true, g + d, h + e, j + f, d * 0.1, e * 0.1, f * 0.1);
            }
        }

        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}