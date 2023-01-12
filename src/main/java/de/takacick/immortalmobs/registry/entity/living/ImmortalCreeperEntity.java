package de.takacick.immortalmobs.registry.entity.living;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.network.ImmortalExplosionHandler;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
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
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;

public class ImmortalCreeperEntity
        extends HostileEntity
        implements SkinOverlayOwner, ImmortalEntity {
    private static final TrackedData<Integer> FUSE_SPEED = DataTracker.registerData(ImmortalCreeperEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> CHARGED = DataTracker.registerData(ImmortalCreeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IGNITED = DataTracker.registerData(ImmortalCreeperEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public int lastFuseTime;
    public int currentFuseTime;
    private int fuseTime = 30;
    private int explosionRadius = 3;
    private int headsDropped;

    public ImmortalCreeperEntity(EntityType<? extends ImmortalCreeperEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new CreeperIgniteGoal(this));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, OcelotEntity.class, 6.0f, 1.0, 1.2));
        this.goalSelector.add(3, new FleeEntityGoal<>(this, CatEntity.class, 6.0f, 1.0, 1.2));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
    }

    public static DefaultAttributeContainer.Builder createCreeperAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getSource() instanceof LivingEntity attacker && attacker.getMainHandStack().isOf(ItemRegistry.IMMORTAL_SWORD)) {
            return super.damage(source, 100000);
        }

        if (amount < 10000) {
            boolean bl = super.damage(source, 0.1f);
            setHealth(getMaxHealth());
            return bl;
        }

        return super.damage(source, amount);
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
        this.dataTracker.startTracking(CHARGED, false);
        this.dataTracker.startTracking(IGNITED, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.dataTracker.get(CHARGED).booleanValue()) {
            nbt.putBoolean("powered", true);
        }
        nbt.putShort("Fuse", (short) this.fuseTime);
        nbt.putByte("ExplosionRadius", (byte) this.explosionRadius);
        nbt.putBoolean("ignited", this.isIgnited());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(CHARGED, nbt.getBoolean("powered"));
        if (nbt.contains("Fuse", NbtElement.NUMBER_TYPE)) {
            this.fuseTime = nbt.getShort("Fuse");
        }
        if (nbt.contains("ExplosionRadius", NbtElement.NUMBER_TYPE)) {
            this.explosionRadius = nbt.getByte("ExplosionRadius");
        }
        if (nbt.getBoolean("ignited")) {
            this.ignite();
        }
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            int i;
            this.lastFuseTime = this.currentFuseTime;
            if (this.isIgnited()) {
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
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        ImmortalCreeperEntity creeperEntity;
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        Entity entity = source.getAttacker();
        if (entity != this && entity instanceof ImmortalCreeperEntity && (creeperEntity = (ImmortalCreeperEntity) entity).shouldDropHead()) {
            creeperEntity.onHeadDropped();
            this.dropItem(Items.CREEPER_HEAD);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        return true;
    }

    @Override
    public boolean shouldRenderOverlay() {
        return this.dataTracker.get(CHARGED);
    }

    public float getClientFuseTime(float timeDelta) {
        return MathHelper.lerp(timeDelta, this.lastFuseTime, this.currentFuseTime) / (float) (this.fuseTime - 2);
    }

    public int getFuseSpeed() {
        return this.dataTracker.get(FUSE_SPEED);
    }

    public void setFuseSpeed(int fuseSpeed) {
        this.dataTracker.set(FUSE_SPEED, fuseSpeed);
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        super.onStruckByLightning(world, lightning);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player2, Hand hand) {
        ItemStack itemStack = player2.getStackInHand(hand);
        if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
            this.world.playSound(player2, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0f, this.random.nextFloat() * 0.4f + 0.8f);
            if (!this.world.isClient) {
                this.ignite();
                itemStack.damage(1, player2, player -> player.sendToolBreakStatus(hand));
            }
            return ActionResult.success(this.world.isClient);
        }
        return super.interactMob(player2, hand);
    }

    private void explode() {
        if (!this.world.isClient) {
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            this.dead = true;
            this.setFuseSpeed(-1);
            this.fuseTime = 30;
            ImmortalExplosionHandler.createExplosion((ServerWorld) world, this, null, null,
                    getX(), getBodyY(0.5), getZ(), 8f, false,
                    destructionType);

            this.getDataTracker().set(IGNITED, false);
            BionicUtils.sendEntityStatus((ServerWorld) world, this, ImmortalMobs.IDENTIFIER, 7);

            this.spawnEffectsCloud();
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!world.isClient) {
            for (int i = 0; i < random.nextBetween(3, 6); i++) {
                ImmortalItemEntity itemEntity = new ImmortalItemEntity(world, getX(), getBodyY(0.5), getZ(),
                        ItemRegistry.IMMORTAL_GUNPOWDER.getDefaultStack(),
                        world.getRandom().nextGaussian() * 0.2,
                        0.35, world.getRandom().nextGaussian() * 0.2);
                itemEntity.setGlowing(true);
                world.spawnEntity(itemEntity);
            }
            world.playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.PLAYERS, 1.0f, 1.0f);
            BionicUtils.sendEntityStatus((ServerWorld) world, this, ImmortalMobs.IDENTIFIER, 8);

            ((ServerWorld) world).spawnParticles(ParticleRegistry.IMMORTAL_EXPLOSION, getX(), getBodyY(0.5), getZ(), 5,
                    3.0D, 2.0D, 3.0D, 0.2);
            world.playSound(null, getX(), getBodyY(0.5), getZ(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1f, 1f);
        }

        super.onDeath(damageSource);
    }

    private void spawnEffectsCloud() {
        Collection<StatusEffectInstance> collection = this.getStatusEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.world, this.getX(), this.getY(), this.getZ());
            areaEffectCloudEntity.setRadius(2.5f);
            areaEffectCloudEntity.setRadiusOnUse(-0.5f);
            areaEffectCloudEntity.setWaitTime(10);
            areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
            areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
            for (StatusEffectInstance statusEffectInstance : collection) {
                areaEffectCloudEntity.addEffect(new StatusEffectInstance(statusEffectInstance));
            }
            this.world.spawnEntity(areaEffectCloudEntity);
        }
    }

    public boolean isIgnited() {
        return this.dataTracker.get(IGNITED);
    }

    public void ignite() {
        this.dataTracker.set(IGNITED, true);
    }

    public boolean shouldDropHead() {
        return this.shouldRenderOverlay() && this.headsDropped < 1;
    }

    public void onHeadDropped() {
        ++this.headsDropped;
    }

    public class CreeperIgniteGoal
            extends Goal {
        private final ImmortalCreeperEntity creeper;
        @Nullable
        private LivingEntity target;

        public CreeperIgniteGoal(ImmortalCreeperEntity creeper) {
            this.creeper = creeper;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.creeper.getTarget();
            return this.creeper.getFuseSpeed() > 0 || livingEntity != null && this.creeper.squaredDistanceTo(livingEntity) < 9.0;
        }

        @Override
        public void start() {
            this.creeper.getNavigation().stop();
            this.target = this.creeper.getTarget();
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.creeper.setFuseSpeed(-1);
                return;
            }
            if (this.creeper.squaredDistanceTo(this.target) > 49.0) {
                this.creeper.setFuseSpeed(-1);
                return;
            }
            if (!this.creeper.getVisibilityCache().canSee(this.target)) {
                this.creeper.setFuseSpeed(-1);
                return;
            }
            this.creeper.setFuseSpeed(1);
        }
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.ADD_DEATH_PARTICLES) {
            for (int i = 0; i < 20; ++i) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.world.addParticle(ParticleRegistry.IMMORTAL_POOF, this.getParticleX(1.0), this.getRandomBodyY(), this.getParticleZ(1.0), d, e, f);
            }
        } else {
            super.handleStatus(status);
        }
    }

}

