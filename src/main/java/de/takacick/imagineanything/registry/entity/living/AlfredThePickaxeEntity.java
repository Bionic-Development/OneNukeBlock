package de.takacick.imagineanything.registry.entity.living;

import de.takacick.imagineanything.ImagineAnything;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AlfredThePickaxeEntity extends TameableEntity {

    public AlfredThePickaxeEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!world.isClient) {
            playSound(SoundEvents.ENTITY_ITEM_BREAK, 3.0f, 1.0f);
            for (int x = 0; x < 25; x++) {
                ((ServerWorld) world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.DIAMOND_PICKAXE.getDefaultStack()), getParticleX(getWidth()),
                        getRandomBodyY(),
                        getParticleZ(getWidth()), 1,
                        0.5,
                        0.5,
                        0.5, 0.2);
            }
        }

        this.discard();

        super.onDeath(damageSource);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.0, 15.0f, 2.0f, false));
        this.goalSelector.add(3, new BreakOreGoal(this));
        this.goalSelector.add(4, new WanderAroundGoal(this, 1.0));
    }

    @Override
    public boolean tryAttack(Entity target) {

        boolean bl = super.tryAttack(target);

        if (bl && target instanceof LivingEntity livingEntity) {
            playSound(SoundEvents.BLOCK_STONE_HIT, 2.0f, 1.0f);
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
        }

        return bl;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ITEM_BREAK;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void tickMovement() {
        this.tickHandSwing();
        super.tickMovement();
    }

    public class BreakOreGoal
            extends Goal {
        private static final int MIN_MAX_PROGRESS = 240;
        private BlockPos oreBlockPos;
        protected int breakProgress;
        protected int prevBreakProgress = -1;
        protected int maxProgress = 1;
        protected AlfredThePickaxeEntity mob;

        public BreakOreGoal(AlfredThePickaxeEntity mob) {
            this.mob = mob;
        }

        protected int getMaxProgress() {
            return Math.max(5, this.maxProgress);
        }

        @Override
        public boolean canStart() {
            if (!getNavigation().isIdle()) {
                return false;
            }

            HashMap<BlockPos, Path> paths = new HashMap<>();

            ImagineAnything.generateSphere(mob.getBlockPos(), 7, false).forEach(blockPos -> {
                BlockState blockState = world.getBlockState(blockPos);
                if ((blockState.getBlock() instanceof OreBlock || blockState.getBlock() instanceof RedstoneOreBlock) && !blockState.isOf(Blocks.SCULK)) {
                    Path path = getNavigation().findPathTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10);
                    if (path != null && path.getTarget().getSquaredDistance(blockPos) <= 2) {
                        paths.put(blockPos, path);
                    }
                }
            });

            var optional = paths.keySet().stream().min((o1, o2) -> Double.compare(o1.getSquaredDistance(mob.getBlockPos()),
                    o2.getSquaredDistance(mob.getBlockPos())));

            if (optional.isEmpty()) {
                return false;
            }

            oreBlockPos = optional.get();
            getNavigation().startMovingAlong(paths.get(oreBlockPos), 1.4);
            return true;
        }

        @Override
        public void start() {

            super.start();
            this.breakProgress = 0;
        }

        @Override
        public boolean shouldContinue() {
            return this.breakProgress <= this.getMaxProgress()
                    && this.oreBlockPos.isWithinDistance(this.mob.getPos(), 15.0)
                    && (world.getBlockState(oreBlockPos).getBlock() instanceof OreBlock
                    || world.getBlockState(oreBlockPos).getBlock() instanceof RedstoneOreBlock);
        }

        @Override
        public void stop() {
            super.stop();
            this.mob.world.setBlockBreakingInfo(this.mob.getId(), this.oreBlockPos, -1);
            this.oreBlockPos = null;
        }

        @Override
        public void tick() {
            super.tick();
            //     if (this.mob.getRandom().nextInt(20) == 0) {
            // this.mob.world.syncWorldEvent(WorldEvents.ZOMBIE_ATTACKS_WOODEN_DOOR, this.doorPos, 0);
            //   }
            if (this.oreBlockPos.isWithinDistance(this.mob.getPos(), 2.3) ||
                    this.oreBlockPos.isWithinDistance(this.mob.getPos().add(0, 1, 0), 2.3)) {
                getNavigation().stop();
                if (!this.mob.handSwinging) {
                    this.mob.swingHand(Hand.MAIN_HAND, true);
                    world.syncWorldEvent(132391834, this.oreBlockPos, 0);
                }
                lookControl.lookAt(oreBlockPos.getX() + 0.5, oreBlockPos.getY() + 0.5, oreBlockPos.getZ() + 0.5, getMaxLookPitchChange(), getMaxLookYawChange());

                ++this.breakProgress;
                int i = (int) ((float) this.breakProgress / (float) this.getMaxProgress() * 5.0f);
                if (i != this.prevBreakProgress) {
                    this.mob.world.setBlockBreakingInfo(this.mob.getId(), this.oreBlockPos, i);
                    this.prevBreakProgress = i;
                }
                if (this.breakProgress == this.getMaxProgress()) {
                    this.mob.world.breakBlock(this.oreBlockPos, true);
                }
            } else {
                getNavigation().startMovingAlong(getNavigation().findPathTo(oreBlockPos.getX() + 0.5, oreBlockPos.getY() + 0.5, oreBlockPos.getZ() + 0.5, 0), 1.4);
            }
        }
    }
}
