package de.takacick.onescaryblock.registry.entity.custom;

import de.takacick.onescaryblock.registry.EntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.world.World;

public class HerobrineLightningEntity extends LightningEntity {

    public HerobrineLightningEntity(EntityType<? extends LightningEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        if (this.age <= 1) {
            if (!getWorld().isClient) {
                HerobrineLightningEffectEntity herobrineLightningEffectEntity = new HerobrineLightningEffectEntity(EntityRegistry.HEROBRINE_LIGHTNING_EFFECT, getWorld());
                herobrineLightningEffectEntity.refreshPositionAndAngles(getX(), getY(), getZ(), 0f, 0f);
                getWorld().spawnEntity(herobrineLightningEffectEntity);
            }
        }

        super.tick();
    }
}
