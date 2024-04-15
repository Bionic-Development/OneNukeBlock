package de.takacick.onegirlfriendblock.registry.statuseffects;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class Bleeding extends StatusEffect {

    public Bleeding() {
        super(StatusEffectCategory.HARMFUL, 0x820A0A);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient) {
            livingEntity.damage(livingEntity.getWorld().getDamageSources().generic(), 2);
            BionicUtils.sendEntityStatus(livingEntity.getWorld(), livingEntity, OneGirlfriendBlock.IDENTIFIER, 3);
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}