package de.takacick.emeraldmoney.registry.heart;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.access.PlayerProperties;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.heart.Heart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class EmeraldHeart extends Heart {

    public EmeraldHeart(Identifier identifier, boolean blinking) {
        super(identifier, blinking);
    }

    @Override
    public void onDamage(LivingEntity livingEntity, DamageSource damageSource, float heartDamage, float damage, int heart) {

        if(!livingEntity.getWorld().isClient) {
            BionicUtils.sendEntityStatus((ServerWorld) livingEntity.getWorld(), livingEntity, EmeraldMoney.IDENTIFIER, 6);
        }
        if (livingEntity instanceof PlayerProperties playerProperties) {
            playerProperties.addEmeralds((int) heartDamage, true);
        }

        super.onDamage(livingEntity, damageSource, heartDamage, damage, heart);
    }
}
