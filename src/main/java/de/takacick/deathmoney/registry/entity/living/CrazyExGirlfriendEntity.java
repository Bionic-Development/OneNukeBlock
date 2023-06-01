package de.takacick.deathmoney.registry.entity.living;

import de.takacick.deathmoney.damage.DeathDamageSources;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.registry.particles.ColoredParticleEffect;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CrazyExGirlfriendEntity extends TameableEntity {

    public CrazyExGirlfriendEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public void tick() {

        if (!world.isClient) {
            LivingEntity livingEntity = getOwner();
            if (livingEntity != null) {
                setTarget(getOwner());
            }
        }

        this.tickHandSwing();

        super.tick();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.25, false));
        this.goalSelector.add(2, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(3, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::canTarget));
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {

    }

    @Override
    public boolean canTarget(LivingEntity target) {
        return (target instanceof PlayerEntity) && super.canTarget(target);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {

        equipStack(EquipmentSlot.MAINHAND, Items.IRON_SWORD.getDefaultStack());

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl;
        int i;
        float f = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        float g = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity) target).getGroup());
            g += (float) EnchantmentHelper.getKnockback(this);
        }
        if ((i = EnchantmentHelper.getFireAspect(this)) > 0) {
            target.setOnFireFor(i * 4);
        }
        if (bl = target.damage(DeathDamageSources.GIRLFRIEND_PUNCH, f)) {
            if (g > 0.0f && target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(g * 0.5f, MathHelper.sin(this.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(this.getYaw() * ((float) Math.PI / 180)));
                this.setVelocity(this.getVelocity().multiply(0.6, 1.0, 0.6));
            }
            if (target instanceof PlayerEntity playerEntity) {
                this.disablePlayerShield(playerEntity, this.getMainHandStack(), playerEntity.isUsingItem() ? playerEntity.getActiveItem() : ItemStack.EMPTY);
            }
            this.applyDamageEffects(this, target);
            this.onAttacking(target);
        }
        return bl;
    }

    private void disablePlayerShield(PlayerEntity player, ItemStack mobStack, ItemStack playerStack) {
        if (!mobStack.isEmpty() && !playerStack.isEmpty() && mobStack.getItem() instanceof AxeItem && playerStack.isOf(Items.SHIELD)) {
            float f = 0.25f + (float) EnchantmentHelper.getEfficiency(this) * 0.05f;
            if (this.random.nextFloat() < f) {
                player.getItemCooldownManager().set(Items.SHIELD, 100);
                this.world.sendEntityStatus(player, EntityStatuses.BREAK_SHIELD);
            }
        }
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 60) {
            for (int i = 0; i < 15; ++i) {
                double d = world.getRandom().nextGaussian() * 0.02;
                double e = world.getRandom().nextGaussian() * 0.02;
                double f = world.getRandom().nextGaussian() * 0.02;
                world.addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_POOF, new Vec3f(Vec3d.unpackRgb(0xFF1313))), getParticleX(1.0), getRandomBodyY(), getParticleZ(1.0), d, e, f);
            }
            world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, getSoundCategory(), 1.0f, 6.0f, false);
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public void setOwner(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerUuid(player.getUuid());
    }

}