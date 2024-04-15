package de.takacick.onegirlfriendblock.registry.entity.living;

import de.takacick.onegirlfriendblock.registry.entity.living.ai.CatSitOnBlockGoal;
import de.takacick.onegirlfriendblock.registry.entity.living.ai.GoToBedAndSleepGoal;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.EntityView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Iterator;
import java.util.List;

public class ZukoEntity extends TameableEntity {

    private static final Ingredient TAMING_INGREDIENT = Ingredient.ofItems(Items.COD, Items.SALMON);
    private static final TrackedData<Boolean> IN_SLEEPING_POSE = DataTracker.registerData(ZukoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HEAD_DOWN = DataTracker.registerData(ZukoEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> COLLAR_COLOR = DataTracker.registerData(ZukoEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private ZukoEntity.CatFleeGoal<PlayerEntity> fleeGoal;
    @Nullable
    private net.minecraft.entity.ai.goal.TemptGoal temptGoal;
    private float sleepAnimation;
    private float prevSleepAnimation;
    private float tailCurlAnimation;
    private float prevTailCurlAnimation;
    private float headDownAnimation;
    private float prevHeadDownAnimation;

    public ZukoEntity(EntityType<? extends ZukoEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        this.temptGoal = new TemptGoal(this, 0.6, TAMING_INGREDIENT, true);
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(3, new ZukoEntity.SleepWithOwnerGoal(this));
        this.goalSelector.add(4, this.temptGoal);
        this.goalSelector.add(5, new GoToBedAndSleepGoal(this, 1.1, 8));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F, false));
        this.goalSelector.add(7, new CatSitOnBlockGoal(this, 0.8));
        this.goalSelector.add(8, new PounceAtTargetGoal(this, 0.3F));
        this.goalSelector.add(9, new AttackGoal(this));
        this.goalSelector.add(11, new WanderAroundFarGoal(this, 0.8, 1.0000001E-5F));
        this.goalSelector.add(12, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.targetSelector.add(1, new UntamedActiveTargetGoal<>(this, RabbitEntity.class, false, null));
        this.targetSelector.add(1, new UntamedActiveTargetGoal<>(this, TurtleEntity.class, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
    }

    public void setInSleepingPose(boolean sleeping) {
        this.dataTracker.set(IN_SLEEPING_POSE, sleeping);
    }

    public boolean isInSleepingPose() {
        return this.dataTracker.get(IN_SLEEPING_POSE);
    }

    public void setHeadDown(boolean headDown) {
        this.dataTracker.set(HEAD_DOWN, headDown);
    }

    public boolean isHeadDown() {
        return this.dataTracker.get(HEAD_DOWN);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.dataTracker.get(COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor color) {
        this.dataTracker.set(COLLAR_COLOR, color.getId());
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IN_SLEEPING_POSE, false);
        this.dataTracker.startTracking(HEAD_DOWN, false);
        this.dataTracker.startTracking(COLLAR_COLOR, DyeColor.CYAN.getId());
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("CollarColor", (byte) this.getCollarColor().getId());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(nbt.getInt("CollarColor")));
        }
    }

    public void mobTick() {
        if (this.getMoveControl().isMoving()) {
            double d = this.getMoveControl().getSpeed();
            if (d == 0.6) {
                this.setPose(EntityPose.CROUCHING);
                this.setSprinting(false);
            } else if (d == 1.33) {
                this.setPose(EntityPose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(EntityPose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(EntityPose.STANDING);
            this.setSprinting(false);
        }

    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isTamed()) {
            if (this.isInLove()) {
                return SoundEvents.ENTITY_CAT_PURR;
            } else {
                return this.random.nextInt(4) == 0 ? SoundEvents.ENTITY_CAT_PURREOW : SoundEvents.ENTITY_CAT_AMBIENT;
            }
        } else {
            return SoundEvents.ENTITY_CAT_STRAY_AMBIENT;
        }
    }

    public int getMinAmbientSoundDelay() {
        return 120;
    }

    public void hiss() {
        this.playSound(SoundEvents.ENTITY_CAT_HISS, this.getSoundVolume(), this.getSoundPitch());
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CAT_DEATH;
    }

    public static DefaultAttributeContainer.Builder createCatAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30000001192092896).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }

    protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (this.isBreedingItem(stack)) {
            this.playSound(SoundEvents.ENTITY_CAT_EAT, 1.0F, 1.0F);
        }

        super.eat(player, hand, stack);
    }

    private float getAttackDamage() {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    public boolean tryAttack(Entity target) {
        return target.damage(this.getDamageSources().mobAttack(this), this.getAttackDamage());
    }

    public void tick() {
        super.tick();
        if (this.temptGoal != null && this.temptGoal.isActive() && !this.isTamed() && this.age % 100 == 0) {
            this.playSound(SoundEvents.ENTITY_CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }

        this.updateAnimations();
    }

    private void updateAnimations() {
        if ((this.isInSleepingPose() || this.isHeadDown()) && this.age % 5 == 0) {
            this.playSound(SoundEvents.ENTITY_CAT_PURR, 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F);
        }

        this.updateSleepAnimation();
        this.updateHeadDownAnimation();
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (isSitting() || isSleeping() && getWorld().getRandom().nextDouble() <= 0.0001) {
            hiss();
        }

        super.pushAwayFrom(entity);
    }

    private void updateSleepAnimation() {
        this.prevSleepAnimation = this.sleepAnimation;
        this.prevTailCurlAnimation = this.tailCurlAnimation;
        if (this.isInSleepingPose()) {
            this.sleepAnimation = Math.min(1.0F, this.sleepAnimation + 0.15F);
            this.tailCurlAnimation = Math.min(1.0F, this.tailCurlAnimation + 0.08F);
        } else {
            this.sleepAnimation = Math.max(0.0F, this.sleepAnimation - 0.22F);
            this.tailCurlAnimation = Math.max(0.0F, this.tailCurlAnimation - 0.13F);
        }
    }


    private void updateHeadDownAnimation() {
        this.prevHeadDownAnimation = this.headDownAnimation;
        if (this.isHeadDown()) {
            this.headDownAnimation = Math.min(1.0F, this.headDownAnimation + 0.1F);
        } else {
            this.headDownAnimation = Math.max(0.0F, this.headDownAnimation - 0.13F);
        }

    }

    public float getSleepAnimation(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevSleepAnimation, this.sleepAnimation);
    }

    public float getTailCurlAnimation(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevTailCurlAnimation, this.tailCurlAnimation);
    }

    public float getHeadDownAnimation(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevHeadDownAnimation, this.headDownAnimation);
    }

