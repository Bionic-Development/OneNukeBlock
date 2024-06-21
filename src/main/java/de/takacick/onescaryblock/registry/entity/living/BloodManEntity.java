package de.takacick.onescaryblock.registry.entity.living;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.registry.EffectRegistry;
import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BloodManEntity extends HostileEntity {

    boolean targetingUnderwater;
    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;

    public BloodManEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setStepHeight(1.0f);
        this.moveControl = new BloodManEntity.DrownedMoveControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.43f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new BloodManEntity.WanderAroundOnSurfaceGoal(this, 1.0));
        this.goalSelector.add(2, new BloodManEntity.DrownedAttackGoal(this, 1.0, false));
        this.goalSelector.add(5, new BloodManEntity.LeaveWaterGoal(this, 1.0));
        this.goalSelector.add(6, new BloodManEntity.TargetAboveWaterGoal(this, 1.0, this.getWorld().getSeaLevel()));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0));
        this.targetSelector.add(1, new RevengeGoal(this, BloodManEntity.class).setGroupRevenge(ZombifiedPiglinEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::canDrownedAttackTarget));
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (!getWorld().isClient) {
            BionicUtils.sendEntityStatus(getWorld(), this, OneScaryBlock.IDENTIFIER, 1);
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);

        if (bl && target instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
        }

        return bl;
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {

        dropItem(ItemRegistry.BLOOD_BUCKET);

        super.dropLoot(damageSource, causedByPlayer);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        if (this.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty() && world.getRandom().nextFloat() < 0.03f) {
            this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.NAUTILUS_SHELL));
            this.updateDropChances(EquipmentSlot.OFFHAND);
        }
        return entityData;
    }

    public static boolean canSpawn(EntityType<BloodManEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        boolean bl;
        if (!world.getFluidState(pos.down()).isIn(FluidTags.WATER) && !SpawnReason.isAnySpawner(spawnReason)) {
            return false;
        }
        RegistryEntry<Biome> registryEntry = world.getBiome(pos);
        boolean bl2 = bl = !(world.getDifficulty() == Difficulty.PEACEFUL || !SpawnReason.isTrialSpawner(spawnReason) && !BloodManEntity.isSpawnDark(world, pos, random) || !SpawnReason.isAnySpawner(spawnReason) && !world.getFluidState(pos).isIn(FluidTags.WATER));
        if (bl && SpawnReason.isAnySpawner(spawnReason)) {
            return true;
        }
        if (registryEntry.isIn(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
            return random.nextInt(15) == 0 && bl;
        }
        return random.nextInt(40) == 0 && BloodManEntity.isValidSpawnDepth(world, pos) && bl;
    }

    private static boolean isValidSpawnDepth(WorldAccess world, BlockPos pos) {
        return pos.getY() < world.getSeaLevel() - 5;
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {

    }

    public boolean canDrownedAttackTarget(@Nullable LivingEntity target) {
        if (target != null) {
            return !this.getWorld().isDay() || target.isTouchingWater();
        }
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return !this.isSwimming();
    }

    boolean isTargetingUnderwater() {
        if (this.targetingUnderwater) {
            return true;
        }
        LivingEntity livingEntity = this.getTarget();
        return livingEntity != null && livingEntity.isTouchingWater();
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater() && this.isTargetingUnderwater()) {
            this.updateVelocity(0.01f, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    public void updateSwimming() {
        if (!this.getWorld().isClient) {
            if (this.canMoveVoluntarily() && this.isTouchingWater() && this.isTargetingUnderwater()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.landNavigation;
                this.setSwimming(false);
            }
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);

        if (!getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.discard();
        }
    }

    @Override
    public boolean isInSwimmingPose() {
        return this.isSwimming();
    }

    protected boolean hasFinishedCurrentPath() {
        double d;
        BlockPos blockPos;
        Path path = this.getNavigation().getCurrentPath();
        return path != null && (blockPos = path.getTarget()) != null && (d = this.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ())) < 4.0;
    }

    public void setTargetingUnderwater(boolean targetingUnderwater) {
        this.targetingUnderwater = targetingUnderwater;
    }

    static class DrownedMoveControl
            extends MoveControl {
        private final BloodManEntity drowned;

        public DrownedMoveControl(BloodManEntity drowned) {
            super(drowned);
            this.drowned = drowned;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.drowned.getTarget();
            if (this.drowned.isTargetingUnderwater() && this.drowned.isTouchingWater()) {
                if (livingEntity != null && livingEntity.getY() > this.drowned.getY() || this.drowned.targetingUnderwater) {
                    this.drowned.setVelocity(this.drowned.getVelocity().add(0.0, 0.002, 0.0));
                }
                if (this.state != MoveControl.State.MOVE_TO || this.drowned.getNavigation().isIdle()) {
                    this.drowned.setMovementSpeed(0.0f);
                    return;
                }
                double d = this.targetX - this.drowned.getX();
                double e = this.targetY - this.drowned.getY();
                double f = this.targetZ - this.drowned.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f;
                this.drowned.setYaw(this.wrapDegrees(this.drowned.getYaw(), h, 90.0f));
                this.drowned.bodyYaw = this.drowned.getYaw();
                float i = (float) (this.speed * this.drowned.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(1f, this.drowned.getMovementSpeed(), i * 3f);
                this.drowned.setMovementSpeed(j);
                this.drowned.setVelocity(this.drowned.getVelocity().add((double) j * d * 0.005, (double) j * e * 0.1, (double) j * f * 0.005));
            } else {
                if (!this.drowned.isOnGround()) {
                    this.drowned.setVelocity(this.drowned.getVelocity().add(0.0, -0.008, 0.0));
                }
                super.tick();
            }
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 60) {
            for (int i = 0; i < 150; ++i) {
                double d = this.random.nextGaussian() * 0.4;
                double e = this.random.nextGaussian() * 0.4;
                double f = this.random.nextGaussian() * 0.4;
                this.getWorld().addParticle(ParticleRegistry.FALLING_BLOOD, true, getX() + d, this.getBodyY(0.5 + e), getZ() + f, d * 0.2, e * 0.2, f * 0.2);
            }
        } else {
            super.handleStatus(status);
        }
    }

    static class WanderAroundOnSurfaceGoal
            extends Goal {
        private final PathAwareEntity mob;
        private double x;
        private double y;
        private double z;
        private final double speed;
        private final World world;

        public WanderAroundOnSurfaceGoal(PathAwareEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.world = mob.getWorld();
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (!this.world.isDay()) {
                return false;
            }
            if (this.mob.isTouchingWater()) {
                return false;
            }
            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            }
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            return true;
        }

        @Override
        public boolean shouldContinue() {
            return !this.mob.getNavigation().isIdle();
        }

        @Override
        public void start() {
            this.mob.getNavigation().startMovingTo(this.x, this.y, this.z, this.speed);
        }

        @Nullable
        private Vec3d getWanderTarget() {
            Random random = this.mob.getRandom();
            BlockPos blockPos = this.mob.getBlockPos();
            for (int i = 0; i < 10; ++i) {
                BlockPos blockPos2 = blockPos.add(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (!this.world.getBlockState(blockPos2).isOf(Blocks.WATER)) continue;
                return Vec3d.ofBottomCenter(blockPos2);
            }
            return null;
        }
    }

    static class DrownedAttackGoal
            extends MeleeAttackGoal {
        private final BloodManEntity drowned;

        public DrownedAttackGoal(BloodManEntity drowned, double speed, boolean pauseWhenMobIdle) {
            super(drowned, speed, pauseWhenMobIdle);
            this.drowned = drowned;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && this.drowned.canDrownedAttackTarget(this.drowned.getTarget());
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && this.drowned.canDrownedAttackTarget(this.drowned.getTarget());
        }
    }

    static class LeaveWaterGoal
            extends MoveToTargetPosGoal {
        private final BloodManEntity drowned;

        public LeaveWaterGoal(BloodManEntity drowned, double speed) {
            super(drowned, speed, 8, 2);
            this.drowned = drowned;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.drowned.getWorld().isDay() && this.drowned.isTouchingWater() && this.drowned.getY() >= (double) (this.drowned.getWorld().getSeaLevel() - 3);
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue();
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockPos blockPos = pos.up();
            if (!world.isAir(blockPos) || !world.isAir(blockPos.up())) {
                return false;
            }
            return world.getBlockState(pos).hasSolidTopSurface(world, pos, this.drowned);
        }

        @Override
        public void start() {
            this.drowned.setTargetingUnderwater(false);
            this.drowned.navigation = this.drowned.landNavigation;
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    static class TargetAboveWaterGoal
            extends Goal {
        private final BloodManEntity drowned;
        private final double speed;
        private final int minY;
        private boolean foundTarget;

        public TargetAboveWaterGoal(BloodManEntity drowned, double speed, int minY) {
            this.drowned = drowned;
            this.speed = speed;
            this.minY = minY;
        }

        @Override
        public boolean canStart() {
            return !this.drowned.getWorld().isDay() && this.drowned.isTouchingWater() && this.drowned.getY() < (double) (this.minY - 2);
        }

        @Override
        public boolean shouldContinue() {
            return this.canStart() && !this.foundTarget;
        }

        @Override
        public void tick() {
            if (this.drowned.getY() < (double) (this.minY - 1) && (this.drowned.getNavigation().isIdle() || this.drowned.hasFinishedCurrentPath())) {
                Vec3d vec3d = NoPenaltyTargeting.findTo(this.drowned, 4, 8, new Vec3d(this.drowned.getX(), this.minY - 1, this.drowned.getZ()), 1.5707963705062866);
                if (vec3d == null) {
                    this.foundTarget = true;
                    return;
                }
                this.drowned.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }
        }

        @Override
        public void start() {
            this.drowned.setTargetingUnderwater(true);
            this.foundTarget = false;
        }

        @Override
        public void stop() {
            this.drowned.setTargetingUnderwater(false);
        }
    }
}