package de.takacick.illegalwars.registry.entity.living;

import com.mojang.serialization.Dynamic;
import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.entity.living.brain.CyberWardenSecurityBrain;
import de.takacick.illegalwars.registry.entity.projectiles.CyberLaserEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CyberWardenSecurityEntity extends WardenEntity {

    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.translatable(getType().getTranslationKey()), IllegalWars.LIGHT_BLUE_BAR, BossBar.Style.PROGRESS).setDarkenSky(true);
    private static final TrackedData<Integer> CHARGE = DataTracker.registerData(CyberWardenSecurityEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int laserTicks = 0;
    private int cooldown = 0;
    private int charge = 0;

    public CyberWardenSecurityEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(CHARGE, -5);

        super.initDataTracker();
    }

    @Override
    public void tick() {

        if (!getWorld().isClient) {
            if (this.cooldown > 0) {
                this.cooldown--;
            } else if (this.charge < 0) {
                this.charge = 0;
            }
        }

        if (this.charge >= 0) {
            this.charge = MathHelper.clamp(this.charge + 2, 0, 100);
        }

        if (!getWorld().isClient) {
            setCharge(this.charge);
        } else {
            if (Math.abs(this.charge - getCharge()) > 2) {
                this.charge = getCharge();
            }
        }

        if (!getWorld().isClient) {
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

            if (getChargeProgress(1f) >= 1f && this.laserTicks <= 0) {
                this.laserTicks = getRandom().nextBetween(40, 60);
            }

            if (this.laserTicks > 0 && isAlive()) {
                if (getTarget() != null) {
                    this.laserTicks--;

                    Vec3d pos = new Vec3d(getX() + getRandom().nextGaussian() * 0.15, getY() + this.getHeight() * 0.63829787234f + getRandom().nextGaussian() * 0.15, getZ() + getRandom().nextGaussian() * 0.15);
                    Vec3d vec3d = getTarget().getPos().add(getRandom().nextGaussian() * 0.5, getRandom().nextGaussian() * 0.5, getRandom().nextGaussian() * 0.5).subtract(pos);
                    CyberLaserEntity cyberLaserEntity = new CyberLaserEntity(this.getWorld(), this,
                            vec3d.getX(), vec3d.getY(), vec3d.getZ());
                    cyberLaserEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    this.getWorld().spawnEntity(cyberLaserEntity);
                    this.getWorld().playSound(null, cyberLaserEntity.getX(), cyberLaserEntity.getY(), cyberLaserEntity.getZ(),
                            SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), cyberLaserEntity.getSoundCategory(), 1.2f, 1.8f);
                }

                if (this.laserTicks <= 0) {
                    this.cooldown = getRandom().nextBetween(20, 40);
                    this.charge = -5;
                    setCharge(-5);
                }
            }
        }

        super.tick();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return CyberWardenSecurityBrain.create(this);
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
    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound.equals(SoundEvents.ENTITY_WARDEN_TENDRIL_CLICKS) || sound.equals(SoundEvents.ENTITY_WARDEN_HEARTBEAT)) {
            return;
        }

        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!getWorld().isClient) {
            for (int i = 0; i < getRandom().nextBetween(12, 16); i++) {
                dropItem(Items.DIAMOND_BLOCK);
            }

            for (int i = 0; i < getRandom().nextBetween(12, 16); i++) {
                dropItem(Items.IRON_BLOCK);
            }
        }

        super.onDeath(damageSource);
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

    public void setCharge(int charge) {
        getDataTracker().set(CHARGE, charge);
    }

    public int getCharge() {
        return getDataTracker().get(CHARGE);
    }

    public float getChargeProgress(float tickDelta) {
        return Math.min(MathHelper.getLerpProgress(this.charge + tickDelta, 0, 100f), 1f);
    }
}