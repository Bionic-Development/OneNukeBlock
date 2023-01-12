package de.takacick.immortalmobs.registry.entity.dragon;

import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalEndCrystalEntity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ImmortalEnderDragonEntity extends CustomDragonEntity {

    @Nullable
    public ImmortalEndCrystalEntity connectedCrystal;
    private PlayerEntity playerEntity = null;

    public ImmortalEnderDragonEntity(EntityType<? extends ImmortalEnderDragonEntity> entityType, World world) {
        super(entityType, world);
    }

    public static ImmortalEnderDragonEntity create(EntityType<ImmortalEnderDragonEntity> entityType, World world) {
        return new ImmortalEnderDragonEntity(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createEnderDragonAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0D);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public void setFireTicks(int ticks) {

    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.ADD_DEATH_PARTICLES) {
            for (int i = 0; i < 20; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.world.addParticle(ParticleRegistry.IMMORTAL_POOF, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public void setOnFireFromLava() {

    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return super.damage(source, amount / 20f);
    }

    public void tickWithEndCrystals() {
        if (this.connectedCrystal != null) {
            if (this.connectedCrystal.isRemoved()) {
                this.connectedCrystal = null;
            } else if (this.age % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.random.nextInt(10) == 0) {
            List<ImmortalEndCrystalEntity> list = this.world.getNonSpectatingEntities(ImmortalEndCrystalEntity.class, this.getBoundingBox().expand(32.0));
            ImmortalEndCrystalEntity endCrystalEntity = null;
            double d = Double.MAX_VALUE;
            for (ImmortalEndCrystalEntity endCrystalEntity2 : list) {
                double e = endCrystalEntity2.squaredDistanceTo(this);
                if (!(e < d)) continue;
                d = e;
                endCrystalEntity = endCrystalEntity2;
            }
            this.connectedCrystal = endCrystalEntity;
        }
    }

    public void tickWithEndCrystals(PlayerEntity player) {
        if (player.age % 10 == 0) {
            List<ImmortalEndCrystalEntity> list = this.world.getNonSpectatingEntities(ImmortalEndCrystalEntity.class, player.getBoundingBox().expand(32.0));
            ImmortalEndCrystalEntity endCrystalEntity = null;
            double d = Double.MAX_VALUE;
            for (ImmortalEndCrystalEntity endCrystalEntity2 : list) {
                double e = endCrystalEntity2.squaredDistanceTo(player);
                if (!(e < d)) continue;
                d = e;
                endCrystalEntity = endCrystalEntity2;
            }
            this.connectedCrystal = endCrystalEntity;
            if (connectedCrystal != null && player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getHealth() + 1.0f);
            }
        }
    }

    public void setPlayerEntity(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }
}
