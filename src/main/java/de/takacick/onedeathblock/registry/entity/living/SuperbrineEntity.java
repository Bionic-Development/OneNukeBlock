package de.takacick.onedeathblock.registry.entity.living;

import de.takacick.onedeathblock.access.PlayerProperties;
import de.takacick.onedeathblock.server.SuperbrineExplosionHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

public class SuperbrineEntity extends HostileEntity {

    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(this.getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(false);
    private int lightningTicks = 0;
    private int passengerTicks = 15;
    public int deathTick = 0;

    public SuperbrineEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 120.0);
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

            if (this.lightningTicks > 0) {
                this.lightningTicks--;
                for (int i = 0; i < world.getRandom().nextBetween(1, 2); i++) {
                    LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                    lightningEntity.setPos(getX() + world.getRandom().nextGaussian() * 3, getY() + world.getRandom().nextGaussian() * 3, getZ() + world.getRandom().nextGaussian() * 3);
                    world.spawnEntity(lightningEntity);
                }
            }

            if (hasPassengers()) {
                this.passengerTicks--;
                getPassengerList().forEach(passenger -> {
                    if (passenger instanceof PlayerProperties playerProperties) {
                        playerProperties.resetDamageDelay();
                    }
                    passenger.damage(DamageSource.mob(this), 1);
                });

                if (this.passengerTicks <= 0) {
                    world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, getSoundCategory(), 0.15f, 1f);
                    swingHand(Hand.MAIN_HAND, true);
                    removeAllPassengers();
                    updateGoalControls();
                    if (world.getRandom().nextDouble() <= 0.2) {
                        this.teleportRandomly();
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!this.world.isClient) {
            this.world.getPlayers().forEach(playerEntity -> {
                playerEntity.sendMessage(Text.of("<Herobrine> You may have defeated me, but you won't surive my §cself destruction power§f!"));
            });
        } else {
            world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_ENDERMAN_STARE, getSoundCategory(), 1f, 1f, false);
        }

        super.onDeath(damageSource);
    }

    @Override
    protected void updatePostDeath() {

        setPos(getX(), getY() + 0.01, getZ());

        this.hurtTime = 0;
        setVelocity(0, 0, 0);

        world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_ENDERMAN_SCREAM, getSoundCategory(), 1f, 1f, false);

        ++this.deathTick;
        if (this.deathTick == 60 && !this.world.isClient()) {
            this.remove(Entity.RemovalReason.KILLED);
            SuperbrineExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                    getX(), getBodyY(0.5), getZ(), (float) 32, false,
                    Explosion.DestructionType.DESTROY);

            for (int i = 0; i < 15; i++) {
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                lightningEntity.setPos(getX() + world.getRandom().nextGaussian() * 5, getY() + world.getRandom().nextGaussian() * 5, getZ() + world.getRandom().nextGaussian() * 5);
                world.spawnEntity(lightningEntity);
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);

        passenger.setVelocity(getRotationVector().multiply(0.5, 0.5, 0.5).add(0, 0.2, 0));
        passenger.velocityDirty = true;
        passenger.velocityModified = true;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.3, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.1));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
    }

    @Override
    protected void updateGoalControls() {
        boolean bl = !(this.getFirstPassenger() instanceof MobEntity);
        boolean bl2 = !(this.getVehicle() instanceof BoatEntity);

        this.goalSelector.setControlEnabled(Goal.Control.MOVE, bl);
        this.goalSelector.setControlEnabled(Goal.Control.JUMP, bl && bl2);
        this.goalSelector.setControlEnabled(Goal.Control.LOOK, true);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!hasPassengers()) {
            target.startRiding(this, true);
            this.passengerTicks = world.getRandom().nextBetween(15, 20);
            updateGoalControls();
        }
        return false;
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        this.updatePassengerPosition(passenger, Entity::setPosition);
    }

    private void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        if (!this.hasPassenger(passenger)) {
            return;
        }
        Vec3d rotation = getRotationVector(0, getBodyYaw()).multiply(getWidth() * 0.9);

        this.setBodyYaw(getYaw());

        double d = getBodyY(0.2) + (Math.max(getHeight() - 2.0, 0) * 0.25) + passenger.getHeightOffset();
        positionUpdater.accept(passenger, this.getX() + rotation.getX(), d, this.getZ() + rotation.getZ());

        passenger.setYaw(getYaw() - 180);
        passenger.setHeadYaw(getYaw() - 180);
        passenger.setBodyYaw(getYaw() - 180);
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return (target instanceof PlayerEntity) && super.canTarget(target);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.equals(DamageSource.LIGHTNING_BOLT)) {
            return false;
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        }

        boolean bl2 = super.damage(source, amount);
        if (!this.world.isClient() && !(source.getAttacker() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
            removeAllPassengers();
            this.teleportRandomly();
        }

        return bl2;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
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
    public void playSpawnEffects() {
        this.lightningTicks = 20;
    }

    protected boolean teleportRandomly() {
        if (this.world.isClient() || !this.isAlive()) {
            return false;
        }
        double d = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        double e = this.getY() + (double) (this.random.nextInt(64) - 32);
        double f = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d, e, f);
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.world.getBottomY() && !this.world.getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.world.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        Vec3d vec3d = this.getPos();
        boolean bl3 = this.teleport(x, y, z, true);
        if (bl3) {
            this.world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(this));
            if (!this.isSilent()) {
                this.world.playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0f, 1.0f);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        }
        return bl3;
    }

}