package de.takacick.onegirlfriendblock.registry.entity.living;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import de.takacick.onegirlfriendblock.registry.entity.living.ai.FollowBuffLoveGoal;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class BuffChadVillagerEntity extends IronGolemEntity {

    private static final List<Text> loveMessages = Stream.of(
                    "Hey §bbeautiful§f, like my §6muscles§f?",
                    "Hey §bgorgeous§f, need a §6strong man §fto protect you?",
                    "Let me §6rizz §fyou up §bshawty§f!"
            ).map(literal -> Text.of("<Buff Villager Chad> " + literal))
            .toList();

    private static final List<Text> angryMessages = Stream.of(
                    "You're §cugly anyways§f!",
                    "You dare §creject §fme? §cDIE!",
                    "Not into §6big buff men§f? You'll §cregret §fit!"
            ).map(literal -> Text.of("<Buff Villager Chad> " + literal))
            .toList();

    private UUID targetUuid;
    private int angryTicks = 0;
    private int animationDelay = 5;
    private boolean angry = false;
    private int loveDelay = 0;

    private final AnimationState rightArmFlexingState = new AnimationState();
    private final AnimationState leftArmFlexingState = new AnimationState();
    private final AnimationState bodyFlexingState = new AnimationState();
    private final AnimationState chestFlexingState = new AnimationState();

    public BuffChadVillagerEntity(EntityType<? extends IronGolemEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createIronGolemAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void initGoals() {
        if (isAngry()) {
            this.goalSelector.add(1, new SwimGoal(this));
            this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, true));
            this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            this.goalSelector.add(4, new LookAroundGoal(this));
            this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
            this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, livingEntity -> livingEntity.getUuid().equals(this.targetUuid)));
        } else {
            this.goalSelector.add(0, new SwimGoal(this));
            this.goalSelector.add(1, new FollowBuffLoveGoal(this, 1, false));
            this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
            this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 10.0f));
            this.goalSelector.add(8, new LookAroundGoal(this));
            this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        }

        if (this.age > 1) {
            this.getWorld().getProfiler().push("targetSelector");
            this.targetSelector.tick();
            this.getWorld().getProfiler().pop();
            this.getWorld().getProfiler().push("goalSelector");
            this.goalSelector.tick();
            this.getWorld().getProfiler().pop();
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {

        if (!getWorld().isClient && !isAngry()) {
            if (target instanceof PlayerEntity && !target.equals(getTarget())) {
                BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 12 + getRandom().nextInt(4));
                this.animationDelay = getRandom().nextBetween(20, 60);
                if (this.loveDelay <= 0) {
                    Text message = loveMessages.get(getRandom().nextInt(loveMessages.size()));
                    getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                        serverPlayerEntity.sendMessage(message);
                    });
                }
                this.loveDelay = 20;
            }
        }

        super.setTarget(target);
    }

    @Override
    public void tick() {
        if (!getWorld().isClient) {
            if (getTarget() != null && (getTarget().isRemoved() || getTarget().isDead())) {
                setTarget(null);
            }
            if (this.age > 1) {
                this.getWorld().getProfiler().push("targetSelector");
                this.targetSelector.tick();
                this.getWorld().getProfiler().pop();
            }

            if (this.loveDelay > 0) {
                this.loveDelay--;
            }
        }

        if (isAngry()) {
            if (getTarget() == null) {
                if (this.angryTicks > 0) {
                    this.angryTicks--;
                } else {
                    setAngry(false);
                }
            }
        }

        super.tick();
    }

    @Override
    public void tickMovement() {
        tickHandSwing();

        if (!isAngry()) {
            if (getTarget() != null) {
                if (this.age % 2 == 0) {
                    BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 10);
                }

                if (this.animationDelay > 0) {
                    this.animationDelay--;
                } else if (getRandom().nextDouble() <= 0.5f) {
                    BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 12 + getRandom().nextInt(4));
                    this.animationDelay = getRandom().nextBetween(20, 60);
                }
            }
        } else {
            if (this.random.nextInt(1000) < this.ambientSoundChance++) {
                this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_VILLAGER_NO, getSoundCategory(), 1f, 0.5f + getRandom().nextFloat() * 0.2f);
            }

            if (this.age % 2 == 0) {
                BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 17);
            }
        }

        super.tickMovement();
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {

    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {

        dropItem(ItemRegistry.HEART);

        super.dropLoot(damageSource, causedByPlayer);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (!getWorld().isClient && !isAngry()) {
            if (source.getSource() instanceof PlayerEntity playerEntity) {
                this.targetUuid = playerEntity.getUuid();
                this.angryTicks = 600;
                BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 16);

                Text message = angryMessages.get(getRandom().nextInt(angryMessages.size()));

                getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    serverPlayerEntity.sendMessage(message);
                });
                amount = 0f;
                source = getWorld().getDamageSources().generic();
                setAngry(true);
            }
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return target.canTakeDamage();
    }

    public void setAngry(boolean angry) {
        this.angry = angry;

        this.goalSelector.goalsByControl.clear();
        this.targetSelector.goalsByControl.clear();

        this.clearGoals(goal -> true);
        this.targetSelector.clear(goal -> true);
        initGoals();
    }

    public boolean isAngry() {
        return this.angry;
    }

    public AnimationState getRightArmFlexingState() {
        return this.rightArmFlexingState;
    }

    public AnimationState getLeftArmFlexingState() {
        return this.leftArmFlexingState;
    }

    public AnimationState getBodyFlexingState() {
        return this.bodyFlexingState;
    }

    public AnimationState getChestFlexingState() {
        return this.chestFlexingState;
    }
}