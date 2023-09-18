package de.takacick.onesuperblock.registry.entity.living;

import de.takacick.onesuperblock.registry.ItemRegistry;
import de.takacick.superitems.registry.ParticleRegistry;
import de.takacick.superitems.registry.particles.RainbowParticleEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class SuperVillagerEntity extends MerchantEntity {

    @Nullable
    private BlockPos wanderTarget;
    private int despawnDelay;

    public SuperVillagerEntity(EntityType<? extends SuperVillagerEntity> entityType, World world) {
        super(entityType, world);

    }

    public static DefaultAttributeContainer.Builder createVillagerAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5).add(EntityAttributes.GENERIC_MAX_HEALTH, 200).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new StopFollowingCustomerGoal(this));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, ZombieEntity.class, 8.0f, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, EvokerEntity.class, 12.0f, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, VindicatorEntity.class, 8.0f, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, VexEntity.class, 8.0f, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, PillagerEntity.class, 15.0f, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, IllusionerEntity.class, 12.0f, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, ZoglinEntity.class, 10.0f, 0.5, 0.5));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 0.5));
        this.goalSelector.add(1, new LookAtCustomerGoal(this));
        this.goalSelector.add(2, new WanderToTargetGoal(this, 2.0, 0.35));
        this.goalSelector.add(4, new GoToWalkTargetGoal(this, 0.35));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.35));
        this.goalSelector.add(9, new StopAndLookAtEntityGoal(this, PlayerEntity.class, 3.0f, 1.0f));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0f));
    }

    @Override
    @Nullable
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean isLeveledMerchant() {
        return false;
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.hasCustomer() && !this.isBaby()) {
            if (hand == Hand.MAIN_HAND) {
                player.incrementStat(Stats.TALKED_TO_VILLAGER);
            }
            if (this.getOffers().isEmpty()) {
                return ActionResult.success(this.world.isClient);
            }
            if (!this.world.isClient) {
                this.setCustomer(player);
                this.sendOffers(player, this.getDisplayName(), 1);
            }
            return ActionResult.success(this.world.isClient);
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected void fillRecipes() {
        TradeOffers.Factory[] factories = new TradeOffers.Factory[]{
                new SellItemFactory(new ItemStack(Items.IRON_BLOCK, 12), new ItemStack(Items.OAK_LOG, 32), Integer.MAX_VALUE, 8, 0),
                new SellItemFactory(new ItemStack(Items.DIAMOND_BLOCK, 24), new ItemStack(ItemRegistry.SUPER_ENCHANTER_BOOK), Integer.MAX_VALUE, 8, 0),
                new SellItemFactory(new ItemStack(Items.NETHERITE_BLOCK, 64), new ItemStack(Items.EMERALD_BLOCK, 32), new ItemStack(ItemRegistry.SUPER_PROTO), Integer.MAX_VALUE, 8, 0),
        };

        TradeOfferList tradeOfferList = this.getOffers();
        this.fillRecipesFromPool(tradeOfferList, factories, factories.length);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("DespawnDelay", this.despawnDelay);
        if (this.wanderTarget != null) {
            nbt.put("WanderTarget", NbtHelper.fromBlockPos(this.wanderTarget));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("DespawnDelay", NbtElement.NUMBER_TYPE)) {
            this.despawnDelay = nbt.getInt("DespawnDelay");
        }
        if (nbt.contains("WanderTarget")) {
            this.wanderTarget = NbtHelper.toBlockPos(nbt.getCompound("WanderTarget"));
        }
        this.setBreedingAge(Math.max(0, this.getBreedingAge()));
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    protected void afterUsing(TradeOffer offer) {
        if (offer.shouldRewardPlayerExperience()) {
            int i = 3 + this.random.nextInt(4);
            this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.getX(), this.getY() + 0.5, this.getZ(), i));
        }
    }

    @Override
    public void trade(TradeOffer offer) {
        super.trade(offer);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.hasCustomer()) {
            return SoundEvents.ENTITY_WANDERING_TRADER_TRADE;
        }
        return SoundEvents.ENTITY_WANDERING_TRADER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WANDERING_TRADER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WANDERING_TRADER_DEATH;
    }

    @Override
    protected SoundEvent getDrinkSound(ItemStack stack) {
        if (stack.isOf(Items.MILK_BUCKET)) {
            return SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK;
        }
        return SoundEvents.ENTITY_WANDERING_TRADER_DRINK_POTION;
    }

    @Override
    protected SoundEvent getTradingSound(boolean sold) {
        return sold ? SoundEvents.ENTITY_WANDERING_TRADER_YES : SoundEvents.ENTITY_WANDERING_TRADER_NO;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_WANDERING_TRADER_YES;
    }

    public void setDespawnDelay(int despawnDelay) {
        this.despawnDelay = despawnDelay;
    }

    public int getDespawnDelay() {
        return this.despawnDelay;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            this.tickDespawnDelay();
        }
    }

    private void tickDespawnDelay() {
        if (this.despawnDelay > 0 && !this.hasCustomer() && --this.despawnDelay == 0) {
            this.discard();
        }
    }

    public void setWanderTarget(@Nullable BlockPos wanderTarget) {
        this.wanderTarget = wanderTarget;
    }

    @Nullable
    BlockPos getWanderTarget() {
        return this.wanderTarget;
    }

    class WanderToTargetGoal
            extends Goal {
        final SuperVillagerEntity trader;
        final double proximityDistance;
        final double speed;

        WanderToTargetGoal(SuperVillagerEntity trader, double proximityDistance, double speed) {
            this.trader = trader;
            this.proximityDistance = proximityDistance;
            this.speed = speed;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public void stop() {
            this.trader.setWanderTarget(null);
            SuperVillagerEntity.this.navigation.stop();
        }

        @Override
        public boolean canStart() {
            BlockPos blockPos = this.trader.getWanderTarget();
            return blockPos != null && this.isTooFarFrom(blockPos, this.proximityDistance);
        }

        @Override
        public void tick() {
            BlockPos blockPos = this.trader.getWanderTarget();
            if (blockPos != null && SuperVillagerEntity.this.navigation.isIdle()) {
                if (this.isTooFarFrom(blockPos, 10.0)) {
                    Vec3d vec3d = new Vec3d((double) blockPos.getX() - this.trader.getX(), (double) blockPos.getY() - this.trader.getY(), (double) blockPos.getZ() - this.trader.getZ()).normalize();
                    Vec3d vec3d2 = vec3d.multiply(10.0).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
                    SuperVillagerEntity.this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, this.speed);
                } else {
                    SuperVillagerEntity.this.navigation.startMovingTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.speed);
                }
            }
        }

        private boolean isTooFarFrom(BlockPos pos, double proximityDistance) {
            return !pos.isWithinDistance(this.trader.getPos(), proximityDistance);
        }
    }

    static class SellItemFactory
            implements TradeOffers.Factory {
        private final ItemStack buy;
        private ItemStack secondBuy;
        private final ItemStack sell;
        private final int maxUses;
        private final int experience;
        private final int multiplier;

        public SellItemFactory(ItemStack buy, ItemStack sell, int maxUses, int experience, int multiplier) {
            this.buy = buy;
            this.sell = sell;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public SellItemFactory(ItemStack buy, ItemStack secondBuy, ItemStack sell, int maxUses, int experience, int multiplier) {
            this.buy = buy;
            this.secondBuy = secondBuy;
            this.sell = sell;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public TradeOffer create(Entity entity, Random random) {
            if (secondBuy == null) {
                return new TradeOffer(buy, sell, this.maxUses, this.experience, this.multiplier);
            }

            return new TradeOffer(buy, secondBuy, sell, this.maxUses, this.experience, this.multiplier);
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 60) {
            for (int i = 0; i < 20; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.world.addParticle(new RainbowParticleEffect(ParticleRegistry.RAINBOW_SMOKE, this.world.getRandom().nextInt(240000), false), this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }
    }
}

