package de.takacick.immortalmobs.registry.entity.living;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.network.ImmortalFireworkExplosionHandler;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import de.takacick.utils.BionicUtils;
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
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class ImmortalPigEntity
        extends AnimalEntity
        implements ItemSteerable, ImmortalEntity {
    private static final TrackedData<Boolean> IMMORTAL_ELYTRA = DataTracker.registerData(ImmortalPigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IMMORTAL_FLYING = DataTracker.registerData(ImmortalPigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IMMORTAL_FALLING = DataTracker.registerData(ImmortalPigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int lookAtPlayerTicks = 0;
    private int flyTicks = 0;
    private PlayerEntity player;

    public ImmortalPigEntity(EntityType<? extends ImmortalPigEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(3, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(4, new TemptGoal(this, 1.2, Ingredient.ofItems(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createPigAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    @Nullable
    public Entity getPrimaryPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity != null && this.canBeControlledByRider(entity) ? entity : null;
    }

    private boolean canBeControlledByRider(Entity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            return playerEntity.getMainHandStack().isOf(Items.CARROT_ON_A_STICK) || playerEntity.getOffHandStack().isOf(Items.CARROT_ON_A_STICK);
        }
        return false;
    }

    @Override
    public void tick() {

        if (lookAtPlayerTicks > 0) {
            lookAtPlayerTicks--;
            if (player != null) {
                getLookControl().lookAt(player);
                this.navigation.stop();
            } else {
                lookAtPlayerTicks = 0;
            }

            if (lookAtPlayerTicks <= 0) {
                player = null;

                if (hasImmortalElytra()) {
                    setImmortalFlying(true);
                    setVelocity(new Vec3d(0, 1, 0));
                    this.velocityDirty = true;
                    this.velocityModified = true;
                    BionicUtils.sendEntityStatus((ServerWorld) world, this, ImmortalMobs.IDENTIFIER, 10);
                    ((ServerWorld) world).spawnParticles(ParticleRegistry.IMMORTAL_FIREWORK, this.getX(), this.getY() - 0.8, this.getZ(), 1, 0, 0, 0, 0);
                }
            }
        }

        if (isImmortalFlying()) {
            setVelocity(new Vec3d(0, 1.45, 0));
            this.velocityDirty = true;
            this.velocityModified = true;
            flyTicks++;

            if (!world.isClient) {
                if (flyTicks <= 30) {
                    ((ServerWorld) world).spawnParticles(ParticleRegistry.IMMORTAL_FIREWORK, this.getX(), this.getY() - 0.8, this.getZ(), 1, 0, 0, 0, 0);
                }

                if (flyTicks >= 120) {
                    setImmortalFlying(false);
                    setImmortalFalling(true);
                }
            }
        } else if (isImmortalFalling()) {
            setVelocity(new Vec3d(0, -1.45, 0));
            this.velocityDirty = true;
            this.velocityModified = true;
            fallDistance = 10;
        }

        super.tick();
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

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {

        if (!world.isClient && isImmortalFalling()) {
            ImmortalFireworkExplosionHandler.createExplosion((ServerWorld) world, null, null, null,
                    getX(), getBodyY(0.5), getZ(), 3.0f, false,
                    Explosion.DestructionType.DESTROY);
            for (int i = 0; i < 1; i++) {
                ImmortalItemEntity itemEntity = new ImmortalItemEntity(world, getX(), getBodyY(0.5), getZ(),
                        ItemRegistry.IMMORTAL_PORKCHOP.getDefaultStack(),
                        world.getRandom().nextGaussian() * 0.2,
                        0.35, world.getRandom().nextGaussian() * 0.2);
                itemEntity.setGlowing(true);
                world.spawnEntity(itemEntity);
            }
            BionicUtils.sendEntityStatus((ServerWorld) world, this, ImmortalMobs.IDENTIFIER, 11);
            this.discard();
        }

        return super.handleFallDamage(fallDistance, damageMultiplier, damageSource);
    }

    @Override
    public boolean isFallFlying() {
        return isImmortalFlying() || isImmortalFalling() || super.isFallFlying();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (amount < 10000) {
            boolean bl = super.damage(source, 0.1f);
            setHealth(getMaxHealth());
            return bl;
        }

        return super.damage(source, amount);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IMMORTAL_ELYTRA, false);
        this.dataTracker.startTracking(IMMORTAL_FLYING, false);
        this.dataTracker.startTracking(IMMORTAL_FALLING, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("immortal_elytra", this.dataTracker.get(IMMORTAL_ELYTRA));
        nbt.putBoolean("immortal_flying", this.dataTracker.get(IMMORTAL_FLYING));
        nbt.putBoolean("immortal_falling", this.dataTracker.get(IMMORTAL_FALLING));
        nbt.putInt("immortal_flyTicks", this.flyTicks);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.dataTracker.set(IMMORTAL_ELYTRA, nbt.getBoolean("immortal_elytra"));
        this.dataTracker.set(IMMORTAL_FLYING, nbt.getBoolean("immortal_flying"));
        this.dataTracker.set(IMMORTAL_FALLING, nbt.getBoolean("immortal_falling"));
        this.flyTicks = nbt.getInt("immortal_flyTicks");

        if (hasImmortalElytra() && !isImmortalFalling() && !isImmortalFlying()) {
            this.lookAtPlayerTicks = 1;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PIG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15f, 1.0f);
    }

    @Override
    public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient && playerEntity.getStackInHand(hand).isOf(ItemRegistry.IMMORTAL_ELYTRA) && !hasImmortalElytra()) {
            BionicUtils.sendEntityStatus((ServerWorld) world, this, ImmortalMobs.IDENTIFIER, 9);
            this.getNavigation().stop();
            lookAtPlayerTicks = 100;
            player = playerEntity;
            getLookControl().lookAt(player);
            playerEntity.sendMessage(Text.of("<§dImmortal §fPig> With this I can definitely fly... and there's a carrot!"));
            setImmortalElytra(true);
            playerEntity.getStackInHand(hand).decrement(1);

            return ActionResult.CONSUME;
        }

        if (!world.isClient && !hasImmortalElytra()) {
            this.getNavigation().stop();
            lookAtPlayerTicks = 60;
            player = playerEntity;
            getLookControl().lookAt(player);
            playerEntity.sendMessage(Text.of("<§dImmortal §fPig> All these years, and I still wish I could fly..."));
        }

        return ActionResult.CONSUME;
    }

    public void setImmortalElytra(boolean immortalElytra) {
        this.getDataTracker().set(IMMORTAL_ELYTRA, immortalElytra);
    }

    public boolean hasImmortalElytra() {
        return this.getDataTracker().get(IMMORTAL_ELYTRA);
    }

    public void setImmortalFlying(boolean immortalFlying) {
        this.getDataTracker().set(IMMORTAL_FLYING, immortalFlying);
    }

    public boolean isImmortalFlying() {
        return this.getDataTracker().get(IMMORTAL_FLYING);
    }

    public void setImmortalFalling(boolean immortalFalling) {
        this.getDataTracker().set(IMMORTAL_FALLING, immortalFalling);
    }

    public boolean isImmortalFalling() {
        return this.getDataTracker().get(IMMORTAL_FALLING);
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        }
        int[][] is = Dismounting.getDismountOffsets(direction);
        BlockPos blockPos = this.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (EntityPose entityPose : passenger.getPoses()) {
            Box box = passenger.getBoundingBox(entityPose);
            for (int[] js : is) {
                Vec3d vec3d;
                mutable.set(blockPos.getX() + js[0], blockPos.getY(), blockPos.getZ() + js[1]);
                double d = this.world.getDismountHeight(mutable);
                if (!Dismounting.canDismountInBlock(d) || !Dismounting.canPlaceEntityAt(this.world, passenger, box.offset(vec3d = Vec3d.ofCenter(mutable, d))))
                    continue;
                passenger.setPose(entityPose);
                return vec3d;
            }
        }
        return super.updatePassengerForDismount(passenger);
    }

    @Override
    public float getSaddledSpeed() {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * 0.225f;
    }

    @Override
    public boolean consumeOnAStickItem() {
        return false;
    }

    @Override
    public void setMovementInput(Vec3d movementInput) {
        super.travel(movementInput);
    }

    @Override
    public ImmortalPigEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, 0.6f * this.getStandingEyeHeight(), this.getWidth() * 0.4f);
    }
}
