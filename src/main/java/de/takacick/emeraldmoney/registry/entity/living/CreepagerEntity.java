package de.takacick.emeraldmoney.registry.entity.living;

import de.takacick.emeraldmoney.EmeraldMoneyClient;
import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.emeraldmoney.registry.entity.custom.EmeraldShopPortalEntity;
import de.takacick.emeraldmoney.registry.entity.living.ai.AttackWithOwnerGoal;
import de.takacick.emeraldmoney.registry.entity.living.ai.CreeperIgniteGoal;
import de.takacick.emeraldmoney.registry.entity.living.ai.FollowOwnerGoal;
import de.takacick.emeraldmoney.registry.entity.living.ai.TrackOwnerAttackerGoal;
import de.takacick.emeraldmoney.registry.particles.ColoredParticleEffect;
import de.takacick.emeraldmoney.server.commands.EmeraldShopCommand;
import de.takacick.emeraldmoney.server.explosion.CreepagerExplosionHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class CreepagerEntity extends HostileEntity implements Tameable {

    private static final TrackedData<Integer> FUSE_SPEED = DataTracker.registerData(CreepagerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IGNITED = DataTracker.registerData(CreepagerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(CreepagerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private int lastFuseTime;
    private int currentFuseTime;
    private int fuseTime = 30;
    private int cooldown = 0;

    public CreepagerEntity(EntityType<? extends CreepagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new CreeperIgniteGoal(this));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(5, new FollowOwnerGoal(this, 1.0f, 10.0f, 3.0f, false));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new AttackWithOwnerGoal(this));
        this.targetSelector.add(2, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createCreeperAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 60);
    }

    @Override
    public int getSafeFallDistance() {
        if (this.getTarget() == null) {
            return 3;
        }
        return 3 + (int) (this.getHealth() - 1.0f);
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        boolean bl = super.handleFallDamage(fallDistance, damageMultiplier, damageSource);
        this.currentFuseTime += (int) (fallDistance * 1.5f);
        if (this.currentFuseTime > this.fuseTime - 5) {
            this.currentFuseTime = this.fuseTime - 5;
        }
        return bl;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FUSE_SPEED, -1);
        this.dataTracker.startTracking(IGNITED, false);
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putShort("Fuse", (short) this.fuseTime);
        nbt.putBoolean("ignited", this.isIgnited());
        if (this.getOwnerUuid() != null) {
            nbt.putUuid("Owner", this.getOwnerUuid());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Fuse", NbtElement.NUMBER_TYPE)) {
            this.fuseTime = nbt.getShort("Fuse");
        }
        if (nbt.getBoolean("ignited")) {
            this.ignite();
        }

        UUID uUID;
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) {
            uUID = nbt.getUuid("Owner");
        } else {
            String string = nbt.getString("Owner");
            uUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }
        if (uUID != null) {
            try {
                this.setOwnerUuid(uUID);
            } catch (Throwable throwable) {

            }
        }
    }

    @Override
    public boolean isTeammate(Entity other) {
        LivingEntity livingEntity = this.getOwner();
        if (other == livingEntity) {
            return true;
        }
        if (livingEntity != null) {
            return livingEntity.isTeammate(other);
        }
        return super.isTeammate(other);
    }

    @Override
    public void tick() {
        if (this.cooldown > 0) {
            this.cooldown--;
        }

        if (this.isAlive()) {
            EmeraldShopPortalEntity emeraldShopPortalEntity = EmeraldShopCommand.EMERALD_SHOP_PORTAL;
            if (!getWorld().isClient && emeraldShopPortalEntity != null && emeraldShopPortalEntity.getWorld().equals(getWorld()) &&
                    distanceTo(emeraldShopPortalEntity) <= 150) {
                this.setFuseSpeed(-1);
                this.getDataTracker().set(IGNITED, false);
            } else {
                int i;
                this.lastFuseTime = this.currentFuseTime;
                if (this.isIgnited() && !getWorld().isClient) {
                    this.setFuseSpeed(1);
                }
                if ((i = this.getFuseSpeed()) > 0 && this.currentFuseTime == 0) {
                    this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.5f);
                    this.emitGameEvent(GameEvent.PRIME_FUSE);
                }
                this.currentFuseTime += i;
                if (this.currentFuseTime < 0) {
                    this.currentFuseTime = 0;
                }
                if (this.currentFuseTime >= this.fuseTime) {
                    this.currentFuseTime = this.fuseTime;
                    this.explode();
                }
            }
        }
        super.tick();
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof GoatEntity) {
            return;
        }
        super.setTarget(target);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return true;
    }

    public float getClientFuseTime(float timeDelta) {
        return MathHelper.lerp(timeDelta, (float) this.lastFuseTime, (float) this.currentFuseTime) / (float) (this.fuseTime - 2);
    }

    public int getFuseSpeed() {
        return this.dataTracker.get(FUSE_SPEED);
    }

    public void setFuseSpeed(int fuseSpeed) {
        this.dataTracker.set(FUSE_SPEED, fuseSpeed);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isIn(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent soundEvent = itemStack.isOf(Items.FIRE_CHARGE) ? SoundEvents.ITEM_FIRECHARGE_USE : SoundEvents.ITEM_FLINTANDSTEEL_USE;
            this.getWorld().playSound(player, this.getX(), this.getY(), this.getZ(), soundEvent, this.getSoundCategory(), 1.0f, this.random.nextFloat() * 0.4f + 0.8f);
            if (!this.getWorld().isClient) {
                this.cooldown = 0;
                this.ignite();
                if (!itemStack.isDamageable()) {
                    itemStack.decrement(1);
                } else {
                    itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
                }
            }
            return ActionResult.success(this.getWorld().isClient);
        }
        return super.interactMob(player, hand);
    }

    private void explode() {
        if (!this.getWorld().isClient) {
            CreepagerExplosionHandler.createExplosion((ServerWorld) getWorld(), this, null, null,
                    getX(), getY(), getZ(), 3f, false, Explosion.DestructionType.DESTROY);
            this.getDataTracker().set(IGNITED, false);
            this.getDataTracker().set(FUSE_SPEED, -100);
            this.cooldown = getWorld().getRandom().nextBetween(10, 20);
        }
    }

    public boolean isIgnited() {
        return this.dataTracker.get(IGNITED);
    }

    public void ignite() {
        this.dataTracker.set(IGNITED, true);
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    @Override
    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void setOwner(PlayerEntity player) {
        this.setOwnerUuid(player.getUuid());
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        if (this.isOwner(target)) {
            return false;
        }

        EmeraldShopPortalEntity emeraldShopPortalEntity = EmeraldShopCommand.EMERALD_SHOP_PORTAL;
        if (emeraldShopPortalEntity != null && emeraldShopPortalEntity.getWorld().equals(getWorld()) &&
                distanceTo(emeraldShopPortalEntity) <= 150) {
            return false;
        }

        return super.canTarget(target);
    }

    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner) {
        return true;
    }

    @Override
    public EntityView method_48926() {
        return super.getWorld();
    }

    public void playSpawnEffects() {
        if (this.getWorld().isClient) {
            for (int i = 0; i < 15; ++i) {
                double g = getX();
                double h = getRandomBodyY();
                double j = getZ();

                double d = random.nextGaussian() * 0.3;
                double e = random.nextGaussian() * 0.3;
                double f = random.nextGaussian() * 0.3;

                getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.DUST, EmeraldMoneyClient.getEmeraldColor(random)),
                        true, g + d, h + e, j + f, d, e, f);
            }
            for (int i = 0; i < 10; ++i) {
                double d = random.nextGaussian() * 0.02;
                double e = random.nextGaussian() * 0.02;
                double f = random.nextGaussian() * 0.02;
                getWorld().addParticle(ParticleTypes.HEART, getParticleX(1.0),
                        getBodyY(0.5 + getRandom().nextDouble() * 0.5) + 0.25, getParticleZ(1.0), d, e, f);
            }
            for (int i = 0; i < 15; ++i) {
                double d = random.nextGaussian() * 0.02;
                double e = random.nextGaussian() * 0.02;
                double f = random.nextGaussian() * 0.02;
                getWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, getParticleX(1.0),
                        getRandomBodyY(), getParticleZ(1.0), d, e, f);
            }

            getWorld().playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.PLAYERS, 1.4f, 1f, true);
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_SPAWN_EFFECTS);
        }
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        if (this.isSleeping()) {
            return null;
        }
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    public int getCooldown() {
        return cooldown;
    }
}

