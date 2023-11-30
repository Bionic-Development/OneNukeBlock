package de.takacick.elementalblock.registry.entity.living;

import de.takacick.elementalblock.registry.ParticleRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class MagicCloudBuddyEntity extends PathAwareEntity {

    public MagicCloudBuddyEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new MagicMoveControl(this);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(8, new LookAtTargetGoal());
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0f, 1.0f));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0f));
    }

    @Override
    public void tick() {

        if(!hasPassengers()) {
            this.noClip = true;
        }
        super.tick();
        this.noClip = false;

        this.setNoGravity(true);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {

        if (!hasPassengers()) {
            player.startRiding(this, true);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean shouldDismountUnderwater() {
        return false;
    }

    @Override
    public int getMaxLookYawChange() {
        return 5;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if (!getWorld().isClient) {
            this.deathTime += 25;
        }
        super.onDeath(damageSource);
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed * 0.5f;
        float g = controllingPlayer.forwardSpeed;
        if (g <= 0.0f) {
            g *= 0.25f;
        }
        return new Vec3d(f, 0.0, g);
    }

    @Override
    public double getMountedHeightOffset() {
        return super.getMountedHeightOffset() + 0.2f;
    }

    @Override
    public void travel(Vec3d movementInput) {

        LivingEntity livingEntity = this.hasPassengers() ? (LivingEntity) this.getPassengerList().get(0) : null;
        if (livingEntity != null) {
                this.setYaw(livingEntity.getYaw());
                this.prevYaw = this.getYaw();
                this.setPitch(livingEntity.getPitch() * 0.5F);
                this.setRotation(this.getYaw(), this.getPitch());
                this.bodyYaw = this.getYaw();
                this.headYaw = this.bodyYaw;
                float f = livingEntity.sidewaysSpeed * 0.15F;
                float g = livingEntity.forwardSpeed * 0.15f;

                double y = livingEntity.getRotationVector().getY();

                double d = g > 0f ? y * 0.65 : 0;
                double h;
                if (this.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                    h = d + (double) ((float) (this.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
                } else {
                    h = d;
                }

                Vec3d vec3d = this.getVelocity();
                this.setVelocity(vec3d.x, h, vec3d.z);

                updateVelocity(1f, new Vec3d(f, movementInput.y, g));
                this.velocityDirty = true;

                if (this.isLogicalSideForUpdatingMovement()) {
                    this.setMovementSpeed((float) this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                    super.travel(new Vec3d(f, movementInput.y, g));
                } else if (livingEntity instanceof PlayerEntity) {
                    this.setVelocity(Vec3d.ZERO);
                }
                this.tryCheckBlockCollision();
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {

    }

    @Override
    protected void spawnSprintingParticles() {

    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 60) {
            double g = getX();
            double j = getZ();

            for (int i = 0; i < 15; ++i) {
                double d = random.nextGaussian();
                double e = random.nextDouble();
                double f = random.nextGaussian();

                getWorld().addParticle(ParticleRegistry.CLOUD,
                        true, g + d * getWidth(), getY() + e * getHeight(), j + f * getWidth(), d * 0.3, e * 0.3, f * 0.3);
            }
            double h = getBodyY(0.5);

            getWorld().playSound(g, h, j, SoundEvents.ENTITY_PHANTOM_FLAP, SoundCategory.AMBIENT, 1f, 1f + getRandom().nextFloat() * 0.2f, true);
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {

    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof MobEntity) {
            return (MobEntity) entity;
        }
        if ((entity = this.getFirstPassenger()) instanceof PlayerEntity) {
            return (PlayerEntity) entity;
        }
        return null;
    }

    public static DefaultAttributeContainer.Builder createMagicCloudBuddyAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0);
    }

    class MagicMoveControl
            extends MoveControl {
        public MagicMoveControl(MagicCloudBuddyEntity owner) {
            super(owner);
        }

        @Override
        public void tick() {
            if (this.state != MoveControl.State.MOVE_TO) {
                return;
            }
            Vec3d vec3d = new Vec3d(this.targetX - MagicCloudBuddyEntity.this.getX(), this.targetY - MagicCloudBuddyEntity.this.getY(), this.targetZ - MagicCloudBuddyEntity.this.getZ());
            double d = vec3d.length();
            if (d < MagicCloudBuddyEntity.this.getBoundingBox().getAverageSideLength()) {
                this.state = MoveControl.State.WAIT;
                MagicCloudBuddyEntity.this.setVelocity(MagicCloudBuddyEntity.this.getVelocity().multiply(0.5));
            } else {
                MagicCloudBuddyEntity.this.setVelocity(MagicCloudBuddyEntity.this.getVelocity().add(vec3d.multiply(this.speed * 0.05 / d)));
                if (MagicCloudBuddyEntity.this.getTarget() == null) {
                    Vec3d vec3d2 = MagicCloudBuddyEntity.this.getVelocity();
                    MagicCloudBuddyEntity.this.setYaw(-((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776f);
                    MagicCloudBuddyEntity.this.bodyYaw = MagicCloudBuddyEntity.this.getYaw();
                } else {
                    double e = MagicCloudBuddyEntity.this.getTarget().getX() - MagicCloudBuddyEntity.this.getX();
                    double f = MagicCloudBuddyEntity.this.getTarget().getZ() - MagicCloudBuddyEntity.this.getZ();
                    MagicCloudBuddyEntity.this.setYaw(-((float) MathHelper.atan2(e, f)) * 57.295776f);
                    MagicCloudBuddyEntity.this.bodyYaw = MagicCloudBuddyEntity.this.getYaw();
                }
            }
        }
    }


    class LookAtTargetGoal
            extends Goal {
        public LookAtTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return !MagicCloudBuddyEntity.this.getMoveControl().isMoving() && MagicCloudBuddyEntity.this.random.nextInt(MagicCloudBuddyEntity.LookAtTargetGoal.toGoalTicks(7)) == 0;
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }

        @Override
        public void tick() {
            BlockPos blockPos = MagicCloudBuddyEntity.this.getBlockPos();
            for (int i = 0; i < 3; ++i) {
                BlockPos blockPos2 = blockPos.add(MagicCloudBuddyEntity.this.random.nextInt(15) - 7, MagicCloudBuddyEntity.this.random.nextInt(11) - 5, MagicCloudBuddyEntity.this.random.nextInt(15) - 7);
                if (!MagicCloudBuddyEntity.this.getWorld().isAir(blockPos2)) continue;
                MagicCloudBuddyEntity.this.moveControl.moveTo((double) blockPos2.getX() + 0.5, (double) blockPos2.getY() + 0.5, (double) blockPos2.getZ() + 0.5, 0.25);
                if (MagicCloudBuddyEntity.this.getTarget() != null) break;
                MagicCloudBuddyEntity.this.getLookControl().lookAt((double) blockPos2.getX() + 0.5, (double) blockPos2.getY() + 0.5, (double) blockPos2.getZ() + 0.5, 180.0f, 20.0f);
                break;
            }
        }
    }
}
