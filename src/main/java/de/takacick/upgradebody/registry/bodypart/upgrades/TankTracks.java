package de.takacick.upgradebody.registry.bodypart.upgrades;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.registry.bodypart.BodyPart;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class TankTracks extends BodyPart {

    public TankTracks() {
        super("Tank Tracks", new Identifier(UpgradeBody.MOD_ID, "tank_tracks"));
    }

    @Override
    public double getHeight() {
        return (12f / 16f) * 0.9375f;
    }

    @Override
    public double getWidth() {
        return 0.6f;
    }

    @Override
    public float getPivotYOffset() {
        return 12f;
    }

    @Override
    public int getHeightIndex() {
        return 2;
    }

    @Override
    public boolean affectModelOrdering() {
        return true;
    }

    @Override
    public float getHealth() {
        return 6f;
    }

    @Override
    public boolean allowsWalking() {
        return true;
    }

    @Override
    public void onEquip(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, -1, 0, false, false, true));
        super.onEquip(playerEntity);
    }

    @Override
    public void tick(PlayerEntity playerEntity) {
        playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, -1, 0, false, false, true));
        super.tick(playerEntity);
    }

    @Override
    public void onDequip(PlayerEntity playerEntity) {
        playerEntity.removeStatusEffect(StatusEffects.SPEED);
        super.onDequip(playerEntity);
    }
}
