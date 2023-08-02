package de.takacick.onedeathblock.access;

import de.takacick.onedeathblock.client.DeathToast;
import net.minecraft.entity.AnimationState;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface PlayerProperties {

    void setDeaths(int deaths);

    int addDeaths(int deaths, boolean multiplier);

    int getDeaths();

    void setDeathMultiplier(double deathMultiplier);

    double getDeathMultiplier();

    List<DeathToast> getDeathToasts();

    void setDeathTicks(int deathTicks);

    void setHeartCarverStack(ItemStack heartCarver);

    AnimationState getHeartRemovalState();

    void setHeartRemovalTicks(int heartRemovalTicks);

    int getHeartRemovalTicks();

    void setTntExplosionTicks(int tntExplosionTicks);

    int getTntExplosionTicks();

    int getMeteorShakeTicks();

    void resetDamageDelay();

    void setDeathsDisplay(boolean deathsDisplay);

    boolean hasDeathsDisplay();

    void setExplosivePlacing(boolean explosivePlacing);

    boolean hasExplosivePlacing();

    void setShockedTicks(int shockedTicks);

    int getShockedTicks();

}
