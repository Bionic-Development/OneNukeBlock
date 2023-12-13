package de.takacick.upgradebody.registry.bodypart.upgrades;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.PlayerProperties;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class EnergyBellyCannon extends BodyPart {

    public EnergyBellyCannon() {
        super("Energy Belly Cannon", new Identifier(UpgradeBody.MOD_ID, "energy_belly_cannon"));
    }

    @Override
    public String getInheritModelPart() {
        return "body";
    }

    @Override
    public double getHeight() {
        return (12f / 16f) * 0.9375f;
    }

    @Override
    public double getWidth() {
        return 0.6;
    }

    @Override
    public float getPivotYOffset() {
        return 12f;
    }

    @Override
    public int getHeightIndex() {
        return 1;
    }

    @Override
    public boolean affectModelOrdering() {
        return true;
    }

    @Override
    public float getHealth() {
        return 20f;
    }

    @Override
    public void onEquip(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 0, false, false, true));
        super.onEquip(playerEntity);
    }

    @Override
    public void tick(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 0, false, false, true));

        PlayerProperties playerProperties = (PlayerProperties) playerEntity;
        World world = playerEntity.getWorld();

        if (world.isClient && playerProperties.isUsingEnergyBellyBlast()) {
            for (int i = 0; i < 8 * Math.min(playerProperties.getEnergyBellyBlastUsageTicks() / 50f, 1f); i++) {
                world.addParticle(ParticleRegistry.ENERGY_PORTAL, playerEntity.getX(), playerEntity.getY() + playerEntity.getHeight() * 0.6, playerEntity.getZ(),
                        0, 0, playerEntity.getId());
            }

            if (playerEntity.getItemUseTime() % 8 == 0) {
                world.playSound(playerEntity.getX(), playerEntity.getBodyY(0.5), playerEntity.getZ(),
                        SoundEvents.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.PLAYERS, 0.25f,
                        3.1f + (Math.min(playerProperties.getEnergyBellyBlastUsageTicks() / 50f, 2f)), true);
            }
        }

        super.tick(playerEntity);
    }

    @Override
    public void onDequip(PlayerEntity playerEntity) {
        playerEntity.removeStatusEffect(StatusEffects.RESISTANCE);
        super.onDequip(playerEntity);
    }
}
