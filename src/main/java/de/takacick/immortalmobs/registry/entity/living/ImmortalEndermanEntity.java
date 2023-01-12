package de.takacick.immortalmobs.registry.entity.living;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalFireworkEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class ImmortalEndermanEntity
        extends HostileEntity
        implements Angerable, ImmortalEntity {
    private static final UUID ATTACKING_SPEED_BOOST_ID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final EntityAttributeModifier ATTACKING_SPEED_BOOST = new EntityAttributeModifier(ATTACKING_SPEED_BOOST_ID, "Attacking speed boost", 0.15f, EntityAttributeModifier.Operation.ADDITION);
    private static final TrackedData<Optional<BlockState>> CARRIED_BLOCK = DataTracker.registerData(ImmortalEndermanEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE);
    private static final TrackedData<Boolean> ANGRY = DataTracker.registerData(ImmortalEndermanEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> PROVOKED = DataTracker.registerData(ImmortalEndermanEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int lastAngrySoundAge = Integer.MIN_VALUE;
    private int ageWhenTargetSet;
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    private UUID angryAt;
    private int angerTime;

    public ImmortalEndermanEntity(EntityType<? extends ImmortalEndermanEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ImmortalEndermanEntity.ChasePlayerGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal((PathAwareEntity) this, 1.0, 0.0f));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(10, new ImmortalEndermanEntity.PlaceBlockGoal(this));
        this.goalSelector.add(11, new ImmortalEndermanEntity.PickUpBlockGoal(this));
        this.targetSelector.add(1, new ImmortalEndermanEntity.TeleportTowardsPlayerGoal(this, this::shouldAngerAt));
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, EndermiteEntity.class, true, false));
        this.targetSelector.add(4, new UniversalAngerGoal<>(this, false));
    }

    public static DefaultAttributeContainer.Builder createEndermanAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (target == null) {
            this.ageWhenTargetSet = 0;
            this.dataTracker.set(ANGRY, false);
            this.dataTracker.set(PROVOKED, false);
            entityAttributeInstance.removeModifier(ATTACKING_SPEED_BOOST);
        } else {
            this.ageWhenTargetSet = this.age;
            this.dataTracker.set(ANGRY, true);
            if (!entityAttributeInstance.hasModifier(ATTACKING_SPEED_BOOST)) {
                entityAttributeInstance.addTemporaryModifier(ATTACKING_SPEED_BOOST);
            }
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CARRIED_BLOCK, Optional.empty());
        this.dataTracker.startTracking(ANGRY, false);
        this.dataTracker.startTracking(PROVOKED, false);
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    public void playAngrySound() {
        if (this.age >= this.lastAngrySoundAge + 400) {
            this.lastAngrySoundAge = this.age;
            if (!this.isSilent()) {
                this.world.playSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ENTITY_ENDERMAN_STARE, this.getSoundCategory(), 2.5f, 1.0f, false);
            }
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (ANGRY.equals(data) && this.isProvoked() && this.world.isClient) {
            this.playAngrySound();
        }
        super.onTrackedDataSet(data);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        BlockState blockState = this.getCarriedBlock();
        if (blockState != null) {
            nbt.put("carriedBlockState", NbtHelper.fromBlockState(blockState));
        }
        this.writeAngerToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        BlockState blockState = null;
        if (nbt.contains("carriedBlockState", NbtElement.COMPOUND_TYPE) && (blockState = NbtHelper.toBlockState(nbt.getCompound("carriedBlockState"))).isAir()) {
            blockState = null;
        }
        this.setCarriedBlock(blockState);
        this.readAngerFromNbt(this.world, nbt);
    }

    boolean isPlayerStaring(PlayerEntity player) {
        ItemStack itemStack = player.getInventory().armor.get(3);
        if (itemStack.isOf(Blocks.CARVED_PUMPKIN.asItem())) {
            return false;
        }
        Vec3d vec3d = player.getRotationVec(1.0f).normalize();
        Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
        double d = vec3d2.length();
        double e = vec3d.dotProduct(vec3d2 = vec3d2.normalize());
        if (e > 1.0 - 0.025 / d) {
            return player.canSee(this);
        }
        return false;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 2.55f;
    }

    @Override
    public void tickMovement() {
        if (this.world.isClient) {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleRegistry.IMMORTAL_PORTAL, this.getParticleX(0.5), this.getRandomBodyY() - 0.25, this.getParticleZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
            }
        }
        this.jumping = false;
        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld) this.world, true);
        }
        super.tickMovement();
    }

    @Override
    public boolean hurtByWater() {
        return false;
    }

    @Override
    protected void mobTick() {
        float f;
        if (this.world.isDay() && this.age >= this.ageWhenTargetSet + 600 && (f = this.getBrightnessAtEyes()) > 0.5f && this.world.isSkyVisible(this.getBlockPos()) && this.random.nextFloat() * 30.0f < (f - 0.4f) * 2.0f) {
            this.setTarget(null);
            this.teleportRandomly();
        }
        super.mobTick();
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

    boolean teleportTo(Entity entity) {
        Vec3d vec3d = new Vec3d(this.getX() - entity.getX(), this.getBodyY(0.5) - entity.getEyeY(), this.getZ() - entity.getZ());
        vec3d = vec3d.normalize();
        double d = 16.0;
        double e = this.getX() + (this.random.nextDouble() - 0.5) * 8.0 - vec3d.x * 16.0;
        double f = this.getY() + (double) (this.random.nextInt(16) - 8) - vec3d.y * 16.0;
        double g = this.getZ() + (this.random.nextDouble() - 0.5) * 8.0 - vec3d.z * 16.0;
        return this.teleportTo(e, f, g);
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.world.getBottomY() && !this.world.getBlockState(mutable).getMaterial().blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.world.getBlockState(mutable);
        boolean bl = blockState.getMaterial().blocksMovement();
        if (!bl) {
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

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? SoundEvents.ENTITY_ENDERMAN_SCREAM : SoundEvents.ENTITY_ENDERMAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMAN_DEATH;
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        BlockState blockState = this.getCarriedBlock();
        if (blockState != null) {
            this.dropItem(blockState.getBlock());
        }
    }

    public void setCarriedBlock(@Nullable BlockState state) {
        this.dataTracker.set(CARRIED_BLOCK, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getCarriedBlock() {
        return this.dataTracker.get(CARRIED_BLOCK).orElse(null);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getSource() instanceof ImmortalFireworkEntity immortalFireworkEntity && immortalFireworkEntity.wasShotAtAngle()) {
            return super.damage(source, 100000);
        }

        if (this.isInvulnerableTo(source)) {
            return false;
        }

        if (amount < 10000) {
            boolean bl = super.damage(source, 0.1f);
            setHealth(getMaxHealth());
            return bl;
        }

        if (source instanceof ProjectileDamageSource) {
            Entity entity = source.getSource();
            boolean bl = entity instanceof PotionEntity && this.damageFromPotion(source, (PotionEntity) entity, amount);
            for (int i = 0; i < 64; ++i) {
                if (!this.teleportRandomly()) continue;
                return true;
            }
            return bl;
        }
        boolean bl2 = super.damage(source, amount);
        if (!this.world.isClient() && !(source.getAttacker() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
            this.teleportRandomly();
        }
        return bl2;
    }

    @Override
    public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient) {

            for (int i = 0; i < 64; ++i) {
                if (!this.teleportRandomly()) continue;
                break;
            }

            playerEntity.sendMessage(Text.of("<§dImmortal §fEnderman> You can't catch me mortal! I know every trick in the book..."));
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!world.isClient) {
            ImmortalItemEntity itemEntity = new ImmortalItemEntity(world, getX(), getBodyY(0.5), getZ(),
                    ItemRegistry.IMMORTAL_ORB.getDefaultStack(),
                    world.getRandom().nextGaussian() * 0.2,
                    0.35, world.getRandom().nextGaussian() * 0.2);
            itemEntity.setGlowing(true);
            world.spawnEntity(itemEntity);

            world.playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0f, 1.0f);
            BionicUtils.sendEntityStatus((ServerWorld) world, this, ImmortalMobs.IDENTIFIER, 8);
            this.discard();
        }

        super.onDeath(damageSource);
    }

    private boolean damageFromPotion(DamageSource source, PotionEntity potion, float amount) {
        boolean bl;
        ItemStack itemStack = potion.getStack();
        Potion potion2 = PotionUtil.getPotion(itemStack);
        List<StatusEffectInstance> list = PotionUtil.getPotionEffects(itemStack);
        boolean bl2 = bl = potion2 == Potions.WATER && list.isEmpty();
        if (bl) {
            return super.damage(source, amount);
        }
        return false;
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

    public boolean isAngry() {
        return this.dataTracker.get(ANGRY);
    }

    public boolean isProvoked() {
        return this.dataTracker.get(PROVOKED);
    }

    public void setProvoked() {
        this.dataTracker.set(PROVOKED, true);
    }

    @Override
    public boolean cannotDespawn() {
        return super.cannotDespawn() || this.getCarriedBlock() != null;
    }

    static class ChasePlayerGoal
            extends Goal {
        private final ImmortalEndermanEntity enderman;
        @Nullable
        private LivingEntity target;

        public ChasePlayerGoal(ImmortalEndermanEntity enderman) {
            this.enderman = enderman;
            this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            this.target = this.enderman.getTarget();
            if (!(this.target instanceof PlayerEntity)) {
                return false;
            }
            double d = this.target.squaredDistanceTo(this.enderman);
            if (d > 256.0) {
                return false;
            }
            return this.enderman.isPlayerStaring((PlayerEntity) this.target);
        }

        @Override
        public void start() {
            this.enderman.getNavigation().stop();
        }

        @Override
        public void tick() {
            this.enderman.getLookControl().lookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
        }
    }

    static class PlaceBlockGoal
            extends Goal {
        private final ImmortalEndermanEntity enderman;

        public PlaceBlockGoal(ImmortalEndermanEntity enderman) {
            this.enderman = enderman;
        }

        @Override
        public boolean canStart() {
            if (this.enderman.getCarriedBlock() == null) {
                return false;
            }
            if (!this.enderman.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                return false;
            }
            return this.enderman.getRandom().nextInt(ImmortalEndermanEntity.PlaceBlockGoal.toGoalTicks(2000)) == 0;
        }

        @Override
        public void tick() {
            Random random = this.enderman.getRandom();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.getX() - 1.0 + random.nextDouble() * 2.0);
            int j = MathHelper.floor(this.enderman.getY() + random.nextDouble() * 2.0);
            int k = MathHelper.floor(this.enderman.getZ() - 1.0 + random.nextDouble() * 2.0);
            BlockPos blockPos = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos2 = blockPos.down();
            BlockState blockState2 = world.getBlockState(blockPos2);
            BlockState blockState3 = this.enderman.getCarriedBlock();
            if (blockState3 == null) {
                return;
            }
            if (this.canPlaceOn(world, blockPos, blockState3 = Block.postProcessState(blockState3, this.enderman.world, blockPos), blockState, blockState2, blockPos2)) {
                world.setBlockState(blockPos, blockState3, Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Emitter.of(this.enderman, blockState3));
                this.enderman.setCarriedBlock(null);
            }
        }

        private boolean canPlaceOn(World world, BlockPos posAbove, BlockState carriedState, BlockState stateAbove, BlockState state, BlockPos pos) {
            return stateAbove.isAir() && !state.isAir() && !state.isOf(Blocks.BEDROCK) && state.isFullCube(world, pos) && carriedState.canPlaceAt(world, posAbove) && world.getOtherEntities(this.enderman, Box.from(Vec3d.of(posAbove))).isEmpty();
        }
    }

    static class PickUpBlockGoal
            extends Goal {
        private final ImmortalEndermanEntity enderman;

        public PickUpBlockGoal(ImmortalEndermanEntity enderman) {
            this.enderman = enderman;
        }

        @Override
        public boolean canStart() {
            if (this.enderman.getCarriedBlock() != null) {
                return false;
            }
            if (!this.enderman.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                return false;
            }
            return this.enderman.getRandom().nextInt(ImmortalEndermanEntity.PickUpBlockGoal.toGoalTicks(20)) == 0;
        }

        @Override
        public void tick() {
            Random random = this.enderman.getRandom();
            World world = this.enderman.world;
            int i = MathHelper.floor(this.enderman.getX() - 2.0 + random.nextDouble() * 4.0);
            int j = MathHelper.floor(this.enderman.getY() + random.nextDouble() * 3.0);
            int k = MathHelper.floor(this.enderman.getZ() - 2.0 + random.nextDouble() * 4.0);
            BlockPos blockPos = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPos);
            Vec3d vec3d = new Vec3d((double) this.enderman.getBlockX() + 0.5, (double) j + 0.5, (double) this.enderman.getBlockZ() + 0.5);
            Vec3d vec3d2 = new Vec3d((double) i + 0.5, (double) j + 0.5, (double) k + 0.5);
            BlockHitResult blockHitResult = world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this.enderman));
            boolean bl = blockHitResult.getBlockPos().equals(blockPos);
            if (blockState.isIn(BlockTags.ENDERMAN_HOLDABLE) && bl) {
                world.removeBlock(blockPos, false);
                world.emitGameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Emitter.of(this.enderman, blockState));
                this.enderman.setCarriedBlock(blockState.getBlock().getDefaultState());
            }
        }
    }

    static class TeleportTowardsPlayerGoal
            extends ActiveTargetGoal<PlayerEntity> {
        private final ImmortalEndermanEntity enderman;
        @Nullable
        private PlayerEntity targetPlayer;
        private int lookAtPlayerWarmup;
        private int ticksSinceUnseenTeleport;
        private final TargetPredicate staringPlayerPredicate;
        private final TargetPredicate validTargetPredicate = TargetPredicate.createAttackable().ignoreVisibility();

        public TeleportTowardsPlayerGoal(ImmortalEndermanEntity enderman, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(enderman, PlayerEntity.class, 10, false, false, targetPredicate);
            this.enderman = enderman;
            this.staringPlayerPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(playerEntity -> enderman.isPlayerStaring((PlayerEntity) playerEntity));
        }

        @Override
        public boolean canStart() {
            this.targetPlayer = this.enderman.world.getClosestPlayer(this.staringPlayerPredicate, this.enderman);
            return this.targetPlayer != null;
        }

        @Override
        public void start() {
            this.lookAtPlayerWarmup = this.getTickCount(5);
            this.ticksSinceUnseenTeleport = 0;
            this.enderman.setProvoked();
        }

        @Override
        public void stop() {
            this.targetPlayer = null;
            super.stop();
        }

        @Override
        public boolean shouldContinue() {
            if (this.targetPlayer != null) {
                if (!this.enderman.isPlayerStaring(this.targetPlayer)) {
                    return false;
                }
                this.enderman.lookAtEntity(this.targetPlayer, 10.0f, 10.0f);
                return true;
            }
            if (this.targetEntity != null && this.validTargetPredicate.test(this.enderman, this.targetEntity)) {
                return true;
            }
            return super.shouldContinue();
        }

        @Override
        public void tick() {
            if (this.enderman.getTarget() == null) {
                super.setTargetEntity(null);
            }
            if (this.targetPlayer != null) {
                if (--this.lookAtPlayerWarmup <= 0) {
                    this.targetEntity = this.targetPlayer;
                    this.targetPlayer = null;
                    super.start();
                }
            } else {
                if (this.targetEntity != null && !this.enderman.hasVehicle()) {
                    if (this.enderman.isPlayerStaring((PlayerEntity) this.targetEntity)) {
                        if (this.targetEntity.squaredDistanceTo(this.enderman) < 16.0) {
                            this.enderman.teleportRandomly();
                        }
                        this.ticksSinceUnseenTeleport = 0;
                    } else if (this.targetEntity.squaredDistanceTo(this.enderman) > 256.0 && this.ticksSinceUnseenTeleport++ >= this.getTickCount(30) && this.enderman.teleportTo(this.targetEntity)) {
                        this.ticksSinceUnseenTeleport = 0;
                    }
                }
                super.tick();
            }
        }
    }
}

