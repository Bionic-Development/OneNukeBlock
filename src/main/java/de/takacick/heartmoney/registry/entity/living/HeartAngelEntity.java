package de.takacick.heartmoney.registry.entity.living;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.heartmoney.registry.entity.living.brain.HeartAngleBrain;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AllayBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.GameEventTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.VibrationListener;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class HeartAngelEntity
        extends PathAwareEntity
        implements InventoryOwner,
        VibrationListener.Callback, Tameable {

    protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(HeartAngelEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final Logger field_39045 = LogUtils.getLogger();
    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1, 1, 1);
    protected static final ImmutableList<SensorType<? extends Sensor<? super HeartAngelEntity>>> SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.PATH,
            MemoryModuleType.LOOK_TARGET, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM,
            MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS,
            MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS);
    private final EntityGameEventHandler<VibrationListener> gameEventHandler;
    private final SimpleInventory inventory = new SimpleInventory(1);
    private float field_38935;
    private float field_38936;

    private int explosionCooldown = random.nextBetween(200, 600);

    public HeartAngelEntity(EntityType<? extends HeartAngelEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.setCanPickUpLoot(this.canPickUpLoot());
        this.gameEventHandler = new EntityGameEventHandler<>(new VibrationListener(new EntityPositionSource(this, this.getStandingEyeHeight()), 16, this, null, 0.0f, 0));
    }

    protected Brain.Profile<HeartAngelEntity> createBrainProfile() {
        return Brain.createProfile(MEMORY_MODULES, SENSORS);
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return HeartAngleBrain.create(this, this.createBrainProfile().deserialize(dynamic));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1f).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement()) {
            if (this.isTouchingWater()) {
                this.updateVelocity(0.02f, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.8f));
            } else if (this.isInLava()) {
                this.updateVelocity(0.02f, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.5));
            } else {
                this.updateVelocity(this.getMovementSpeed(), movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.91f));
            }
        }
        this.updateLimbs(this, false);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.6f;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity entity = source.getAttacker();
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            Optional<UUID> optional = this.getBrain().getOptionalMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent() && playerEntity.getUuid().equals(optional.get())) {
                return false;
            }
        }
        return super.damage(source, amount);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.hasStackEquipped(EquipmentSlot.MAINHAND) ? SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ALLAY_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ALLAY_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    public Brain<HeartAngelEntity> getBrain() {
        return (Brain<HeartAngelEntity>) super.getBrain();
    }

    @Override
    protected void mobTick() {
        this.world.getProfiler().push("xpFairyBrain");
        this.getBrain().tick((ServerWorld) this.world, this);
        this.world.getProfiler().pop();
        this.world.getProfiler().push("xpFairyActivityUpdate");
        HeartAngleBrain.updateActivities(this);
        this.world.getProfiler().pop();
        super.mobTick();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient && this.isAlive() && this.age % 10 == 0) {
            this.heal(1.0f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClient) {
            this.field_38936 = this.field_38935;
            this.field_38935 = this.isHoldingItem() ? MathHelper.clamp(this.field_38935 + 1.0f, 0.0f, 5.0f) : MathHelper.clamp(this.field_38935 - 1.0f, 0.0f, 5.0f);

            if (random.nextInt(3) == 0) {
                world.addParticle(ParticleRegistry.ANGEL_DUST, getX() + world.getRandom().nextGaussian() * 0.1, getBodyY(0.6) + world.getRandom().nextGaussian() * 0.1, getZ() + world.getRandom().nextGaussian() * 0.25, 0, -0.05, 0);
            }
        } else {
            if (this.explosionCooldown > 0) {
                this.explosionCooldown--;

                if (this.explosionCooldown <= 0) {

                    for (int i = 0; i < getRandom().nextBetween(10, 30); i++) {
                        ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.HEART_ANGEL_HEART.getDefaultStack(),
                                world.getRandom().nextGaussian() * 0.175, world.getRandom().nextDouble() * 0.175, world.getRandom().nextGaussian() * 0.175);
                        itemEntity.setPickupDelay(10);
                        world.spawnEntity(itemEntity);
                    }

                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, HeartMoney.IDENTIFIER, 8);

                    this.explosionCooldown = random.nextBetween(200, 600);
                }
            }

            this.gameEventHandler.getListener().tick(this.world);
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!world.isClient) {
            for (int i = 0; i < getRandom().nextBetween(2, 5); i++) {
                ItemEntity itemEntity = new ItemEntity(world, getX(), getBodyY(0.5), getZ(), ItemRegistry.HEART_ANGEL_HEART.getDefaultStack(),
                        world.getRandom().nextGaussian() * 0.175, world.getRandom().nextDouble() * 0.175, world.getRandom().nextGaussian() * 0.175);
                itemEntity.setPickupDelay(10);
                world.spawnEntity(itemEntity);
            }

            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, HeartMoney.IDENTIFIER, 8);
            this.discard();
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return !this.isItemPickupCoolingDown() && this.isHoldingItem();
    }

    public boolean isHoldingItem() {
        return !this.getStackInHand(Hand.MAIN_HAND).isEmpty();
    }

    @Override
    public boolean canEquip(ItemStack stack) {
        return false;
    }

    private boolean isItemPickupCoolingDown() {
        return this.getBrain().isMemoryInState(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleState.VALUE_PRESENT);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        ItemStack itemStack2 = this.getStackInHand(Hand.MAIN_HAND);
        if (itemStack2.isEmpty() && !itemStack.isEmpty()) {
            ItemStack itemStack3 = itemStack.copy();
            itemStack3.setCount(1);
            this.setStackInHand(Hand.MAIN_HAND, itemStack3);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            this.world.playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_GIVEN, SoundCategory.NEUTRAL, 2.0f, 1.0f);
            this.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
            return ActionResult.SUCCESS;
        }
        if (!itemStack2.isEmpty() && hand == Hand.MAIN_HAND && itemStack.isEmpty()) {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.world.playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_TAKEN, SoundCategory.NEUTRAL, 2.0f, 1.0f);
            this.swingHand(Hand.MAIN_HAND);
            for (ItemStack itemStack4 : this.getInventory().clearToList()) {
                LookTargetUtil.give(this, itemStack4, this.getPos());
            }
            this.getBrain().forget(MemoryModuleType.LIKED_PLAYER);
            player.giveItemStack(itemStack2);
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    @Override
    protected Vec3i getItemPickUpRangeExpander() {
        return ITEM_PICKUP_RANGE_EXPANDER;
    }

    @Override
    public boolean canGather(ItemStack stack) {
        ItemStack itemStack = this.getStackInHand(Hand.MAIN_HAND);
        return !itemStack.isEmpty() && itemStack.isItemEqual(stack) && this.inventory.canInsert(stack);
    }

    @Override
    protected void loot(ItemEntity item) {
        InventoryOwner.pickUpItem(this, this, item);
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    @Override
    public boolean hasWings() {
        return !this.isOnGround();
    }

    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        World world = this.world;
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }

    public boolean method_43395() {
        return this.limbDistance > 0.3f;
    }

    public float method_43397(float f) {
        return MathHelper.lerp(f, this.field_38936, this.field_38935) / 5.0f;
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        this.inventory.clearToList().forEach(this::dropStack);
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
            this.dropStack(itemStack);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Emitter emitter) {
        if (this.world != world || this.isRemoved() || this.isAiDisabled()) {
            return false;
        }
        if (!this.brain.hasMemoryModule(MemoryModuleType.LIKED_NOTEBLOCK)) {
            return true;
        }
        Optional<GlobalPos> optional = this.brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK);
        return optional.isPresent() && optional.get().getDimension() == world.getRegistryKey() && optional.get().getPos().equals(pos);
    }

    @Override
    public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {
        if (event == GameEvent.NOTE_BLOCK_PLAY) {
            AllayBrain.rememberNoteBlock(this, new BlockPos(pos));
        }
    }

    @Override
    public TagKey<GameEvent> getTag() {
        return GameEventTags.ALLAY_CAN_LISTEN;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getOwnerUuid() != null) {
            nbt.putUuid("Owner", this.getOwnerUuid());
        }
        nbt.put("Inventory", this.inventory.toNbtList());
        VibrationListener.createCodec(this).encodeStart(NbtOps.INSTANCE, this.gameEventHandler.getListener()).resultOrPartial(field_39045::error).ifPresent(nbtElement -> nbt.put("listener", (NbtElement) nbtElement));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        UUID uUID;
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) {
            uUID = nbt.getUuid("Owner");
        } else {
            String string = nbt.getString("Owner");
            uUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }
        if (uUID != null) {
            try {
                this.setOwnerUuid(uUID);
            } catch (Throwable ignored) {

            }
        }

        this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE));
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void setOwner(PlayerEntity player) {
        this.setOwnerUuid(player.getUuid());
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uUID = this.getOwnerUuid();
            if (uUID == null) {
                return null;
            }
            return this.world.getPlayerByUuid(uUID);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    @Override
    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    @Override
    protected boolean shouldFollowLeash() {
        return false;
    }

    @Override
    public Iterable<BlockPos> getPotentialEscapePositions() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX - 0.5);
        int j = MathHelper.floor(box.maxX + 0.5);
        int k = MathHelper.floor(box.minZ - 0.5);
        int l = MathHelper.floor(box.maxZ + 0.5);
        int m = MathHelper.floor(box.minY - 0.5);
        int n = MathHelper.floor(box.maxY + 0.5);
        return BlockPos.iterate(i, m, k, j, n, l);
    }

    @Override
    public void playSpawnEffects() {
        if (this.world.isClient) {
            for (int i = 0; i < 20; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                double g = 10.0;
                this.world.addParticle(ParticleRegistry.HEART_POOF, this.offsetX(1.0) - d * 10.0, this.getRandomBodyY() - e * 10.0, this.getParticleZ(1.0) - f * 10.0, d, e, f);
            }
        } else {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_SPAWN_EFFECTS);
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 60) {
            for (int i = 0; i < 20; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.world.addParticle(ParticleRegistry.HEART_POOF, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, (double) this.getStandingEyeHeight() * 0.6, (double) this.getWidth() * 0.1);
    }
}

