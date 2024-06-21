package de.takacick.onescaryblock.registry.entity.living;

import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.entity.projectile.HerobrineLightningProjectileEntity;
import de.takacick.onescaryblock.utils.datatracker.HerobrineLightningDamageHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HerobrineEntity extends HostileEntity implements RangedAttackMob {

    private final ServerBossBar bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS);

    public HerobrineEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);

        this.setHealth(this.getMaxHealth());
        this.getNavigation().setCanSwim(true);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.43f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100);
    }

    @Override
    public void tick() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        super.tick();
    }


    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.LIGHTNING_BOLT) || source.isOf(HerobrineLightningDamageHelper.HEROBRINE_LIGHTNING)) {
            return false;
        }

        return super.damage(source, amount);
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {

        dropItem(ItemRegistry.HEROBRINE_LIGHTNING_BOLT);

        super.dropLoot(damageSource, causedByPlayer);
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
    protected void initGoals() {
        this.goalSelector.add(1, new TemptGoal(this, 1.0 * 0.5f, Ingredient.ofItems(Items.POPPY), false));
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.0 * 0.75f, 100, 20.0f));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0 * 0.75f));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
    }


    @Override
    public void tickMovement() {
        int d;
        int entity;
        LivingEntity entity2;
        Vec3d vec3d = this.getVelocity().multiply(1.0, 0.1, 1.0);
        Vec3d lookTarget = vec3d;
        if (!this.getWorld().isClient
                && (entity2 = getTarget()) != null) {
            double d2 = vec3d.y;
            if (this.getY() < entity2.getY() || this.getY() < entity2.getY() + 2.7) {
                d2 = Math.max(0.0, d2);
                d2 += 0.3 - d2 * (double) 0.6f;
            }
            vec3d = new Vec3d(vec3d.x, d2, vec3d.z);
            Vec3d vec3d2 = new Vec3d(entity2.getX() - this.getX(), 0.0, entity2.getZ() - this.getZ());
            lookTarget = vec3d2;
            if (vec3d2.horizontalLengthSquared() > 45.0) {
                Vec3d vec3d3 = vec3d2.normalize();
                vec3d = vec3d.add(vec3d3.x * 0.3 - vec3d.x * 0.6, 0.0, vec3d3.z * 0.3 - vec3d.z * 0.6);
            }
        }

        this.setVelocity(vec3d);
        if (vec3d.horizontalLengthSquared() > 0.05) {
            this.setYaw((float) MathHelper.atan2(lookTarget.z, lookTarget.x) * 57.295776f - 90.0f);
        }
        super.tickMovement();
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        World world = getWorld();
        world.playSoundFromEntity(null, this, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1f, 3f);
        world.playSoundFromEntity(null, this, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 3f);

        HerobrineLightningProjectileEntity herobrineLightningProjectileEntity = new HerobrineLightningProjectileEntity(world, this);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - herobrineLightningProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        herobrineLightningProjectileEntity.setVelocity(d, e + g * (double) 0.2f, f, 1.6f, 14 - this.getWorld().getDifficulty().getId() * 4);
        this.getWorld().spawnEntity(herobrineLightningProjectileEntity);
    }
}