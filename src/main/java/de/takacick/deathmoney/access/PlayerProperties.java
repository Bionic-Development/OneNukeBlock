package de.takacick.deathmoney.access;

import de.takacick.deathmoney.registry.entity.custom.DeathShopPortalEntity;
import de.takacick.deathmoney.utils.DeathToast;
import net.minecraft.entity.AnimationState;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface PlayerProperties {

    void setDeaths(int deaths);

    int addDeaths(int deaths, boolean multiplier);

    int getDeaths();

    void setDeathShopPortal(DeathShopPortalEntity deathShopPortalEntity);

    void setDeathMultiplier(double deathMultiplier);

    double getDeathMultiplier();

    List<DeathToast> getDeathToasts();

    void setDeathTicks(int deathTicks);

    void setHeartCarverStack(ItemStack heartCarver);

    AnimationState getHeartRemovalState();

    void setHeartRemovalTicks(int heartRemovalTicks);

    int getHeartRemovalTicks();

    void setCakeExplosionTicks(int cakeExplosionTicks);

    int getCakeExplosionTicks();

    void setGamerAllergyTicks(int gamerAllergyTicks);

    int getGamerAllergyTicks();

    void setMeteorTicks(int meteorTicks);

    void setMeteorShakeTicks(int meteorShakeTicks);

    int getMeteorShakeTicks();

    void setDeathDrop(boolean deathDrop);

    void resetDamageDelay();

    void setEarthFangsTicks(int earthFangsTicks);

}
