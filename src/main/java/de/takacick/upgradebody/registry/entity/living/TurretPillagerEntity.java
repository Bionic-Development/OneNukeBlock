package de.takacick.upgradebody.registry.entity.living;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.ExperienceOrbProperties;
import de.takacick.upgradebody.registry.entity.living.ai.CrossbowAttackGoal;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class TurretPillagerEntity extends PillagerEntity implements Upgraded {

    public static final RegistryKey<DamageType> TURRET_PILLAGER = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(UpgradeBody.MOD_ID, "turret_pillager"));

    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.of("§6Turret Pillager §7[§aUpgrade§7]"), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true);

    public TurretPillagerEntity(EntityType<? extends PillagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {

        if (!getWorld().isClient) {
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        }

        super.tick();
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(3, new CrossbowAttackGoal<>(this, 1.0, 8.0f));
        this.goalSelector.add(8, new WanderAroundGoal(this, 0.6));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 15.0f, 1.0f));
        this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 15.0f));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!getWorld().isClient) {
            for (int i = 0; i < 4; i++) {
                dropItem(Items.EMERALD_BLOCK);
            }

            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, UpgradeBody.IDENTIFIER, 7);
            for (int i = getRandom().nextBetween(1500, 2000); i > 0; ) {
                int level = MathHelper.clamp(getRandom().nextBetween(50, 100), 0, i);
                i -= level;

                ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(getWorld(), getX(), getBodyY(0.5), getZ(), level);
                ((ExperienceOrbProperties) experienceOrbEntity).setLevelOrb(true);
                ((ExperienceOrbProperties) experienceOrbEntity).setCooldown(5);
                experienceOrbEntity.setVelocity(getRandom().nextGaussian() * 0.15, getRandom().nextDouble() * 0.25, getRandom().nextGaussian() * 0.15);
                getWorld().spawnEntity(experienceOrbEntity);
            }
        }

        super.onDeath(damageSource);
    }

    @Override
    public boolean canJoinRaid() {
        return false;
    }

    public static DefaultAttributeContainer.Builder createTurretPillagerAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35f).add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {

    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        super.attack(target, pullProgress);
    }

    public void shoot(LivingEntity entity, float speed) {
        Hand hand = ProjectileUtil.getHandPossiblyHolding(entity, Items.CROSSBOW);
        shootAll(entity.getWorld(), entity, hand, Items.CROSSBOW.getDefaultStack(), speed, 14 - entity.getWorld().getDifficulty().getId() * 4);
        this.postShoot();
    }

    private int shoot = 0;

    public void shootAll(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence) {
        float[] fs = getSoundPitches(entity.getRandom());
        shoot = -1;
        CrossbowItem.shoot(world, entity, Hand.MAIN_HAND, stack, Items.ARROW.getDefaultStack(), fs[0], true, speed, divergence, 0.0f);
        shoot = 1;
        CrossbowItem.shoot(world, entity, Hand.MAIN_HAND, stack, Items.ARROW.getDefaultStack(), fs[0], true, speed, divergence, 0.0f);
        shoot = -1;
    }

    @Override
    public void shoot(LivingEntity entity, LivingEntity target, ProjectileEntity projectile, float multishotSpray, float speed) {
        Vec3d vec3d = entity.getRotationVector(0, getYaw());
        double d = target.getX() - entity.getX();
        double e = target.getZ() - entity.getZ();
        double f = Math.sqrt(d * d + e * e);
        double g = target.getBodyY(0.3333333333333333) - projectile.getY() + f * (double) 0.2f;

        projectile.setPos(getHandX(shoot) + vec3d.getX() * 0.45, entity.getBodyY(0.65), getHandZ(shoot) + vec3d.getZ() * 0.45);

        Vector3f vector3f = this.getProjectileLaunchVelocity(entity, new Vec3d(d, g, e), multishotSpray);
        projectile.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, 14 - entity.getWorld().getDifficulty().getId() * 4);
        entity.playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, 1.0f, 1.0f / (entity.getRandom().nextFloat() * 0.4f + 0.8f));
    }

    private double getHandX(int handIndex) {
        float f = (this.bodyYaw + (float) (180)) * ((float) Math.PI / 180);
        float g = MathHelper.cos(f) * handIndex;
        return this.getX() + (double) g * 0.4;
    }

    private double getHandZ(int handIndex) {
        float f = (this.bodyYaw + (float) (180)) * ((float) Math.PI / 180);
        float g = MathHelper.sin(f) * handIndex;
        return this.getZ() + (double) g * 0.4;
    }

    private static float[] getSoundPitches(Random random) {
        boolean bl = random.nextBoolean();
        return new float[]{1.0f, getSoundPitch(bl, random), getSoundPitch(!bl, random)};
    }

    private static float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

}
