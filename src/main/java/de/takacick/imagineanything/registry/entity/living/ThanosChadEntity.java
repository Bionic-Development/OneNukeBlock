package de.takacick.imagineanything.registry.entity.living;

import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.entity.projectiles.ThanosShotEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ThanosChadEntity extends HostileEntity implements RangedAttackMob {

    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(this.getDisplayName(), BossBar.Color.PINK, BossBar.Style.PROGRESS).setDarkenSky(true);

    public ThanosChadEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        dropItem(ItemRegistry.INFINITY_GAUNTLET_DISABLED);
        super.onDeath(damageSource);
    }

    @Override
    public void tick() {

        if (!world.isClient) {
            this.bossBar.setPercent(getHealth() / getMaxHealth());
        }

        super.tick();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ProjectileAttackGoal(this, 1.0, 40, 80, 12.0f));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.targetSelector.add(0, new RevengeGoal(this, new Class[0]).setGroupRevenge(PlayerEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
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

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.0f, 2.0f);
        Vec3d vec3d = target.getVelocity();
        double d = target.getX() + vec3d.x - this.getX();
        double e = target.getBodyY(0.25) - (double) 0.2f - this.getY();
        double f = target.getZ() + vec3d.z - this.getZ();

        ThanosShotEntity thanosShotEntity = new ThanosShotEntity(world, getX(), getBodyY(0.78f), getZ(), this);
        thanosShotEntity.setVelocity(d, e, f, 1.75f, 1.3f);
        world.spawnEntity(thanosShotEntity);
    }
}