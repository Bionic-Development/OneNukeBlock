package de.takacick.elementalblock.registry.entity.living;

import de.takacick.elementalblock.registry.ParticleRegistry;
import de.takacick.elementalblock.registry.particles.ColoredParticleEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class WaterElementalSlimeEntity extends SlimeEntity {

    public WaterElementalSlimeEntity(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setSize(int size, boolean heal) {
        super.setSize(size, heal);
        int i = MathHelper.clamp(size, 1, 127);
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(i * i);
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2f + 0.1f * (float) i);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(i + 2);
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
            for (int i = 0; i < 5; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ParticleRegistry.WATER_POOF, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }

            double g = getX();
            double j = getZ();

            Vector3f color = Vec3d.unpackRgb(4159204).toVector3f();

            for (int i = 0; i < 25 * getSize(); ++i) {
                double d = random.nextGaussian();
                double e = random.nextDouble();
                double f = random.nextGaussian();

                getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.FALLING_WATER, color),
                        true, g + d * getWidth(), getY() + e * getHeight(), j + f * getWidth(), d * 0.3, e * 0.3, f * 0.3);
            }
            double h = getBodyY(0.5);

            getWorld().playSound(g, h, j, SoundEvents.ENTITY_AXOLOTL_SPLASH, SoundCategory.AMBIENT, 1f, 1f + getRandom().nextFloat() * 0.2f, true);

        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        int i = this.getSize();
        if (!this.getWorld().isClient && i > 1 && this.isDead()) {
            Text text = this.getCustomName();
            boolean bl = this.isAiDisabled();
            float f = (float) i / 4.0f;
            int j = i / 2;
            int k = 2 + this.random.nextInt(3);
            for (int l = 0; l < k; ++l) {
                float g = ((float) (l % 2) - 0.5f) * f;
                float h = ((float) (l / 2) - 0.5f) * f;
                SlimeEntity slimeEntity = this.getType().create(this.getWorld());
                if (slimeEntity == null) continue;
                if (this.isPersistent()) {
                    slimeEntity.setPersistent();
                }
                slimeEntity.setCustomName(text);
                slimeEntity.setAiDisabled(bl);
                slimeEntity.setInvulnerable(this.isInvulnerable());
                slimeEntity.setSize(j, true);
                slimeEntity.refreshPositionAndAngles(this.getX() + (double) g, getBodyY(0.5), this.getZ() + (double) h, this.random.nextFloat() * 360.0f, 0.0f);
                this.getWorld().spawnEntity(slimeEntity);
                slimeEntity.setVelocity(g * 0.5, 0.35 * getRandom().nextDouble(), h * 0.5);
            }
        }
        this.setRemoved(reason);
        this.brain.forgetAll();
    }

    @Override
    protected ParticleEffect getParticles() {
        return ParticleRegistry.WATER_SLIME;
    }
}
