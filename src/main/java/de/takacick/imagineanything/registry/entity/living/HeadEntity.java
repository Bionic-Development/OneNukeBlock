package de.takacick.imagineanything.registry.entity.living;

import com.mojang.authlib.GameProfile;
import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.ItemRegistry;
import de.takacick.imagineanything.registry.entity.custom.ThoughtEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class HeadEntity extends TameableEntity {

    protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(HeadEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    protected static final TrackedData<ItemStack> ITEM_STACK = DataTracker.registerData(HeadEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    protected static final TrackedData<ItemStack> THOUGHT = DataTracker.registerData(HeadEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private GameProfile gameProfile;
    private int thinkTicks = 0;

    public HeadEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
        this.dataTracker.startTracking(ITEM_STACK, ItemRegistry.HEAD.getDefaultStack());
        this.dataTracker.startTracking(THOUGHT, ItemStack.EMPTY);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new FollowOwnerGoal(this, 1.0, 5.0f, 3.0f, true));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f, 1f));
    }

    @Override
    public void tick() {
        if (!world.isClient) {
            if (getOwner() == null) {
                var optional = world.getOtherEntities(this, getBoundingBox().expand(3), entity -> entity instanceof PlayerEntity).stream().findAny();
                optional.ifPresent(entity -> setOwner((PlayerEntity) entity));
            }

            if (thinkTicks > 0) {
                thinkTicks--;
                BionicUtils.sendEntityStatus((ServerWorld) world, this, ImagineAnything.IDENTIFIER, 10);

                if (thinkTicks <= 0) {
                    ThoughtEntity thoughtEntity = new ThoughtEntity(world, getX(), getY() + getHeight(), getZ());
                    thoughtEntity.setOwner(this);
                    thoughtEntity.setThinkingTime(0);
                    world.spawnEntity(thoughtEntity);

                    ItemStack itemStack = ImagineAnything.getImagineQueue().poll();
                    setThoughtStack(itemStack);
                    if (itemStack == null) {
                        setThoughtStack(ItemStack.EMPTY);
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {

        if (!world.isClient) {

            if (player.getStackInHand(hand).isOf(ItemRegistry.THOUGHT) && !player.getStackInHand(hand).hasNbt()) {
                return ActionResult.SUCCESS;
            }

            if (player.getStackInHand(hand).isEmpty()) {
                player.sendMessage(Text.of(Arrays.asList("§ome need thoughts to imagine", "§ohelp i am empty inside", "§ome cant imagine anything without thoughts!").get(world.getRandom().nextInt(3))));

                Vec3d velocity = player.getPos().subtract(getPos()).normalize().multiply(0.35);
                ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.THOUGHT.getDefaultStack(), velocity.getX(), velocity.getY(), velocity.getZ());
                world.spawnEntity(itemEntity);
                BionicUtils.sendEntityStatus((ServerWorld) world, this, ImagineAnything.IDENTIFIER, 11);
            } else if (player.getStackInHand(hand).isOf(ItemRegistry.THOUGHT)) {
                player.sendMessage(Text.of(Arrays.asList("§e§oyes... i am seeing your imagination...", "§e§othe power of imagination is at work", "§e§ogoonganginga...").get(world.getRandom().nextInt(3))));
                player.getStackInHand(hand).decrement(1);
                thinkTicks = 60;
                world.playSoundFromEntity(null, this, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.AMBIENT, 0.5f, 1.5f);
            } else {
                player.sendMessage(Text.of(Arrays.asList("§a§ohmmm i think i am starting to understand...", "§a§oi can already begin to imagine", "§a§omy brain gears are turning...").get(world.getRandom().nextInt(3))));
                world.playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 1.0f, 1.0f + (world.random.nextFloat() - world.random.nextFloat()) * 0.4f);
                BionicUtils.sendEntityStatus((ServerWorld) world, this, ImagineAnything.IDENTIFIER, 9);
                player.getStackInHand(hand).decrement(1);
            }
        }

        return ActionResult.SUCCESS;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void setOwner(PlayerEntity player) {
        this.setOwnerUuid(player.getUuid());
    }

    public void setItemStack(ItemStack itemStack) {
        getDataTracker().set(ITEM_STACK, itemStack);
    }

    public ItemStack getItemStack() {
        return getDataTracker().get(ITEM_STACK);
    }

    public void setThoughtStack(ItemStack itemStack) {
        getDataTracker().set(THOUGHT, itemStack);
    }

    public ItemStack getThoughtStack() {
        return getDataTracker().get(THOUGHT);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {

        if (world.isClient && data.equals(ITEM_STACK)) {
            ItemStack stack = getItemStack();
            this.gameProfile = null;
            if (stack.hasNbt()) {
                NbtCompound nbtCompound = stack.getOrCreateNbt();
                if (nbtCompound.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
                    this.gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
                } else if (nbtCompound.contains("SkullOwner", NbtElement.STRING_TYPE) && !StringUtils.isBlank(nbtCompound.getString("SkullOwner"))) {
                    this.gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
                    nbtCompound.remove("SkullOwner");
                    SkullBlockEntity.loadProperties(gameProfile, profile -> nbtCompound.put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile)));
                }
            }
        }

        super.onTrackedDataSet(data);
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uUID = this.getOwnerUuid();
            if (uUID == null) {
                return null;
            }
            return this.world.getPlayerByUuid(uUID);
        } catch (IllegalArgumentException uUID) {
            return null;
        }
    }

    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return super.damage(source, amount >= 10000 ? amount : 0);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        if (getItemStack() != null) {
            nbt.put("headItemStack", getItemStack().writeNbt(new NbtCompound()));
        }
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        try {
            setItemStack(ItemStack.fromNbt(nbt.getCompound("headItemStack")));
        } catch (Exception ignored) {

        }

        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        HeadNavigation birdNavigation = new HeadNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public static class FollowOwnerGoal extends Goal {

        private final HeadEntity tameable;
        private LivingEntity owner;
        private final WorldView world;
        private final double speed;
        private final EntityNavigation navigation;
        private int updateCountdownTicks;
        private final float maxDistance;
        private final float minDistance;
        private float oldWaterPathfindingPenalty;
        private final boolean leavesAllowed;

        public FollowOwnerGoal(HeadEntity tameable, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
            this.tameable = tameable;
            this.world = tameable.world;
            this.speed = speed;
            this.navigation = tameable.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.leavesAllowed = leavesAllowed;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
            if (!(tameable.getNavigation() instanceof MobNavigation) && !(tameable.getNavigation() instanceof HeadNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.tameable.getOwner();
            if (livingEntity == null) {
                return false;
            }
            if (livingEntity.isSpectator()) {
                return false;
            }

            if (this.tameable.squaredDistanceTo(livingEntity) < (double) (this.minDistance * this.minDistance) && this.tameable.getY() - livingEntity.getBodyY(0.5) <= 1) {
                return false;
            }
            this.owner = livingEntity;
            return true;
        }

        @Override
        public boolean shouldContinue() {
            if (this.navigation.isIdle()) {
                return false;
            }

            return !(this.tameable.squaredDistanceTo(this.owner) <= (double) (this.maxDistance * this.maxDistance));
        }

        @Override
        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.tameable.getPathfindingPenalty(PathNodeType.WATER);
            this.tameable.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        }

        @Override
        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.tameable.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
        }

        @Override
        public void tick() {
            this.tameable.getLookControl().lookAt(this.owner, 10.0f, this.tameable.getMaxLookPitchChange());
            if (--this.updateCountdownTicks > 0) {
                return;
            }
            this.updateCountdownTicks = 10;
            if (this.tameable.isLeashed() || this.tameable.hasVehicle()) {
                return;
            }
            if (this.tameable.squaredDistanceTo(this.owner) >= 144.0) {
                this.tryTeleport();
            } else {
                this.navigation.startMovingTo(this.owner, this.speed);
            }
        }

        private void tryTeleport() {
            BlockPos blockPos = this.owner.getBlockPos();
            for (int i = 0; i < 10; ++i) {
                int j = this.getRandomInt(-3, 3);
                int k = this.getRandomInt(-1, 1);
                int l = this.getRandomInt(-3, 3);
                boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
                if (!bl) continue;
                return;
            }
        }

        private boolean tryTeleportTo(int x, int y, int z) {
            if (Math.abs((double) x - this.owner.getX()) < 2.0 && Math.abs((double) z - this.owner.getZ()) < 2.0) {
                return false;
            }
            if (!this.canTeleportTo(new BlockPos(x, y, z))) {
                return false;
            }
            this.tameable.refreshPositionAndAngles((double) x + 0.5, y, (double) z + 0.5, this.tameable.getYaw(), this.tameable.getPitch());
            this.navigation.stop();
            return true;
        }

        private boolean canTeleportTo(BlockPos pos) {
            PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
            if (pathNodeType != PathNodeType.OPEN) {
                return false;
            }
            BlockState blockState = this.world.getBlockState(pos.down());
            if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
                return false;
            }
            BlockPos blockPos = pos.subtract(this.tameable.getBlockPos());
            return this.world.isSpaceEmpty(this.tameable, this.tameable.getBoundingBox().offset(blockPos));
        }

        private int getRandomInt(int min, int max) {
            return this.tameable.getRandom().nextInt(max - min + 1) + min;
        }
    }

    public static class HeadNavigation
            extends EntityNavigation {
        public HeadNavigation(MobEntity mobEntity, World world) {
            super(mobEntity, world);
        }

        @Override
        protected PathNodeNavigator createPathNodeNavigator(int range) {
            this.nodeMaker = new BirdPathNodeMaker();
            this.nodeMaker.setCanEnterOpenDoors(true);
            return new PathNodeNavigator(this.nodeMaker, range);
        }

        @Override
        protected boolean isAtValidPosition() {
            return this.canSwim() && this.isInLiquid() || !this.entity.hasVehicle();
        }

        @Override
        protected Vec3d getPos() {
            return this.entity.getPos();
        }

        @Override
        public Path findPathTo(Entity entity, int distance) {
            return this.findPathTo(entity.getBlockPos(), distance);
        }

        @Override
        public void tick() {
            Vec3d vec3d;
            ++this.tickCount;
            if (this.inRecalculationCooldown) {
                this.recalculatePath();
            }
            if (this.isIdle()) {
                return;
            }
            if (this.isAtValidPosition()) {
                this.continueFollowingPath();
            } else if (this.currentPath != null && !this.currentPath.isFinished()) {
                vec3d = this.currentPath.getNodePosition(this.entity);
                if (this.entity.getBlockX() == MathHelper.floor(vec3d.x) && this.entity.getBlockY() == MathHelper.floor(vec3d.y) && this.entity.getBlockZ() == MathHelper.floor(vec3d.z)) {
                    this.currentPath.next();
                }
            }
            DebugInfoSender.sendPathfindingData(this.world, this.entity, this.currentPath, this.nodeReachProximity);
            if (this.isIdle()) {
                return;
            }
            vec3d = this.currentPath.getNodePosition(this.entity);
            this.entity.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
        }

        public void setCanPathThroughDoors(boolean canPathThroughDoors) {
            this.nodeMaker.setCanOpenDoors(canPathThroughDoors);
        }

        public boolean canEnterOpenDoors() {
            return this.nodeMaker.canEnterOpenDoors();
        }

        public void setCanEnterOpenDoors(boolean canEnterOpenDoors) {
            this.nodeMaker.setCanEnterOpenDoors(canEnterOpenDoors);
        }

        public boolean method_35129() {
            return this.nodeMaker.canEnterOpenDoors();
        }

        @Override
        public boolean isValidPosition(BlockPos pos) {
            return this.world.getBlockState(pos).hasSolidTopSurface(this.world, pos, this.entity);
        }
    }

    public static class FlightMoveControl
            extends MoveControl {
        private final int maxPitchChange;
        private final boolean noGravity;

        public FlightMoveControl(MobEntity entity, int maxPitchChange, boolean noGravity) {
            super(entity);
            this.maxPitchChange = maxPitchChange;
            this.noGravity = noGravity;
        }

        @Override
        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                this.state = MoveControl.State.WAIT;
                this.entity.setNoGravity(true);
                double d = this.targetX - this.entity.getX();
                double e = this.targetY - this.entity.getY();
                double f = this.targetZ - this.entity.getZ();
                double g = d * d + e * e + f * f;
                if (g < 2.500000277905201E-7) {
                    this.entity.setUpwardSpeed(0.0001f);
                    this.entity.setForwardSpeed(0.0f);
                    return;
                }
                float h = (float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f;
                this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), h, 90.0f));
                float i = this.entity.isOnGround() ? (float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)) : (float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED));
                this.entity.setMovementSpeed(i);
                double j = Math.sqrt(d * d + f * f);
                if (Math.abs(e) > (double) 1.0E-5f || Math.abs(j) > (double) 1.0E-5f) {
                    float k = (float) (-(MathHelper.atan2(e, j) * 57.2957763671875));
                    this.entity.setPitch(this.wrapDegrees(this.entity.getPitch(), k, this.maxPitchChange));
                    this.entity.setUpwardSpeed(e > 0.0 ? i : -i);
                }
            } else {
                if (!this.noGravity) {
                    this.entity.setNoGravity(false);
                }
                this.entity.setVelocity(this.entity.getVelocity().multiply(1, 0.01, 1));
                this.entity.setUpwardSpeed(0.0001f);
                this.entity.setForwardSpeed(0.0f);
            }
        }
    }

}
