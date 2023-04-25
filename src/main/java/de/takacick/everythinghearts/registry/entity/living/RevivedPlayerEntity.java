package de.takacick.everythinghearts.registry.entity.living;

import de.takacick.everythinghearts.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RevivedPlayerEntity extends TameableEntity {

    public RevivedPlayerEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return TameableEntity.createMobAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0);
    }

    @Override
    public void tick() {

        this.tickHandSwing();

        super.tick();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.35, false));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.25, 10.0f, 2.0f, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.25));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {

        if (player.getStackInHand(hand).isOf(ItemRegistry.HEARTMOND)) {
            playSound(SoundEvents.ENTITY_VILLAGER_YES, 1, 1);
            if (!world.isClient) {
                player.sendMessage(Text.of("§f<Revived Player> I will serve you as my clan leader!"), false);
                player.getStackInHand(hand).decrement(1);
                setTamed(true);
                setOwner(player);
            }
            return ActionResult.success(this.world.isClient);
        }

        if (!isTamed()) {
            playSound(SoundEvents.ENTITY_VILLAGER_YES, 1, 1);
            if (!world.isClient) {
                player.sendMessage(Text.of("§f<Revived Player> Wow, thank you for reviving me! I forgot all my memories... but I am happy to once again be alive"), false);
            }
        }
        return ActionResult.success(this.world.isClient);
    }

    @Override
    public void copyFrom(Entity original) {
        super.copyFrom(original);
        getMainHandStack().setCount(0);
        getOffHandStack().setCount(0);
    }

    @Override
    public boolean tryAttack(Entity target) {
        this.swingHand(Hand.MAIN_HAND, true);
        return super.tryAttack(target);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public void setOwner(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerUuid(player.getUuid());
    }
}