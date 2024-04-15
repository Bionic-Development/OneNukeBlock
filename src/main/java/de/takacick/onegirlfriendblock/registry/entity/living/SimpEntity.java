package de.takacick.onegirlfriendblock.registry.entity.living;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.registry.ItemRegistry;
import de.takacick.onegirlfriendblock.registry.entity.living.ai.FollowLoveGoal;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimpEntity extends TameableEntity {

    private static final List<Item> drops = List.of(Items.DIAMOND, ItemRegistry.HEART, Items.EMERALD, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.TOTEM_OF_UNDYING, Items.POPPY);
    private static final List<Text> messages = List.of(Text.literal("<Simp> P-p-please go out with me!"), Text.literal("<Simp> W-w-will you be my girlfriend?!"), Text.literal("<Simp> C-c-can I smell your toes?"), Text.literal("<Simp> C-c-can I have your phone number?"));
    private final AnimationState simpPleaseState = new AnimationState();
    private Text prevMessage;
    private int dropItemTicks = 20;
    private int startPlease = 0;

    public SimpEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0);
    }

    @Override
    protected void initGoals() {
        if (isTamed()) {
            this.goalSelector.add(1, new SwimGoal(this));
            this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2, false));
            this.goalSelector.add(2, new FollowOwnerGoal(this, 1.2D, 7.0f, 3.0f, false));
            this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            this.goalSelector.add(4, new LookAroundGoal(this));
            this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
            this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
            this.targetSelector.add(2, new AttackWithOwnerGoal(this));
            this.targetSelector.add(3, new ActiveTargetGoal<>(this, HostileEntity.class, true));
        } else {
            this.goalSelector.add(0, new SwimGoal(this));
            this.goalSelector.add(1, new FollowLoveGoal(this, 1, false));
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
    public void tick() {
        if(!getWorld().isClient) {
            if (getTarget() != null && (getTarget().isRemoved() || getTarget().isDead())) {
                setTarget(null);
            }
        }

        super.tick();
    }

    @Override
    public void tickMovement() {
        tickHandSwing();

        if (!isTamed()) {
            if (getTarget() != null && this.age % 2 == 0) {
                BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 10);
            }

            if (this.startPlease > 0) {
                this.startPlease--;
                if (this.startPlease <= 0) {
                    BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 9);
                }
            }
        }

        super.tickMovement();
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {

    }

    @Override
    public boolean damage(DamageSource source, float amount) {

        if (!getWorld().isClient && !isTamed()) {
            if (source.getSource() instanceof PlayerEntity playerEntity) {
                setTarget(null);
                setOwner(playerEntity);
                BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 11);

                Text message = Text.literal("<Simp> Y-y-you touched me! Thank you for gracing me, I shall now be your personal §nknight§f to protect you. §c<3");

                getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
                    serverPlayerEntity.sendMessage(message);
                });
                amount = 0f;
                source = getWorld().getDamageSources().generic();
            }
        }

        return super.damage(source, amount);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void setOwner(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerUuid(player.getUuid());
    }

    @Override
    public void setTamed(boolean tamed) {
        if (tamed) {
            equipStack(EquipmentSlot.MAINHAND, Items.IRON_SWORD.getDefaultStack());
        } else {
            equipStack(EquipmentSlot.MAINHAND, Items.AIR.getDefaultStack());
        }

        super.setTamed(tamed);

        this.goalSelector.goalsByControl.clear();
        this.targetSelector.goalsByControl.clear();
        this.clearGoalsAndTasks();
        this.targetSelector.clear(goal -> true);
        initGoals();
    }

    @Override
    public EntityView method_48926() {
        return getWorld();
    }

    public AnimationState getSimpPleaseState() {
        return this.simpPleaseState;
    }

    public void dropItem(LivingEntity livingEntity) {
        this.dropItemTicks--;
        if (this.dropItemTicks > 10) {
            return;
        }

        if (getMainHandStack().isEmpty()) {
            Item item = this.drops.get(getRandom().nextInt(this.drops.size()));
            ItemStack itemStack = item.getDefaultStack().copyWithCount(Math.min(getRandom().nextBetween(1, 4), item.getMaxCount()));
            equipStack(EquipmentSlot.MAINHAND, itemStack);
        }

        if (this.dropItemTicks > 0) {
            return;
        }
        double distance = Math.min(livingEntity.distanceTo(this), 2d);
        Vec3d vec3d = new Vec3d(0.2f, 0.3f, 0.2f).multiply(distance, 1, distance);
        LookTargetUtil.give(this, getMainHandStack().copy(), getPos().add(getRotationVector()), vec3d, 0.6f);
        getMainHandStack().setCount(0);

        Text message = getPleaseMessage();
        this.prevMessage = message;

        getServer().getPlayerManager().getPlayerList().forEach(serverPlayerEntity -> {
            serverPlayerEntity.sendMessage(message);
        });

        this.dropItemTicks = getRandom().nextBetween(20, 40);

        getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.NEUTRAL, 0.8f, 1f + getRandom().nextFloat() * 0.3f);

        swingHand(Hand.MAIN_HAND);
        this.startPlease = getRandom().nextDouble() <= 0.5 ? 5 : 0;
    }

    private Text getPleaseMessage() {
        Text message = this.messages.get(getRandom().nextInt(this.messages.size()));
        if (message.equals(this.prevMessage)) {
            return getPleaseMessage();
        }

        return message;
    }
}