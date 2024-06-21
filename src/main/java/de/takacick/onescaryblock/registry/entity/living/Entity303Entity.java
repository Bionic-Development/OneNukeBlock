package de.takacick.onescaryblock.registry.entity.living;

import de.takacick.onescaryblock.OneScaryBlockClient;
import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class Entity303Entity extends HostileEntity {

    private static final TrackedData<Integer> TARGET = DataTracker.registerData(Entity303Entity.class, TrackedDataHandlerRegistry.INTEGER);

    public Entity303Entity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.lookControl = new Entity303LookControl(this);

        getEntity303Target().ifPresentOrElse(entity -> {
            lookAtEntity(entity, 180f, 180f);
        }, () -> {
            if (getWorld().isClient) {
                lookAwayFromEntity(OneScaryBlockClient.getClientPlayer(), 180f);
            }
        });
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 120.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0);
    }

    @Override
    protected void initDataTracker() {

        getDataTracker().startTracking(TARGET, -1);

        super.initDataTracker();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.9, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true, (entity) -> {
            return entity.distanceTo(this) <= 7.5;
        }));
    }

    @Override
    public void tick() {
        if (!getWorld().isClient) {
            setEntity303Target(getTarget());
        }

        super.tick();

        getEntity303Target().ifPresentOrElse(entity -> {
            lookAtEntity(entity, 180f, 180f);
        }, () -> {
            if (getWorld().isClient) {
                lookAwayFromEntity(OneScaryBlockClient.getClientPlayer(), 180f);
            }
        });
    }

    @Override
    public float getStepHeight() {
        return 1.2f;
    }

    @Override
    protected void jump() {

    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {

        dropItem(ItemRegistry.ITEM_303);

        super.dropLoot(damageSource, causedByPlayer);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        boolean bl = source.getSource() instanceof PotionEntity;
        if (source.isIn(DamageTypeTags.IS_PROJECTILE) || bl) {
            boolean bl2 = bl && this.damageFromPotion(source, (PotionEntity) source.getSource(), amount);
            for (int i = 0; i < 64; ++i) {
                if (!this.teleportRandomly()) continue;
                return true;
            }
            return bl2;
        }

        return super.damage(source, amount);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);

        if (!getWorld().isClient) {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_DEATH_PARTICLES);
            this.discard();
        }
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
        if (status == 60) {
            for (int i = 0; i < 30; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ParticleRegistry.ITEM_303, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
        } else if (status == 46) {
            for (int i = 0; i < 30; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ParticleRegistry.ITEM_303, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }
    }

    public boolean teleportRandomly() {
        if (this.getWorld().isClient() || !this.isAlive()) {
            return false;
        }
        double d = this.getX() + (this.random.nextDouble() - 0.5) * 64.0;
        double e = this.getY() + (double) (this.random.nextInt(64) - 32);
        double f = this.getZ() + (this.random.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d, e, f);
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);
        while (mutable.getY() > this.getWorld().getBottomY() && !this.getWorld().getBlockState(mutable).blocksMovement()) {
            mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.getWorld().getBlockState(mutable);
        boolean bl = blockState.blocksMovement();
        boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
        if (!bl || bl2) {
            return false;
        }
        Vec3d vec3d = this.getPos();
        boolean bl3 = this.teleport(x, y, z, true);
        if (bl3) {
            if (!this.isSilent()) {
                this.getWorld().playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0f, 1.0f);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        }
        return bl3;
    }

    public void lookAwayFromEntity(Entity targetEntity, float maxYawChange) {
        double f;
        double d = -(targetEntity.getX() - this.getX());
        double e = -(targetEntity.getZ() - this.getZ());
        if (targetEntity instanceof LivingEntity livingEntity) {
            f = livingEntity.getEyeY() - this.getEyeY();
        } else {
            f = (targetEntity.getBoundingBox().minY + targetEntity.getBoundingBox().maxY) / 2.0 - this.getEyeY();
        }
        double g = Math.sqrt(d * d + e * e);
        float h = (float) (MathHelper.atan2(e, d) * 57.2957763671875) - 90.0f;
        float i = (float) (-(MathHelper.atan2(f, g) * 57.2957763671875));
        this.setPitch(40);
        this.setYaw(this.changeAngle(this.getYaw(), h, maxYawChange));
    }

    private float changeAngle(float from, float to, float max) {
        float f = MathHelper.wrapDegrees(to - from);
        if (f > max) {
            f = max;
        }
        if (f < -max) {
            f = -max;
        }
        return from + f;
    }

    public void setEntity303Target(Entity stareTarget) {
        getDataTracker().set(TARGET, stareTarget == null ? -1 : stareTarget.getId());
    }

    public Optional<Entity> getEntity303Target() {
        int id = getDataTracker().get(TARGET);

        return id >= 0 ? Optional.ofNullable(getWorld().getEntityById(id)) : Optional.empty();
    }

    public boolean hasEntity303Target() {
        return getDataTracker().get(TARGET) >= 0;
    }

    public boolean canTarget(LivingEntity target) {
        if (target instanceof PlayerEntity && this.getWorld().getDifficulty() == Difficulty.PEACEFUL && target.distanceTo(this) <= 20) {
            return false;
        }
        return target.canTakeDamage();
    }

    public class Entity303LookControl
            extends LookControl {

        public Entity303LookControl(MobEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {
            if (hasEntity303Target()) {
                return;
            }

            super.tick();
        }
    }
}