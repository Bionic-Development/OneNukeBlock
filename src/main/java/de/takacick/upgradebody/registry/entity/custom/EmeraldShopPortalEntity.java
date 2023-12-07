package de.takacick.upgradebody.registry.entity.custom;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.UpgradeBodyClient;
import de.takacick.upgradebody.access.EntityProperties;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.registry.particles.ColoredParticleEffect;
import de.takacick.upgradebody.server.commands.EmeraldShopCommand;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EmeraldShopPortalEntity extends Entity {

    private static final TrackedData<Boolean> DEAD = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "shop_dead"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> PROGRESS = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "progress"), TrackedDataHandlerRegistry.INTEGER);
    private final int maxAnimationProgress = 30;

    public EmeraldShopPortalEntity(EntityType<?> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(DEAD, false);
        getDataTracker().startTracking(PROGRESS, 0);
    }

    public void pushAway(Entity entity) {
        if (entity.getType().equals(this.getType())) {
            return;
        }

        if (!(entity instanceof EntityProperties entityProperties)) {
            return;
        }

        if (entityProperties.isOnEmeraldShopPortalCooldown()) {
            entityProperties.setEmeraldShopPortalCooldown(10);
            return;
        }

        EmeraldShopPortalEntity emeraldShopPortalEntity = EmeraldShopCommand.EMERALD_SHOP_PORTAL;

        if (equals(emeraldShopPortalEntity)) {
            if (entityProperties.teleportBackFromEmeraldShop()) {
                BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
                getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
            }
        } else if (emeraldShopPortalEntity != null && emeraldShopPortalEntity.isAlive()) {
            BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                serverPlayerEntity.teleport((ServerWorld) emeraldShopPortalEntity.getWorld(), emeraldShopPortalEntity.getX(), emeraldShopPortalEntity.getY() + 0.01, emeraldShopPortalEntity.getZ(), emeraldShopPortalEntity.getYaw(), 0);
                serverPlayerEntity.getWorld().playSound(null, serverPlayerEntity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
            } else {
                getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                if (!getWorld().equals(emeraldShopPortalEntity.getWorld())) {
                    entity.moveToWorld((ServerWorld) emeraldShopPortalEntity.getWorld());
                }
                entity.requestTeleport(emeraldShopPortalEntity.getX(), emeraldShopPortalEntity.getY() + 0.01, emeraldShopPortalEntity.getZ());
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
            }
            entityProperties.setEmeraldShopPortal(getWorld(), getX(), getY() + 0.01, getZ(), getYaw(), false);
            entityProperties.setEmeraldShopPortalCooldown(10);
            BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void tick() {
        if (!getDataTracker().get(DEAD)) {
            if (getWorld().isClient && getProgress(0) < 1f) {
                for (int i = 0; i < 30; i++) {
                    double d = getWorld().getRandom().nextGaussian() * 0.02;
                    double e = getWorld().getRandom().nextGaussian() * 0.02;
                    double f = getWorld().getRandom().nextGaussian() * 0.02;
                    getWorld().addImportantParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.EMERALD_BLOCK.getDefaultState()), getX() + d * 13, getY() + e * 6, getZ() + f * 13, d, e, f);
                }

                for (int i = 0; i < 2; i++) {
                    double d = getWorld().getRandom().nextGaussian() * 0.08;
                    double e = getWorld().getRandom().nextGaussian() * 0.08;
                    double f = getWorld().getRandom().nextGaussian() * 0.08;
                    getWorld().addImportantParticle(ParticleRegistry.EMERALD_TOTEM, getX() + d * 13, getY() + e * 6, getZ() + f * 13, d, e, f);
                }
                for (int i = 0; i < 5; i++) {
                    double d = getWorld().getRandom().nextGaussian() * 0.08;
                    double e = getWorld().getRandom().nextGaussian() * 0.4;
                    double f = getWorld().getRandom().nextGaussian() * 0.08;
                    this.getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.TOTEM_DUST, UpgradeBodyClient.getEmeraldColor(getWorld().random)),
                            getX() + d * 13, getBodyY(Math.min(getWorld().getRandom().nextDouble(), this.getProgress(0.5f))) + e, getZ() + f * 13, d, e, f);
                }
                if (age % 2 == 0) {
                    getWorld().playSound(getParticleX(getWidth()), getY(), getParticleZ(getWidth()), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.3f, 1f + (float) getWorld().getRandom().nextDouble() * 0.3f, false);
                }

                if (age % 4 == 0) {
                    getWorld().playSound(getParticleX(getWidth()), getY(), getParticleZ(getWidth()), SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.BLOCKS, 0.3f, 1f + (float) getWorld().getRandom().nextDouble() * 0.3f, false);
                }
            }

            if (getAnimationProgress() >= maxAnimationProgress) {
                tickCramming();
            } else {
                setAnimationProgress(getAnimationProgress() + 1);
            }
        } else {
            setAnimationProgress(getAnimationProgress() - 2);

            if (getWorld().isClient && getProgress(0) > 0f) {
                for (int i = 0; i < 30; i++) {
                    double d = getWorld().getRandom().nextGaussian() * 0.02;
                    double e = getWorld().getRandom().nextGaussian() * 0.02;
                    double f = getWorld().getRandom().nextGaussian() * 0.02;
                    getWorld().addImportantParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.EMERALD_BLOCK.getDefaultState()), getX() + d * 13, getY() + e * 6, getZ() + f * 13, d, e, f);
                }

                for (int i = 0; i < 2; i++) {
                    double d = getWorld().getRandom().nextGaussian() * 0.08;
                    double e = getWorld().getRandom().nextGaussian() * 0.8;
                    double f = getWorld().getRandom().nextGaussian() * 0.08;
                    getWorld().addImportantParticle(ParticleRegistry.EMERALD_TOTEM, getX() + d * 13, getY() + e * 6, getZ() + f * 13, d, e, f);
                }

                if (age % 2 == 0) {
                    getWorld().playSound(getParticleX(getWidth()), getY(), getParticleZ(getWidth()), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 0.3f, 1f + (float) getWorld().getRandom().nextDouble() * 0.3f, false);
                }

                if (age % 4 == 0) {
                    getWorld().playSound(getParticleX(getWidth()), getY(), getParticleZ(getWidth()), SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.BLOCKS, 0.3f, 1f + (float) getWorld().getRandom().nextDouble() * 0.3f, false);
                }
            }

            if (!getWorld().isClient) {
                if (getAnimationProgress() <= 0) {
                    this.discard();
                }
            }
        }

        if (getWorld().isClient) {
            for (int i = 0; i < 1; i++) {
                getWorld().addParticle(ParticleRegistry.EMERALD_PORTAL, getX() + getWorld().getRandom().nextGaussian() * 0.25, getBodyY(0.6 + getWorld().getRandom().nextGaussian() * 0.25), getZ() + getWorld().getRandom().nextGaussian() * 0.25,
                        (float) getWorld().getRandom().nextGaussian() * 8, getWorld().getRandom().nextGaussian() * 8, getWorld().getRandom().nextGaussian() * 8);
            }
        } else {
            ((ServerWorld) getWorld()).getChunkManager().setChunkForced(getChunkPos(), true);
        }

        super.tick();
    }

    protected void tickCramming() {
        if (!getWorld().isClient) {
            List<Entity> list = getWorld().getOtherEntities(this, calculateBoundingBox());
            list.forEach(this::pushAway);
        }
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void onRemoved() {

        if (!getWorld().isClient) {
            ((ServerWorld) getWorld()).getChunkManager().setChunkForced(getChunkPos(), false);
        }

        super.onRemoved();
    }

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance / 2);
    }

    public void setAnimationProgress(int animationProgress) {
        getDataTracker().set(PROGRESS, animationProgress);
    }

    public int getAnimationProgress() {
        return getDataTracker().get(PROGRESS);
    }

    public int getMaxAnimationProgress() {
        return maxAnimationProgress;
    }

    public float getProgress(float delta) {
        return Math.max(Math.min(MathHelper.getLerpProgress(getAnimationProgress()
                + (getDataTracker().get(DEAD) ? delta * -2 : delta), 0, getMaxAnimationProgress()), 1f), 0f);
    }

    public void setDead(boolean dead) {
        if (getDataTracker().get(DEAD) != dead) {
            BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, UpgradeBody.IDENTIFIER, 8);
        }

        getDataTracker().set(DEAD, dead);
    }
}
