package de.takacick.emeraldmoney.registry.entity.custom;

import de.takacick.emeraldmoney.EmeraldMoneyClient;
import de.takacick.emeraldmoney.registry.EntityRegistry;
import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.emeraldmoney.registry.particles.ColoredParticleEffect;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class VillagerSpikeEntity extends Entity implements VillagerDataContainer {
    private static final TrackedData<VillagerData> VILLAGER_DATA = DataTracker.registerData(VillagerSpikeEntity.class, TrackedDataHandlerRegistry.VILLAGER_DATA);

    private int warmup;
    private boolean startedAttack;
    private int ticksLeft = 10;
    private boolean playingAnimation = false;
    private boolean attacked = false;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUuid;
    private Vec3d knockback;

    public VillagerSpikeEntity(EntityType<? extends VillagerSpikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public VillagerSpikeEntity(World world, double x, double y, double z, float yaw, float pitch, int warmup, LivingEntity owner) {
        this(EntityRegistry.VILLAGER_SPIKE, world);
        this.warmup = warmup;
        this.setOwner(owner);
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setPosition(x, y, z);
        this.ticksLeft = 50;
    }

    public static VillagerSpikeEntity create(EntityType<VillagerSpikeEntity> entityType, World world) {
        return new VillagerSpikeEntity(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    public void setKnockback(Vec3d knockback) {
        this.knockback = knockback;
    }

    @Nullable
    public LivingEntity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld && (entity = ((ServerWorld) this.getWorld()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (LivingEntity) entity;
        }
        return this.owner;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.warmup = nbt.getInt("Warmup");
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Warmup", this.warmup);
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            if (this.playingAnimation) {
                --this.ticksLeft;
                if (this.getAnimationProgress(0.5f) < 1f) {
                    for (int i = 0; i < 7; ++i) {
                        double d = this.getX() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                        double e = this.getY() + 0.1 * this.random.nextDouble();
                        double f = this.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                        double g = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                        double h = 0.3 + this.random.nextDouble() * 0.3;
                        double j = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                        this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.EMERALD_BLOCK.getDefaultState()),
                                d, e, f, g, h, j);
                    }
                    for (int i = 0; i < 2; ++i) {
                        double d = this.getX() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                        double e = this.getY() + 0.1 * this.random.nextDouble();
                        double f = this.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                        double g = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                        double h = 0.3 + this.random.nextDouble() * 0.3;
                        double j = (this.random.nextDouble() * 2.0 - 1.0) * 0.3;
                        getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.TOTEM_DUST, EmeraldMoneyClient.getEmeraldColor(random)),
                                d, e, f, g, h * 2, j);
                    }
                }
            }
        } else if (--this.warmup <= 0) {
            if (this.warmup == 0) {
                getWorld().sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
            }
            if (this.warmup <= -2 || (this.age == 2)) {
                List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, getBoundingBox().expand(0.1, 0.0, 0.1));

                for (LivingEntity livingEntity : list) {
                    this.damage(livingEntity);
                }

                attacked = true;
            }
            if (!this.startedAttack) {
                this.startedAttack = true;
            }
            if (--this.ticksLeft < 6) {
                this.discard();
            }
        }
    }

    @Override
    public void onRemoved() {
        if (getWorld().isClient) {
            Vec3d vec3d = getRotationVector(getPitch() - 90, 90 - getYaw()).multiply(4);
            this.getWorld().playSound(this.getX(), this.getY() + 0.2, this.getZ(), SoundEvents.ENTITY_VILLAGER_DEATH,
                    this.getSoundCategory(), 1f, 1f, true);

            for (int i = 0; i < 5; ++i) {
                Vec3d vec3d1 = vec3d.multiply(i / 12f);
                for (int x = 0; x < 2; x++) {
                    double d = this.getX() + vec3d1.getX() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                    double e = this.getY() + vec3d1.getY() + 0.1 * this.random.nextDouble();
                    double f = this.getZ() + vec3d1.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                    double g = 0.15 * this.random.nextGaussian();
                    double h = this.random.nextGaussian() * 0.3;
                    double j = 0.15 * this.random.nextGaussian();
                    this.getWorld().addParticle(ParticleRegistry.EMERALD_POOF,
                            d, e, f, g, h, j);
                }

                for (int x = 0; x < 2; x++) {
                    double d = this.getX() + vec3d1.getX() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                    double e = this.getY() + vec3d1.getY() + 0.1 * this.random.nextDouble();
                    double f = this.getZ() + vec3d1.getZ() + (this.random.nextDouble() * 2.0 - 1.0) * (double) this.getWidth() * 0.5;
                    double g = 0.15 * this.random.nextGaussian();
                    double h = this.random.nextGaussian() * 0.3;
                    double j = 0.15 * this.random.nextGaussian();
                    getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.TOTEM_DUST, EmeraldMoneyClient.getEmeraldColor(random)),
                            d, e, f, g, h, j);
                }
            }
        }

        super.onRemoved();
    }

    private void damage(LivingEntity target) {
        LivingEntity livingEntity = this.getOwner();
        if (!target.isAlive() || target == livingEntity) {
            return;
        }

        if (livingEntity == null) {
            if (!this.attacked && knockback != null) {
                target.addVelocity(knockback.getX(), knockback.getY(), knockback.getZ());
                target.velocityModified = true;
            }
            target.damage(getWorld().getDamageSources().generic(), 7);
        } else {
            if (livingEntity.isTeammate(target)) {
                return;
            }

            if (!this.attacked && knockback != null) {
                target.addVelocity(knockback.getX(), knockback.getY(), knockback.getZ());
                target.velocityModified = true;
            }
            target.damage(getWorld().getDamageSources().generic(), 7);
        }
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            this.playingAnimation = true;
            if (!this.isSilent()) {
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_VILLAGER_HURT, this.getSoundCategory(), 1.0f, this.random.nextFloat() * 0.2f + 0.85f, false);
            }
        }
    }

    public float getAnimationProgress(float tickDelta) {
        if (!this.playingAnimation) {
            return 0.0f;
        }
        int i = this.ticksLeft - 2;
        if (i <= 0) {
            return 1.0f;
        }
        return 1.0f - ((float) i - tickDelta) / 6.0f;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public VillagerData getVillagerData() {
        return getDataTracker().get(VILLAGER_DATA);
    }

    @Override
    public void setVillagerData(VillagerData villagerData) {
        getDataTracker().set(VILLAGER_DATA, villagerData);
    }
}