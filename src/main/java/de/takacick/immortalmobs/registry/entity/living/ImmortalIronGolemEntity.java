package de.takacick.immortalmobs.registry.entity.living;

import com.google.common.collect.ImmutableList;
import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalArrowEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class ImmortalIronGolemEntity
        extends GolemEntity
        implements Angerable, ImmortalEntity {

    protected static final TrackedData<Byte> IRON_GOLEM_FLAGS = DataTracker.registerData(ImmortalIronGolemEntity.class, TrackedDataHandlerRegistry.BYTE);
    private int attackTicksLeft;
    private int lookingAtVillagerTicksLeft;
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    private int angerTime;
    @Nullable
    private UUID angryAt;
    public int deathTicks = 0;
    private int lookAtPlayerTicks = 0;
    private PlayerEntity player;

    public ImmortalIronGolemEntity(EntityType<? extends ImmortalIronGolemEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0f;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(2, new WanderNearTargetGoal(this, 0.9, 32.0f));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal((PathAwareEntity) this, 0.6, false));
        this.goalSelector.add(4, new IronGolemWanderAroundGoal(this, 0.6));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal<MobEntity>(this, MobEntity.class, 5, false, false, entity -> entity instanceof Monster && !(entity instanceof CreeperEntity)));
        this.targetSelector.add(4, new UniversalAngerGoal<ImmortalIronGolemEntity>(this, false));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IRON_GOLEM_FLAGS, (byte) 0);
    }

    public static DefaultAttributeContainer.Builder createIronGolemAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0);
    }

    @Override
    protected int getNextAirUnderwater(int air) {
        return air;
    }

    @Override
    protected void pushAway(Entity entity) {
        if (entity instanceof Monster && !(entity instanceof CreeperEntity) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity) entity);
        }
        super.pushAway(entity);
    }

    @Override
    protected void updatePostDeath() {
        ++this.deathTicks;

        fallDistance = 0;
        setPos(getX(), getY() + 0.0035, getZ());

        if (this.deathTicks >= 200 && !this.world.isClient()) {
            for (int i = 0; i < 5; i++) {
                ImmortalItemEntity itemEntity = new ImmortalItemEntity(world, getX(), getBodyY(0.5), getZ(),
                        ItemRegistry.IMMORTAL_INGOT.getDefaultStack(),
                        world.getRandom().nextGaussian() * 0.2,
                        0.35, world.getRandom().nextGaussian() * 0.2);
                itemEntity.setGlowing(true);
                world.spawnEntity(itemEntity);
            }
            world.playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0f, 1.0f);
            BionicUtils.sendEntityStatus((ServerWorld) world, this, ImmortalMobs.IDENTIFIER, 8);

            ((ServerWorld) world).spawnParticles(ParticleRegistry.IMMORTAL_EXPLOSION, getX(), getBodyY(0.5), getZ(), 5,
                    3.0D, 2.0D, 3.0D, 0.2);
            world.playSound(null, getX(), getBodyY(0.5), getZ(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1f, 1f);

            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public void tick() {

        if (lookAtPlayerTicks > 0 && player != null) {
            lookAtPlayerTicks--;
            getLookControl().lookAt(player);
            this.navigation.stop();

            if (lookAtPlayerTicks <= 0) {
                player = null;
            }
        }
        super.tick();
    }

    @Override
    public void tickMovement() {
        int k;
        int j;
        int i;
        BlockState blockState;
        super.tickMovement();
        if (this.attackTicksLeft > 0) {
            --this.attackTicksLeft;
        }
        if (this.lookingAtVillagerTicksLeft > 0) {
            --this.lookingAtVillagerTicksLeft;
        }
        if (this.getVelocity().horizontalLengthSquared() > 2.500000277905201E-7 && this.random.nextInt(5) == 0 && !(blockState = this.world.getBlockState(new BlockPos(i = MathHelper.floor(this.getX()), j = MathHelper.floor(this.getY() - (double) 0.2f), k = MathHelper.floor(this.getZ())))).isAir()) {
            this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), this.getX() + ((double) this.random.nextFloat() - 0.5) * (double) this.getWidth(), this.getY() + 0.1, this.getZ() + ((double) this.random.nextFloat() - 0.5) * (double) this.getWidth(), 4.0 * ((double) this.random.nextFloat() - 0.5), 0.5, ((double) this.random.nextFloat() - 0.5) * 4.0);
        }
        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld) this.world, true);
        }
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        if (this.isPlayerCreated() && type == EntityType.PLAYER) {
            return false;
        }
        if (type == EntityType.CREEPER) {
            return false;
        }
        return super.canTarget(type);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("PlayerCreated", this.isPlayerCreated());
        this.writeAngerToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setPlayerCreated(nbt.getBoolean("PlayerCreated"));
        this.readAngerFromNbt(this.world, nbt);
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

    private float getAttackDamage() {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    @Override
    public boolean tryAttack(Entity target) {
        this.attackTicksLeft = 10;
        this.world.sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
        float f = this.getAttackDamage();
        float g = (int) f > 0 ? f / 2.0f + (float) this.random.nextInt((int) f) : f;
        boolean bl = target.damage(DamageSource.mob(this), g);
        if (bl) {
            double d;
            if (target instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) target;
                d = livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            } else {
                d = 0.0;
            }
            double d2 = d;
            double e = Math.max(0.0, 1.0 - d2);
            target.setVelocity(target.getVelocity().add(0.0, (double) 0.4f * e, 0.0));
            this.applyDamageEffects(this, target);
        }
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.0f);
        return bl;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (source.getSource() instanceof ImmortalArrowEntity) {
            this.playSound(SoundEvents.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 1.0f);
            return super.damage(source, 100000);
        }

        if (amount < 10000) {
            boolean bl = super.damage(source, 0.1f);
            setHealth(getMaxHealth());
            return bl;
        }

        ImmortalIronGolemEntity.Crack crack = this.getCrack();
        boolean bl = super.damage(source, amount);
        if (bl && this.getCrack() != crack) {
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 1.0f, 1.0f);
        }

        return bl;
    }

    public ImmortalIronGolemEntity.Crack getCrack() {
        return ImmortalIronGolemEntity.Crack.from(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            this.attackTicksLeft = 10;
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.0f);
        } else if (status == EntityStatuses.LOOK_AT_VILLAGER) {
            this.lookingAtVillagerTicksLeft = 400;
        } else if (status == EntityStatuses.STOP_LOOKING_AT_VILLAGER) {
            this.lookingAtVillagerTicksLeft = 0;
        } else if (status == EntityStatuses.ADD_DEATH_PARTICLES) {
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
    public ActionResult interactMob(PlayerEntity playerEntity, Hand hand) {
        if (!world.isClient) {
            this.getNavigation().stop();
            lookAtPlayerTicks = 60;
            player = playerEntity;
            getLookControl().lookAt(player);
            playerEntity.sendMessage(Text.of("<§dImmortal §fIron Golem> I've defended this Village for millennials, and seeing each generation die has not phased me. Please help me feel pain again."));
        }

        return ActionResult.CONSUME;
    }

    public int getAttackTicksLeft() {
        return this.attackTicksLeft;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0f, 1.0f);
    }

    public int getLookingAtVillagerTicks() {
        return this.lookingAtVillagerTicksLeft;
    }

    public boolean isPlayerCreated() {
        return (this.dataTracker.get(IRON_GOLEM_FLAGS) & 1) != 0;
    }

    public void setPlayerCreated(boolean playerCreated) {
        byte b = this.dataTracker.get(IRON_GOLEM_FLAGS);
        if (playerCreated) {
            this.dataTracker.set(IRON_GOLEM_FLAGS, (byte) (b | 1));
        } else {
            this.dataTracker.set(IRON_GOLEM_FLAGS, (byte) (b & 0xFFFFFFFE));
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        setAiDisabled(true);
        super.onDeath(damageSource);
    }

    @Override
    public boolean canSpawn(WorldView world) {
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPos2 = blockPos.down();
        BlockState blockState = world.getBlockState(blockPos2);
        if (blockState.hasSolidTopSurface(world, blockPos2, this)) {
            for (int i = 1; i < 3; ++i) {
                BlockState blockState2;
                BlockPos blockPos3 = blockPos.up(i);
                if (SpawnHelper.isClearForSpawn(world, blockPos3, blockState2 = world.getBlockState(blockPos3), blockState2.getFluidState(), EntityType.IRON_GOLEM))
                    continue;
                return false;
            }
            return SpawnHelper.isClearForSpawn(world, blockPos, world.getBlockState(blockPos), Fluids.EMPTY.getDefaultState(), EntityType.IRON_GOLEM) && world.doesNotIntersectEntities(this);
        }
        return false;
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, 0.875f * this.getStandingEyeHeight(), this.getWidth() * 0.4f);
    }

    public static enum Crack {
        NONE(1.0f),
        LOW(0.75f),
        MEDIUM(0.5f),
        HIGH(0.25f);

        private static final List<ImmortalIronGolemEntity.Crack> VALUES;
        private final float maxHealthFraction;

        private Crack(float maxHealthFraction) {
            this.maxHealthFraction = maxHealthFraction;
        }

        public static ImmortalIronGolemEntity.Crack from(float healthFraction) {
            for (ImmortalIronGolemEntity.Crack crack : VALUES) {
                if (!(healthFraction < crack.maxHealthFraction)) continue;
                return crack;
            }
            return NONE;
        }

        static {
            VALUES = Stream.of(ImmortalIronGolemEntity.Crack.values()).sorted(Comparator.comparingDouble(crack -> crack.maxHealthFraction)).collect(ImmutableList.toImmutableList());
        }
    }
}

