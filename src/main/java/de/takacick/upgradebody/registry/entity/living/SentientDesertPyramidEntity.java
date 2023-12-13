package de.takacick.upgradebody.registry.entity.living;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.ExperienceOrbProperties;
import de.takacick.upgradebody.registry.entity.projectiles.TntEntity;
import de.takacick.utils.BionicUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SentientDesertPyramidEntity extends MobEntity implements RangedAttackMob, Upgraded{

    private final ServerBossBar bossBar = (ServerBossBar) new ServerBossBar(Text.of("§eSentient Desert Upgrade §a[§7Upgrade§a]"), BossBar.Color.BLUE, BossBar.Style.PROGRESS).setDarkenSky(true);
    private int delay = 0;

    public SentientDesertPyramidEntity(EntityType<? extends SentientDesertPyramidEntity> entityType, World world) {
        super(entityType, world);

        this.ignoreCameraFrustum = true;
        this.setNoGravity(true);

        this.setStepHeight(1.2f);
        this.setHealth(this.getMaxHealth());
        this.getNavigation().setCanSwim(true);
    }

    @Override
    protected void initGoals() {

    }

    @Override
    public void tick() {
        super.tick();

        if (!getWorld().isClient) {
            this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
            if (this.delay > 0) {
                this.delay--;
            } else {
                PlayerEntity target = getWorld().getClosestEntity(PlayerEntity.class,
                        TargetPredicate.createAttackable().setPredicate(livingEntity ->
                                (livingEntity instanceof HostileEntity || livingEntity instanceof PlayerEntity)
                                        && !this.isTeammate(livingEntity)
                                        && Math.abs(livingEntity.getPos().add(0, livingEntity.getHeight() * 0.4, 0)
                                        .subtract(getPos().add(0, getHeight() / 2f, 0)).normalize().getY()) < 0.95
                                        && this.canSee(livingEntity)),
                        null, getX(), getBodyY(0.5), getZ(),
                        getBoundingBox().expand(20));

                if (target != null) {
                    playSound(SoundEvents.BLOCK_LEVER_CLICK, 1f, 1f);
                    attack(target, 1f);
                }
            }
        }
    }

    @Override
    public void slowMovement(BlockState state, Vec3d multiplier) {
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0).add(EntityAttributes.GENERIC_ARMOR, 4.0);
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {

    }

    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }

    @Override
    public void setVelocity(Vec3d velocity) {

    }

    @Override
    protected void pushAway(Entity entity) {
        super.pushAway(entity);
    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        TntEntity tntEntity = new TntEntity(getWorld(), getX(), getBodyY(0.5), getZ(), this);
        Vec3d pos = target.getPos().subtract(tntEntity.getPos());
        tntEntity.setVelocity(pos.getX(), pos.getY(), pos.getZ(), 2.6f, 1f);
        getWorld().spawnEntity(tntEntity);
        tntEntity.playSound(SoundEvents.ENTITY_TNT_PRIMED, 1f, 1f);

        this.delay = 10;
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

    @Nullable
    public ItemEntity dropStack(ItemStack stack, float yOffset) {
        if (stack.isEmpty()) {
            return null;
        }
        if (this.getWorld().isClient) {
            return null;
        }

        Vec3d vec3d = new Vec3d(getRandom().nextGaussian(), getRandom().nextDouble(), getRandom().nextGaussian());

        ItemEntity itemEntity = new ItemEntity(this.getWorld(),
                this.getX() + vec3d.getX() * 5, this.getY() + (double) yOffset + vec3d.getY() * 5, this.getZ() + getZ()  * 5, stack, vec3d.getX(), vec3d.getY(), vec3d.getZ());
        itemEntity.setToDefaultPickupDelay();
        this.getWorld().spawnEntity(itemEntity);
        return itemEntity;
    }

    @Override
    public boolean isInsideWall() {
        return false;
    }

    @Override
    public void onDeath(DamageSource damageSource) {

        if (!getWorld().isClient) {
            for (int i = 0; i < getRandom().nextBetween(1, 2); i++) {
                dropItem(Items.ENCHANTED_GOLDEN_APPLE);
            }
            for (int i = 0; i < getRandom().nextBetween(2, 4); i++) {
                dropItem(Items.GOLDEN_APPLE);
            }
            for (int i = 0; i < 12; i++) {
                dropItem(Items.DIAMOND);
            }
            for (int i = 0; i < 32; i++) {
                dropItem(Items.IRON_INGOT);
                dropItem(Items.GOLD_INGOT);
            }

            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, UpgradeBody.IDENTIFIER, 8);
            for (int i = getRandom().nextBetween(9000, 10000); i > 0; ) {
                int level = MathHelper.clamp(getRandom().nextBetween(200, 300), 0, i);
                i -= level;

                ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(getWorld(), getX(), getBodyY(0.5), getZ(), level);
                ((ExperienceOrbProperties) experienceOrbEntity).setLevelOrb(true);
                ((ExperienceOrbProperties) experienceOrbEntity).setCooldown(5);
                experienceOrbEntity.setVelocity(getRandom().nextGaussian() * 0.35, getRandom().nextDouble() * 0.35, getRandom().nextGaussian() * 0.35);
                getWorld().spawnEntity(experienceOrbEntity);
            }

            this.discard();
        }

        super.onDeath(damageSource);
    }
}
