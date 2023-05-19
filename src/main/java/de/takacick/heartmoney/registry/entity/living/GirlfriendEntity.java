package de.takacick.heartmoney.registry.entity.living;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.particles.ColoredParticleEffect;
import de.takacick.utils.BionicUtilsClient;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class GirlfriendEntity extends TameableEntity {

    private static final TrackedData<Integer> SKIN = BionicDataTracker.registerData(new Identifier(HeartMoney.MOD_ID, "girlfriend_skin"), TrackedDataHandlerRegistry.INTEGER);

    public GirlfriendEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return TameableEntity.createMobAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.5)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        getDataTracker().startTracking(SKIN, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.2D, 7.0f, 2.0f, false));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, true));
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return target.distanceTo(this) <= 7 && super.canTarget(target);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("girlfriendSkin", getSkin());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        setSkin(nbt.getInt("girlfriendSkin"));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {

        setSkin(world.getRandom().nextBetween(0, 4));


        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (world.isClient) {
            world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, getSoundCategory(), 1.0f, 6.0f, false);
            world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, getSoundCategory(), 0.5f, 6.0f, false);

            for (int i = 0; i < 35; ++i) {
                double g = getX();
                double h = getBodyY(0.5);
                double j = getZ();
                int color = BionicUtilsClient.getRainbow().getColorAsInt(world.random.nextInt(601));
                world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(color))), true, g, h, j, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextGaussian() * 0.15);
            }
            for (int i = 0; i < 15; ++i) {
                double g = getX();
                double h = getBodyY(0.5);
                double j = getZ();
                int color = BionicUtilsClient.getRainbow().getColorAsInt(world.random.nextInt(601));
                world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, new Vec3f(Vec3d.unpackRgb(color))), true, g, h, j, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3, world.getRandom().nextGaussian() * 0.3);
            }
            for (int i = 0; i < 8; ++i) {
                double d = world.getRandom().nextGaussian() * 0.02;
                double e = world.getRandom().nextGaussian() * 0.02;
                double f = world.getRandom().nextGaussian() * 0.02;
                int color = BionicUtilsClient.getRainbow().getColorAsInt(world.random.nextInt(601));
                world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(color))), getParticleX(1.0), getRandomBodyY(), getParticleZ(1.0), d, e, f);
            }
        } else {
            for (int i = 0; i < getRandom().nextBetween(30, 50); i++) {
                ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.HEART.getDefaultStack(),
                        world.getRandom().nextGaussian() * 0.275, world.getRandom().nextDouble() * 0.375, world.getRandom().nextGaussian() * 0.275);
                world.spawnEntity(itemEntity);
            }
        }

        this.setTamed(false);
        this.setOwnerUuid(null);

        super.onDeath(damageSource);
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 60) {
            for (int i = 0; i < 15; ++i) {
                double d = world.getRandom().nextGaussian() * 0.02;
                double e = world.getRandom().nextGaussian() * 0.02;
                double f = world.getRandom().nextGaussian() * 0.02;
                int color = BionicUtilsClient.getRainbow().getColorAsInt(world.random.nextInt(601));
                world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(color))), getParticleX(1.0), getRandomBodyY(), getParticleZ(1.0), d, e, f);
            }
            world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, getSoundCategory(), 1.0f, 6.0f, false);
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public void tick() {
        tickHandSwing();

        super.tick();
    }

    @Override
    public void setOwner(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerUuid(player.getUuid());
    }

    public void setSkin(int skin) {
        getDataTracker().set(SKIN, skin);
    }

    public int getSkin() {
        return getDataTracker().get(SKIN);
    }

    public static class FollowOwnerGoal extends Goal {
        private final GirlfriendEntity girlfriendEntity;
        private final WorldView world;
        private final double speed;
        private final EntityNavigation navigation;
        private final float maxDistance;
        private final float minDistance;
        private final boolean leavesAllowed;
        private Entity owner;
        private int updateCountdownTicks;
        private float oldWaterPathfindingPenalty;

        public FollowOwnerGoal(GirlfriendEntity girlfriendEntity, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
            this.girlfriendEntity = girlfriendEntity;
            this.world = girlfriendEntity.world;
            this.speed = speed;
            this.navigation = girlfriendEntity.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.leavesAllowed = leavesAllowed;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
            if (!(girlfriendEntity.getNavigation() instanceof MobNavigation) && !(girlfriendEntity.getNavigation() instanceof BirdNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canStart() {
            this.owner = girlfriendEntity.getOwner();
            if (owner == null) {
                return false;
            } else if (owner.isSpectator()) {
                return false;
            } else if (this.girlfriendEntity.squaredDistanceTo(owner) < (double) (this.minDistance * this.minDistance)) {
                return true;
            } else {
                return true;
            }
        }

        public boolean shouldContinue() {
            if (this.navigation.isIdle()) {
                return false;
            } else {
                return !(this.girlfriendEntity.squaredDistanceTo(this.owner) <= (double) (this.maxDistance * this.maxDistance));
            }
        }

        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.girlfriendEntity.getPathfindingPenalty(PathNodeType.WATER);
            this.girlfriendEntity.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.navigation.stop();
            this.girlfriendEntity.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
            this.girlfriendEntity.getLookControl().lookAt(this.owner, 20.0F, (float) this.girlfriendEntity.getMaxLookPitchChange());
        }

        public void tick() {

            this.girlfriendEntity.getLookControl().lookAt(this.owner, 10.0F, (float) this.girlfriendEntity.getMaxLookPitchChange());
            if (--this.updateCountdownTicks <= 0) {
                this.updateCountdownTicks = 10;
                if (!this.girlfriendEntity.isLeashed() && !this.girlfriendEntity.hasVehicle()) {
                    if (this.girlfriendEntity.squaredDistanceTo(this.owner) >= 144.0D) {
                        this.tryTeleport();
                    } else {
                        this.navigation.startMovingTo(this.owner, this.speed);
                    }
                }
            }
        }

        private void tryTeleport() {
            BlockPos blockPos = this.owner.getBlockPos();

            for (int i = 0; i < 10; ++i) {
                int j = this.getRandomInt(-3, 3);
                int k = this.getRandomInt(-1, 1);
                int l = this.getRandomInt(-3, 3);
                boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
                if (bl) {
                    return;
                }
            }
        }

        private boolean tryTeleportTo(int x, int y, int z) {
            if (Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D) {
                return false;
            } else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.girlfriendEntity.refreshPositionAndAngles((double) x + 0.5D, (double) y, (double) z + 0.5D, this.girlfriendEntity.getYaw(), this.girlfriendEntity.getPitch());
                this.navigation.stop();
                return true;
            }
        }

        private boolean canTeleportTo(BlockPos pos) {
            PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
            if (pathNodeType != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockState = this.world.getBlockState(pos.down());
                if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockPos = pos.subtract(this.girlfriendEntity.getBlockPos());
                    return this.world.isSpaceEmpty(this.girlfriendEntity, this.girlfriendEntity.getBoundingBox().offset(blockPos));
                }
            }
        }

        private int getRandomInt(int min, int max) {
            return this.girlfriendEntity.getRandom().nextInt(max - min + 1) + min;
        }
    }
}