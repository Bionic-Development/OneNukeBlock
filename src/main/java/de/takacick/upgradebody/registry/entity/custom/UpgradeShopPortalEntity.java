package de.takacick.upgradebody.registry.entity.custom;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.EntityProperties;
import de.takacick.upgradebody.registry.ParticleRegistry;
import de.takacick.upgradebody.server.commands.UpgradeBodyShopCommand;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class UpgradeShopPortalEntity extends Entity {

    private static final TrackedData<Boolean> DEAD = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "shop_dead"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> PROGRESS = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "progress"), TrackedDataHandlerRegistry.INTEGER);
    private final int maxAnimationProgress = 15;

    public UpgradeShopPortalEntity(EntityType<?> type, World world) {
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

        UpgradeShopPortalEntity upgradeShopPortalEntity = UpgradeBodyShopCommand.UPGRADE_SHOP_PORTAL;

        if (equals(upgradeShopPortalEntity)) {
            if (entityProperties.teleportBackFromEmeraldShop()) {
                BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
                getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
            }
        } else if (upgradeShopPortalEntity != null && upgradeShopPortalEntity.isAlive()) {
            BionicUtils.sendEntityStatus((ServerWorld) entity.getWorld(), entity, UpgradeBody.IDENTIFIER, 9);
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                serverPlayerEntity.teleport((ServerWorld) upgradeShopPortalEntity.getWorld(), upgradeShopPortalEntity.getX(), upgradeShopPortalEntity.getY() + 0.01, upgradeShopPortalEntity.getZ(), upgradeShopPortalEntity.getYaw(), 0);
                serverPlayerEntity.getWorld().playSound(null, serverPlayerEntity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
            } else {
                getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                if (!getWorld().equals(upgradeShopPortalEntity.getWorld())) {
                    entity.moveToWorld((ServerWorld) upgradeShopPortalEntity.getWorld());
                }
                entity.requestTeleport(upgradeShopPortalEntity.getX(), upgradeShopPortalEntity.getY() + 0.01, upgradeShopPortalEntity.getZ());
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
            if (getAnimationProgress() >= maxAnimationProgress) {
                tickCramming();
            } else {
                setAnimationProgress(getAnimationProgress() + 1);
                getWorld().playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.BLOCKS, 1f, 1f + (float) getWorld().getRandom().nextDouble() * 0.3f, false);
            }
        } else {
            setAnimationProgress(getAnimationProgress() - 2);
            getWorld().playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_CHAIN_PLACE, SoundCategory.BLOCKS, 1f, 1f + (float) getWorld().getRandom().nextDouble() * 0.3f, false);
            if (!getWorld().isClient) {
                if (getAnimationProgress() <= 0) {
                    this.discard();
                }
            }
        }

        if (getWorld().isClient) {
            for (int i = 0; i < 1; i++) {
                getWorld().addParticle(ParticleRegistry.UPGRADE_PORTAL, getX() + getWorld().getRandom().nextGaussian() * 0.25, getBodyY(0.7 + getWorld().getRandom().nextGaussian() * 0.25), getZ() + getWorld().getRandom().nextGaussian() * 0.25,
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
        } else {
            getWorld().playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_HIT, getSoundCategory(), 0.8f, 1f, true);

            getWorld().addParticle(ParticleRegistry.XP_FLASH, getX(), getBodyY(0.5), getZ(), 0.125f, 0, 0);
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
        getDataTracker().set(DEAD, dead);
    }
}