    @Nullable
    public ZukoEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);

        return entityData;
    }

    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if (this.getWorld().isClient) {
            if (this.isTamed() && this.isOwner(player)) {
                return ActionResult.SUCCESS;
            } else {
                return !this.isBreedingItem(itemStack) || !(this.getHealth() < this.getMaxHealth()) && this.isTamed() ? ActionResult.PASS : ActionResult.SUCCESS;
            }
        } else {
            ActionResult actionResult;
            if (this.isTamed()) {
                if (this.isOwner(player)) {
                    if (!(item instanceof DyeItem)) {
                        if (item.isFood() && this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
                            this.eat(player, hand, itemStack);
                            this.heal((float) item.getFoodComponent().getHunger());
                            return ActionResult.CONSUME;
                        }

                        actionResult = super.interactMob(player, hand);
                        if (!actionResult.isAccepted() || this.isBaby()) {
                            this.setSitting(!this.isSitting());
                        }

                        return actionResult;
                    }

                    DyeColor dyeColor = ((DyeItem) item).getColor();
                    if (dyeColor != this.getCollarColor()) {
                        this.setCollarColor(dyeColor);
                        if (!player.getAbilities().creativeMode) {
                            itemStack.decrement(1);
                        }

                        this.setPersistent();
                        return ActionResult.CONSUME;
                    }
                }
            } else if (this.isBreedingItem(itemStack)) {
                this.eat(player, hand, itemStack);
                if (this.random.nextInt(3) == 0) {
                    this.setOwner(player);
                    this.setSitting(true);
                    this.getWorld().sendEntityStatus(this, (byte) 7);
                } else {
                    this.getWorld().sendEntityStatus(this, (byte) 6);
                }

                this.setPersistent();
                return ActionResult.CONSUME;
            }

            actionResult = super.interactMob(player, hand);
            if (actionResult.isAccepted()) {
                this.setPersistent();
            }

            return actionResult;
        }
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.isTamed() && this.age > 2400;
    }

    protected void onTamedChanged() {
        if (this.fleeGoal == null) {
            this.fleeGoal = new ZukoEntity.CatFleeGoal(this, PlayerEntity.class, 16.0F, 0.8, 1.33);
        }

        this.goalSelector.remove(this.fleeGoal);
        if (!this.isTamed()) {
            this.goalSelector.add(4, this.fleeGoal);
        }

    }

    public boolean bypassesSteppingEffects() {
        return this.isInSneakingPose() || super.bypassesSteppingEffects();
    }

    protected Vector3f getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return new Vector3f(0.0F, dimensions.height - 0.1875F * scaleFactor, 0.0F);
    }

    @Override
    public EntityView method_48926() {
        return getWorld();
    }

    static class TemptGoal extends net.minecraft.entity.ai.goal.TemptGoal {
        @Nullable
        private PlayerEntity player;
        private final ZukoEntity cat;

        public TemptGoal(ZukoEntity cat, double speed, Ingredient food, boolean canBeScared) {
            super(cat, speed, food, canBeScared);
            this.cat = cat;
        }

        public void tick() {
            super.tick();
            if (this.player == null && this.mob.getRandom().nextInt(this.getTickCount(600)) == 0) {
                this.player = this.closestPlayer;
            } else if (this.mob.getRandom().nextInt(this.getTickCount(500)) == 0) {
                this.player = null;
            }

        }

        protected boolean canBeScared() {
            return (this.player == null || !this.player.equals(this.closestPlayer)) && super.canBeScared();
        }

        public boolean canStart() {
            return super.canStart() && !this.cat.isTamed();
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return TAMING_INGREDIENT.test(stack);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (amount < 10000f) {
            amount = 0f;
        }

        return super.damage(source, amount);
    }

    static class SleepWithOwnerGoal extends Goal {
        private final ZukoEntity cat;
        @Nullable
        private PlayerEntity owner;
        @Nullable
        private BlockPos bedPos;
        private int ticksOnBed;

        public SleepWithOwnerGoal(ZukoEntity cat) {
            this.cat = cat;
        }

        public boolean canStart() {
            if (!this.cat.isTamed()) {
                return false;
            } else if (this.cat.isSitting()) {
                return false;
            } else {
                LivingEntity livingEntity = this.cat.getOwner();
                if (livingEntity instanceof PlayerEntity) {
                    this.owner = (PlayerEntity) livingEntity;
                    if (!livingEntity.isSleeping()) {
                        return false;
                    }

                    if (this.cat.squaredDistanceTo(this.owner) > 100.0) {
                        return false;
                    }

                    BlockPos blockPos = this.owner.getBlockPos();
                    BlockState blockState = this.cat.getWorld().getBlockState(blockPos);
                    if (blockState.isIn(BlockTags.BEDS)) {
                        this.bedPos = (BlockPos) blockState.getOrEmpty(BedBlock.FACING).map((direction) -> {
                            return blockPos.offset(direction.getOpposite());
                        }).orElseGet(() -> {
                            return new BlockPos(blockPos);
                        });
                        return !this.cannotSleep();
                    }
                }

                return false;
            }
        }

        private boolean cannotSleep() {
            List<ZukoEntity> list = this.cat.getWorld().getNonSpectatingEntities(ZukoEntity.class, (new Box(this.bedPos)).expand(2.0));
            Iterator var2 = list.iterator();

            ZukoEntity catEntity;
            do {
                do {
                    if (!var2.hasNext()) {
                        return false;
                    }

                    catEntity = (ZukoEntity) var2.next();
                } while (catEntity == this.cat);
            } while (!catEntity.isInSleepingPose() && !catEntity.isHeadDown());

            return true;
        }

        public boolean shouldContinue() {
            return this.cat.isTamed() && !this.cat.isSitting() && this.owner != null && this.owner.isSleeping() && this.bedPos != null && !this.cannotSleep();
        }

        public void start() {
            if (this.bedPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().startMovingTo((double) this.bedPos.getX(), (double) this.bedPos.getY(), (double) this.bedPos.getZ(), 1.100000023841858);
            }

        }

        public void stop() {
            this.cat.setInSleepingPose(false);
            float f = this.cat.getWorld().getSkyAngle(1.0F);
            if (this.owner.getSleepTimer() >= 100 && (double) f > 0.77 && (double) f < 0.8 && (double) this.cat.getWorld().getRandom().nextFloat() < 0.7) {
                this.dropMorningGifts();
            }

            this.ticksOnBed = 0;
            this.cat.setHeadDown(false);
            this.cat.getNavigation().stop();
        }

        private void dropMorningGifts() {
            Random random = this.cat.getRandom();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            mutable.set(this.cat.isLeashed() ? this.cat.getHoldingEntity().getBlockPos() : this.cat.getBlockPos());
            this.cat.teleport((double) (mutable.getX() + random.nextInt(11) - 5), (double) (mutable.getY() + random.nextInt(5) - 2), (double) (mutable.getZ() + random.nextInt(11) - 5), false);
            mutable.set(this.cat.getBlockPos());
            LootTable lootTable = this.cat.getWorld().getServer().getLootManager().getLootTable(LootTables.CAT_MORNING_GIFT_GAMEPLAY);
            LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld) this.cat.getWorld())).add(LootContextParameters.ORIGIN, this.cat.getPos()).add(LootContextParameters.THIS_ENTITY, this.cat).build(LootContextTypes.GIFT);
            List<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
            Iterator var6 = list.iterator();

            while (var6.hasNext()) {
                ItemStack itemStack = (ItemStack) var6.next();
                this.cat.getWorld().spawnEntity(new ItemEntity(this.cat.getWorld(), (double) mutable.getX() - (double) MathHelper.sin(this.cat.bodyYaw * 0.017453292F), (double) mutable.getY(), (double) mutable.getZ() + (double) MathHelper.cos(this.cat.bodyYaw * 0.017453292F), itemStack));
            }

        }

        public void tick() {
            if (this.owner != null && this.bedPos != null) {
                this.cat.setInSittingPose(false);
                this.cat.getNavigation().startMovingTo((double) this.bedPos.getX(), (double) this.bedPos.getY(), (double) this.bedPos.getZ(), 1.100000023841858);
                if (this.cat.squaredDistanceTo(this.owner) < 2.5) {
                    ++this.ticksOnBed;
                    if (this.ticksOnBed > this.getTickCount(16)) {
                        this.cat.setInSleepingPose(true);
                        this.cat.setHeadDown(false);
                    } else {
                        this.cat.lookAtEntity(this.owner, 45.0F, 45.0F);
                        this.cat.setHeadDown(true);
                    }
                } else {
                    this.cat.setInSleepingPose(false);
                }
            }

        }
    }

    static class CatFleeGoal<T extends LivingEntity> extends FleeEntityGoal<T> {
        private final ZukoEntity cat;

        public CatFleeGoal(ZukoEntity cat, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
            super(cat, fleeFromType, distance, slowSpeed, fastSpeed);
            this.cat = cat;
        }

        public boolean canStart() {
            return !this.cat.isTamed() && super.canStart();
        }

        public boolean shouldContinue() {
            return !this.cat.isTamed() && super.shouldContinue();
        }
    }
}
