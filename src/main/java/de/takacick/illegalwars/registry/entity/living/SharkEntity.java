package de.takacick.illegalwars.registry.entity.living;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.EffectRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SharkEntity
        extends WaterCreatureEntity {

    public static final Predicate<LivingEntity> INSIDE_WATER_FILTER = Entity::isTouchingWater;
    private static final TrackedData<Integer> MOISTNESS = DataTracker.registerData(SharkEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public boolean guiRendering = false;

    private boolean closing = false;

    public float lowerYaw = 0f;
    public float prevLowerYaw = 0f;

    public SharkEntity(EntityType<? extends SharkEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new AquaticMoveControl(this, 85, 10, 0.02f, 0.1f, true);
        this.lookControl = new YawAdjustingLookControl(this, 10);
        this.setCanPickUpLoot(true);
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setAir(this.getMaxAir());
        this.setPitch(0.0f);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void tickWaterBreathingAir(int air) {
    }

    @Override
    public void tickMovement() {

        this.prevLowerYaw = this.lowerYaw;
        if (this.handSwinging) {
            this.lowerYaw = Math.max(this.lowerYaw - 10, 0f);
            if (this.lowerYaw <= 0) {
                this.handSwinging = false;
            }
        } else if (this.isAttacking()) {
            this.lowerYaw = MathHelper.clamp(this.lowerYaw + 5f, 0f, 40f);
        } else {
            if (this.lowerYaw > 20f) {
                this.closing = true;
            }

            if (this.closing) {
                this.lowerYaw = Math.max(this.lowerYaw - 2.5f, 0f);
                if (this.lowerYaw <= 0) {
                    this.closing = false;
                }
            } else {
                this.lowerYaw = this.lowerYaw + 2.5f;
                if (this.lowerYaw >= 20f) {
                    this.closing = true;
                }
            }
        }

        super.tickMovement();
    }

    public float getLowerYaw(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevLowerYaw, this.lowerYaw);
    }

    public int getMoistness() {
        return this.dataTracker.get(MOISTNESS);
    }

    public void setMoistness(int moistness) {
        this.dataTracker.set(MOISTNESS, moistness);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOISTNESS, 2400);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Moistness", this.getMoistness());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        int i = nbt.getInt("TreasurePosX");
        int j = nbt.getInt("TreasurePosY");
        int k = nbt.getInt("TreasurePosZ");
        super.readCustomDataFromNbt(nbt);
        this.setMoistness(nbt.getInt("Moistness"));
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new BreatheAirGoal(this));
        this.goalSelector.add(0, new MoveIntoWaterGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2f, true));
        this.goalSelector.add(4, new SwimAroundGoal(this, 1.0, 10));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class,
                10, true, false, INSIDE_WATER_FILTER));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PassiveEntity.class,
                10, true, false, INSIDE_WATER_FILTER));
    }

    public static DefaultAttributeContainer.Builder createSharkAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1.2f).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new SwimNavigation(this, world);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(this.getDamageSources().mobAttack(this), (int) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (bl) {
            this.applyDamageEffects(this, target);
            this.playSound(ParticleRegistry.SHARK_BITE, 0.15f, 1.0f);
        }
        return bl;
    }

    @Override
    public void applyDamageEffects(LivingEntity attacker, Entity target) {

        if (target instanceof LivingEntity livingEntity) {
            BionicUtils.sendEntityStatus(getWorld(), livingEntity, IllegalWars.IDENTIFIER, 2);

            livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
        }

        super.applyDamageEffects(attacker, target);
    }

    @Override
    public int getMaxAir() {
        return 4800;
    }

    @Override
    protected int getNextAirOnLand(int air) {
        return this.getMaxAir();
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.3f;
    }

    @Override
    public int getMaxLookPitchChange() {
        return 1;
    }

    @Override
    public int getMaxHeadRotation() {
        return 1;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return true;
    }

    @Override
    public boolean canEquip(ItemStack stack) {
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack);
        if (!this.getEquippedStack(equipmentSlot).isEmpty()) {
            return false;
        }
        return equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack);
    }

    public boolean canPickupItem(ItemStack stack) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAiDisabled()) {
            this.setAir(this.getMaxAir());
            return;
        }
        if (this.isWet()) {
            this.setMoistness(2400);
        } else {
            this.setMoistness(this.getMoistness() - 1);
            if (this.getMoistness() <= 0) {
                this.damage(this.getDamageSources().dryOut(), 1.0f);
            }
            if (this.isOnGround()) {
                this.setVelocity(this.getVelocity().add((this.random.nextFloat() * 2.0f - 1.0f) * 0.2f, 0.5, (this.random.nextFloat() * 2.0f - 1.0f) * 0.2f));
                this.setYaw(this.random.nextFloat() * 360.0f);
                this.setOnGround(false);
                this.velocityDirty = true;
            }
        }
        if (this.getWorld().isClient && this.isTouchingWater() && this.getVelocity().lengthSquared() > 0.03) {
            Vec3d vec3d = this.getRotationVec(0.0f);
            float f = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180)) * 0.3f;
            float g = MathHelper.sin(this.getYaw() * ((float) Math.PI / 180)) * 0.3f;
            float h = 1.2f - this.random.nextFloat() * 0.7f;
            for (int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3d.x * (double) h + (double) f, this.getY() - vec3d.y, this.getZ() - vec3d.z * (double) h + (double) g, 0.0, 0.0, 0.0);
                this.getWorld().addParticle(ParticleTypes.DOLPHIN, this.getX() - vec3d.x * (double) h - (double) f, this.getY() - vec3d.y, this.getZ() - vec3d.z * (double) h - (double) g, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return super.interactMob(player, hand);
    }

    @Override
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_DOLPHIN_SPLASH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_DOLPHIN_SWIM;
    }

    protected boolean isNearTarget() {
        BlockPos blockPos = this.getNavigation().getTargetPos();
        if (blockPos != null) {
            return blockPos.isWithinDistance(this.getPos(), 12.0);
        }
        return false;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(this.getMovementSpeed(), movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
            if (this.getTarget() == null) {
                this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(movementInput);
        }
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }
}
