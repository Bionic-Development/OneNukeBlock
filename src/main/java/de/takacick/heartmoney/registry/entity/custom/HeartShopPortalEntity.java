package de.takacick.heartmoney.registry.entity.custom;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.access.EntityProperties;
import de.takacick.heartmoney.commands.HeartShopCommand;
import de.takacick.heartmoney.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class HeartShopPortalEntity extends Entity {
    private static final TrackedData<Boolean> DEAD = BionicDataTracker.registerData(new Identifier(HeartMoney.MOD_ID, "shop_dead"), TrackedDataHandlerRegistry.BOOLEAN);
    private int scaleTicks = 0;
    private int prevScaleTicks = 0;

    public HeartShopPortalEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(DEAD, false);
    }

    public void pushAway(Entity entity) {
        if (entity.getType().equals(this.getType())) {
            return;
        }

        if (!(entity instanceof EntityProperties entityProperties)) {
            return;
        }

        if (entityProperties.isOnHeartShopPortalCooldown()) {
            entityProperties.setHeartShopPortalCooldown(10);
            return;
        }

        if (equals(HeartShopCommand.HEART_SHOP_PORTAL)) {
            entityProperties.teleportBackFromHeartShop();
        } else if (HeartShopCommand.HEART_SHOP_PORTAL != null && HeartShopCommand.HEART_SHOP_PORTAL.isAlive()) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                serverPlayerEntity.teleport((ServerWorld) HeartShopCommand.HEART_SHOP_PORTAL.getWorld(), HeartShopCommand.HEART_SHOP_PORTAL.getX(), HeartShopCommand.HEART_SHOP_PORTAL.getY() + 0.01, HeartShopCommand.HEART_SHOP_PORTAL.getZ(), HeartShopCommand.HEART_SHOP_PORTAL.getYaw(), 0);
            } else {
                if (!world.equals(HeartShopCommand.HEART_SHOP_PORTAL.getWorld())) {
                    entity.moveToWorld((ServerWorld) HeartShopCommand.HEART_SHOP_PORTAL.getWorld());
                }
                entity.requestTeleport(HeartShopCommand.HEART_SHOP_PORTAL.getX(), HeartShopCommand.HEART_SHOP_PORTAL.getY() + 0.01, HeartShopCommand.HEART_SHOP_PORTAL.getZ());
            }
            entityProperties.setHeartShopPortal(getWorld(), getX(), getY() + 0.01, getZ(), false);
            entityProperties.setHeartShopPortalCooldown(10);
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
            this.prevScaleTicks = scaleTicks;
            this.scaleTicks = Math.min(scaleTicks + 2, 20);

            if (this.scaleTicks >= 20) {
                tickCramming();
            }
        } else {
            this.prevScaleTicks = scaleTicks;
            this.scaleTicks -= 2;

            if (!world.isClient) {
                if (this.scaleTicks <= 0) {
                    BionicUtils.sendEntityStatus((ServerWorld) this.world, this, HeartMoney.IDENTIFIER, 4);
                    this.discard();
                }
            }
        }

        if (world.isClient) {
            for (int i = 0; i < 1; i++) {
                world.addParticle(ParticleRegistry.HEART_PORTAL, getX() + world.getRandom().nextGaussian() * 0.25, getBodyY(1 + world.getRandom().nextGaussian() * 0.25), getZ() + world.getRandom().nextGaussian() * 0.25,
                        (float) world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8);
            }
        } else {
            ((ServerWorld) getWorld()).getChunkManager().setChunkForced(getChunkPos(), true);
        }

        super.tick();
    }

    protected void tickCramming() {
        if (!world.isClient) {
            List<Entity> list = world.getOtherEntities(this, calculateBoundingBox());
            list.forEach(this::pushAway);
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
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

        if (!world.isClient) {
            ((ServerWorld) getWorld()).getChunkManager().setChunkForced(getChunkPos(), false);
        }

        super.onRemoved();
    }

    public float getScaleProgress(float delta) {
        return MathHelper.lerp(delta, prevScaleTicks, scaleTicks) / 20f;
    }

    public void setDead(boolean dead) {
        getDataTracker().set(DEAD, dead);
    }
}
