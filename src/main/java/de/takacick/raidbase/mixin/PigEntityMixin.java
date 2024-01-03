package de.takacick.raidbase.mixin;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.PigProperties;
import de.takacick.raidbase.registry.entity.living.goals.*;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(PigEntity.class)
@Implements({@Interface(iface = PigProperties.class, prefix = "raidbase$")})
public abstract class PigEntityMixin extends AnimalEntity implements Tameable {

    @Shadow
    protected abstract void initGoals();

    @Shadow
    public abstract boolean isSaddled();

    @Shadow
    @Final
    private static TrackedData<Boolean> SADDLED;

    private static final TrackedData<Optional<UUID>> raidbase$OWNER_UUID = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "owner_uuid"), TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Boolean> raidbase$PIG_SOLDIER = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "pig_soldier"), TrackedDataHandlerRegistry.BOOLEAN);
    private final AnimationState raidbase$standUpState = new AnimationState();

    protected PigEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(raidbase$OWNER_UUID, Optional.empty());
        getDataTracker().startTracking(raidbase$PIG_SOLDIER, false);
    }

    @Inject(method = "createPigAttributes", at = @At("RETURN"), cancellable = true)
    private static void createPigAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.setReturnValue(info.getReturnValue().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK));
    }

    @Inject(method = "initGoals", at = @At("HEAD"), cancellable = true)
    public void initGoals(CallbackInfo info) {
        if (raidbase$isPigSoldier()) {
            this.goalSelector.add(1, new SwimGoal(this));
            this.goalSelector.add(2, new FollowOwnerGoal((PigEntity) (Object) this, 1f, 5.0f, 2.0f, true));
            this.goalSelector.add(2, new MeleeAttackGoal(this, 1.4f, true));
            this.goalSelector.add(3, new WanderAroundGoal(this, 1f));
            this.goalSelector.add(4, new LookAroundGoal(this));
            this.targetSelector.add(0, new TrackOwnerAttackerGoal((PigEntity) (Object) this));
            this.targetSelector.add(1, new AttackWithOwnerGoal((PigEntity) (Object) this));
            this.targetSelector.add(2, new ActiveTamedTargetGoal<>(this, PlayerEntity.class, true));
            info.cancel();
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if (raidbase$isPigSoldier()) {

            if (!player.getUuid().equals(getOwnerUuid())) {
                return;
            }

            ItemStack itemStack = player.getStackInHand(hand);
            ItemStack mainHandStack = getMainHandStack();

            if (!itemStack.isEmpty()) {

                ItemStack copy = itemStack.copy();
                copy.setCount(1);
                equipLootStack(EquipmentSlot.MAINHAND, copy.copy());
                setEquipmentDropChance(EquipmentSlot.MAINHAND, 1f);

                itemStack.decrement(1);

                if (!mainHandStack.isEmpty()) {
                    if (itemStack.isEmpty()) {
                        player.setStackInHand(hand, mainHandStack);
                    } else {
                        player.getInventory().offerOrDrop(mainHandStack);
                    }
                }

                playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f);
                playSound(SoundEvents.ENTITY_PIGLIN_CELEBRATE, 1f, 1.5f);

                if (getWorld().isClient) {
                    double g = getX();
                    double j = getZ();

                    for (int i = 0; i < 10; ++i) {
                        double h = getRandomBodyY();
                        double x = getRandom().nextGaussian() * 0.3;
                        double y = getRandom().nextGaussian() * 0.3;
                        double z = getRandom().nextGaussian() * 0.3;

                        getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, true, g + x, h + y, j + z, x * 0.4, y * 0.4, z * 0.4);
                    }
                }

                info.setReturnValue(ActionResult.SUCCESS);
            } else {
                if (!mainHandStack.isEmpty()) {
                    player.setStackInHand(hand, mainHandStack.copy());
                    mainHandStack.setCount(0);
                    playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f);
                    playSound(SoundEvents.ENTITY_PIG_AMBIENT, 1f, 1f);
                    info.setReturnValue(ActionResult.SUCCESS);
                }
            }
        } else {
            if (!getWorld().isClient) {
                ItemStack itemStack = player.getStackInHand(hand);

                if (itemStack.isOf(Items.TURTLE_HELMET)) {
                    raidbase$setSoldierOwner(player);
                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, RaidBase.IDENTIFIER, 2);
                    raidbase$setPigSoldier(true);

                    if (isSaddled()) {
                        getDataTracker().set(SADDLED, false);
                        dropItem(Items.SADDLE);
                    }

                    if (!player.isCreative()) {
                        itemStack.decrement(1);
                    }

                    getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20);
                    setHealth(getMaxHealth());
                    info.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (raidbase$isPigSoldier()) {
            nbt.putBoolean("raidbase$pigSoldier", raidbase$isPigSoldier());
        }

        if (this.getOwnerUuid() != null) {
            nbt.putUuid("raidbase$ownerUUID", this.getOwnerUuid());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("raidbase$pigSoldier", NbtElement.BYTE_TYPE)) {
            raidbase$setPigSoldier(nbt.getBoolean("raidbase$pigSoldier"));
        }

        UUID uUID = null;
        if (nbt.containsUuid("raidbase$ownerUUID")) {
            uUID = nbt.getUuid("raidbase$ownerUUID");
        }

        if (uUID != null) {
            try {
                this.dataTracker.set(raidbase$OWNER_UUID, Optional.of(uUID));
            } catch (Throwable throwable) {
                this.dataTracker.set(raidbase$OWNER_UUID, Optional.empty());
            }
        }
    }

    public AnimationState raidbase$getStandUpState() {
        return this.raidbase$standUpState;
    }

    public void raidbase$setPigSoldier(boolean pigSoldier) {
        boolean bl = raidbase$isPigSoldier();

        getDataTracker().set(raidbase$PIG_SOLDIER, pigSoldier);

        if (bl != pigSoldier) {
            this.clearGoalsAndTasks();
            this.targetSelector.clear(goal -> true);
            initGoals();
        }
    }

    public boolean raidbase$isPigSoldier() {
        return getDataTracker().get(raidbase$PIG_SOLDIER);
    }

    public void raidbase$setSoldierOwner(@Nullable LivingEntity owner) {
        this.dataTracker.set(raidbase$OWNER_UUID, owner == null ? Optional.empty() : Optional.of(owner.getUuid()));
        setTarget(null);
    }

    @Override
    public EntityView method_48926() {
        return super.getWorld();
    }

    @Nullable
    @Override
    public UUID getOwnerUuid() {
        return this.dataTracker.get(raidbase$OWNER_UUID).orElse(null);
    }

}
