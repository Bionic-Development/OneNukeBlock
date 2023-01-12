package de.takacick.immortalmobs.registry.entity.living;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalArrowEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class ImmortalSkeletonEntity
        extends AbstractSkeletonEntity implements ImmortalEntity {
    private final BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal = new BowAttackGoal<AbstractSkeletonEntity>(this, 1.0, 80, 15.0f);
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2, false) {

        @Override
        public void stop() {
            super.stop();
            ImmortalSkeletonEntity.this.setAttacking(false);
        }

        @Override
        public void start() {
            super.start();
            ImmortalSkeletonEntity.this.setAttacking(true);
        }
    };

    private static final TrackedData<Boolean> CONVERTING = DataTracker.registerData(ImmortalSkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public ImmortalSkeletonEntity(EntityType<? extends ImmortalSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.0));
        this.goalSelector.add(3, new FleeEntityGoal<WolfEntity>(this, WolfEntity.class, 6.0f, 1.0, 1.2));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity) this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<IronGolemEntity>((MobEntity) this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<TurtleEntity>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
    }

    @Override
    public void updateAttackType() {
        if (this.world == null || this.world.isClient || this.goalSelector == null) {
            return;
        }
        this.goalSelector.remove(this.meleeAttackGoal);
        this.goalSelector.remove(this.bowAttackGoal);
        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
        if (itemStack.isOf(Items.BOW)) {
            int i = 20;
            if (this.world.getDifficulty() != Difficulty.HARD) {
                i = 40;
            }
            if(this.bowAttackGoal != null) {
                this.bowAttackGoal.setAttackInterval(i);
                this.goalSelector.add(4, this.bowAttackGoal);
            }
        } else {
            if(this.meleeAttackGoal != null) {
                this.goalSelector.add(4, this.meleeAttackGoal);
            }
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(CONVERTING, false);
    }

    public boolean isConverting() {
        return this.getDataTracker().get(CONVERTING);
    }

    @Override
    public boolean isShaking() {
        return this.isConverting();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getSource() instanceof ImmortalWolfEntity) {
            return super.damage(source, amount * 0.75f);
        }

        if (amount < 10000) {
            float health = getHealth();
            boolean bl = super.damage(source, 0.1f);
            setHealth(health);
            return bl;
        }

        return super.damage(source, amount);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    public SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.playStepSound(pos, state);
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
    public ItemStack getArrowType(ItemStack stack) {
        return ItemRegistry.IMMORTAL_ARROW.getDefaultStack();
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ImmortalArrowEntity persistentProjectileEntity = new ImmortalArrowEntity(world, this);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        persistentProjectileEntity.setVelocity(d, e + g * (double) 0.2f, f, 1.6f, 14 - this.world.getDifficulty().getId() * 4);
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(persistentProjectileEntity);
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!world.isClient) {
            for (int i = 0; i < random.nextBetween(3, 6); i++) {
                ImmortalItemEntity itemEntity = new ImmortalItemEntity(world, getX(), getBodyY(0.5), getZ(),
                        ItemRegistry.IMMORTAL_ARROW.getDefaultStack(),
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
        }

        super.onDeath(damageSource);
    }

}

