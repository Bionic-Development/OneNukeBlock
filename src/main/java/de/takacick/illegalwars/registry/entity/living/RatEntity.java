package de.takacick.illegalwars.registry.entity.living;

import com.mojang.serialization.Codec;
import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.EffectRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.entity.living.brain.RatPounceAtTargetGoal;
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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.IntFunction;

public class RatEntity extends HostileEntity implements VariantHolder<RatEntity.Variant> {

    private float prevBodyPitch = 0f;
    private float bodyPitch = 0f;

    public static final String VARIANT_KEY = "Variant";
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(RatEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public RatEntity(EntityType<? extends RatEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Random random = world.getRandom();
        this.setVariant(Variant.getRandomNatural(random));
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new RatPounceAtTargetGoal(this, 0.4f));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.5, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {

        if (getWorld().isClient) {
            this.prevBodyPitch = this.bodyPitch;
            if (getVelocity().getY() > 0) {
                this.bodyPitch = Math.min(this.bodyPitch + 15, 45);
            } else {
                this.bodyPitch = Math.max(this.bodyPitch - 30, 0);
            }
        }

        super.tick();
    }

    @Override
    public boolean tryAttack(Entity target) {

        boolean bl = super.tryAttack(target);

        if (bl && target instanceof LivingEntity livingEntity) {
            if (getWorld() instanceof ServerWorld serverWorld) {
                BionicUtils.sendEntityStatus(serverWorld, livingEntity, IllegalWars.IDENTIFIER, 2);
            }
            livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEEDING, 60, 0, false, false, true));
        }

        return bl;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.13f;
    }

    public static DefaultAttributeContainer.Builder createRatAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ParticleRegistry.RAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ParticleRegistry.RAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15f, 1.0f);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        return super.damage(source, amount);
    }

    @Override
    public Variant getVariant() {
        return Variant.byId(this.dataTracker.get(VARIANT));
    }

    @Override
    public void setVariant(Variant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt(VARIANT_KEY, this.getVariant().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(Variant.byId(nbt.getInt(VARIANT_KEY)));
    }

    public float getBodyPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevBodyPitch, this.bodyPitch);
    }

    @Override
    protected BlockPos getStepSoundPos(BlockPos pos) {
        return super.getStepSoundPos(pos);
    }

    public static enum Variant implements StringIdentifiable {
        BROWN(0, "brown", true),
        WHITE(1, "white", true),
        BLACK(2, "black", true);

        private static final IntFunction<Variant> BY_ID;
        public static final Codec<Variant> CODEC;
        private final int id;
        private final String name;
        private final boolean natural;

        private Variant(int id, String name, boolean natural) {
            this.id = id;
            this.name = name;
            this.natural = natural;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Variant byId(int id) {
            return BY_ID.apply(id);
        }

        public static Variant getRandomNatural(Random random) {
            return Variant.getRandom(random, true);
        }

        private static Variant getRandom(Random random, boolean natural) {
            Variant[] variants = Arrays.stream(Variant.values()).filter(variant -> variant.natural == natural).toArray(Variant[]::new);
            return Util.getRandom(variants, random);
        }

        static {
            BY_ID = ValueLists.createIdToValueFunction(Variant::getId, Variant.values(), ValueLists.OutOfBoundsHandling.ZERO);
            CODEC = StringIdentifiable.createCodec(Variant::values);
        }
    }
}
