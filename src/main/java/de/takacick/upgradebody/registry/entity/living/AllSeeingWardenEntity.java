package de.takacick.upgradebody.registry.entity.living;

import com.mojang.serialization.Dynamic;
import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.ExperienceOrbProperties;
import de.takacick.upgradebody.registry.entity.living.ai.AllSeeingWardenBrain;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AllSeeingWardenEntity extends WardenEntity implements Upgraded {
    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.of("§3All-Seeing Warden §a[§7Upgrade§a]"), BossBar.Color.BLUE, BossBar.Style.PROGRESS).setDarkenSky(true);

    public AllSeeingWardenEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {

        if (!getWorld().isClient) {
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        }

        super.tick();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 120.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return AllSeeingWardenBrain.create(this);
    }

    @Override
    public Brain<WardenEntity> getBrain() {
        return super.getBrain();
    }

    @Override
    public boolean isValidTarget(@Nullable Entity entity) {
        return entity instanceof PlayerEntity && super.isValidTarget(entity);
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!getWorld().isClient) {

            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, UpgradeBody.IDENTIFIER, 7);
            for (int i = getRandom().nextBetween(5000, 6000); i > 0; ) {
                int level = MathHelper.clamp(getRandom().nextBetween(100, 200), 0, i);
                i -= level;

                ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(getWorld(), getX(), getBodyY(0.5), getZ(), level);
                ((ExperienceOrbProperties) experienceOrbEntity).setLevelOrb(true);
                ((ExperienceOrbProperties) experienceOrbEntity).setCooldown(5);
                experienceOrbEntity.setVelocity(getRandom().nextGaussian() * 0.15, getRandom().nextDouble() * 0.25, getRandom().nextGaussian() * 0.15);
                getWorld().spawnEntity(experienceOrbEntity);
            }
        }

        super.onDeath(damageSource);
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.equals(SoundEvents.ENTITY_WARDEN_TENDRIL_CLICKS) || sound.equals(SoundEvents.ENTITY_WARDEN_HEARTBEAT)) {
            return;
        }

        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }
}