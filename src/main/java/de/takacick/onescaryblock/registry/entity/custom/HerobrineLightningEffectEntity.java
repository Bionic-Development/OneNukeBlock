package de.takacick.onescaryblock.registry.entity.custom;

import de.takacick.onescaryblock.access.HerobrineLightningProperties;
import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.onescaryblock.registry.entity.living.HerobrineEntity;
import de.takacick.onescaryblock.utils.datatracker.HerobrineLightningDamageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class HerobrineLightningEffectEntity extends Entity {

    public HerobrineLightningEffectEntity(EntityType<? extends HerobrineLightningEffectEntity> entityType, World world) {
        super(entityType, world);
    }

    public HerobrineLightningEffectEntity(World world, double x, double y, double z) {
        this(EntityRegistry.HEROBRINE_LIGHTNING_EFFECT, world);
        this.setPosition(x, y, z);
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {

        if (getWorld().isClient) {
            for (int i = 0; i < 3; ++i) {
                double d = this.getX() + (this.random.nextDouble() - this.random.nextDouble()) * 2.0;
                double e = this.getY() + (this.random.nextDouble() - this.random.nextDouble()) * 2.0;
                double f = this.getZ() + (this.random.nextDouble() - this.random.nextDouble()) * 2.0;
                this.getWorld().addImportantParticle(ParticleRegistry.HEROBRINE_LIGHTNING, true, d, e, f, 0.0, 0.0, 0.0);
            }
        } else {
            float size = 1f - this.age / 5f;

            getWorld().getOtherEntities(this, getBoundingBox().expand(8 * size)).forEach(entity -> {
                if (entity instanceof HerobrineLightningProperties herobrineLightningProperties && !(entity instanceof HerobrineEntity)) {
                    HerobrineLightningDamageHelper herobrineLightningDamageHelper = herobrineLightningProperties.getHerobrineLightningHelper();
                    herobrineLightningDamageHelper.setTick(herobrineLightningDamageHelper.getMaxTicks());
                    herobrineLightningProperties.setHerobrineLightningHelper(herobrineLightningDamageHelper);
                }
            });

            if (this.age >= 5) {
                this.discard();
            }
        }
    }

    @Override
    public boolean shouldRender(double distance) {
        return false;
    }

    @Override
    public boolean shouldSave() {
        return true;
    }
}

