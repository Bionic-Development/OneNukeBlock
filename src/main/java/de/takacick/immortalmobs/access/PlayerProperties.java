package de.takacick.immortalmobs.access;

import net.minecraft.entity.boss.ServerBossBar;

public interface PlayerProperties {

    ServerBossBar getImmortalDragonBar();

    void setItemUseTime(int itemUseTime);

    void setImmortalCannon(int immortalCannon);

    boolean hasImmortalCannon();

}
