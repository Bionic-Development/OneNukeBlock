package de.takacick.illegalwars.access;

import net.minecraft.util.math.BlockPos;

public interface PiglinProperties {

    void setPiglinGoldTurret(BlockPos blockPos);

    BlockPos getPiglinGoldTurret();

    boolean isUsingPiglinGoldTurret();
}
