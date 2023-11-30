package de.takacick.elementalblock.registry.entity.living;

import de.takacick.elementalblock.registry.ParticleRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class AirElementalGolemEntity extends VexEntity {
    public AirElementalGolemEntity(EntityType<? extends VexEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!getWorld().isClient) {
            this.deathTime += 25;
        }
        super.onDeath(damageSource);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 60) {
            double g = getX();
            double j = getZ();

            for (int i = 0; i < 15; ++i) {
                double d = random.nextGaussian();
                double e = random.nextDouble();
                double f = random.nextGaussian();

                getWorld().addParticle(ParticleRegistry.CLOUD,
                        true, g + d * getWidth(), getY() + e * getHeight(), j + f * getWidth(), d * 0.3, e * 0.3, f * 0.3);
            }
            double h = getBodyY(0.5);

            getWorld().playSound(g, h, j, SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.AMBIENT, 1f, 1f + getRandom().nextFloat() * 0.2f, true);

        } else {
            super.handleStatus(status);
        }
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {

    }

    public static DefaultAttributeContainer.Builder createVexAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0);
    }

}
