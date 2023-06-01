package de.takacick.deathmoney.registry.entity.custom;

import de.takacick.deathmoney.DeathMoney;
import de.takacick.deathmoney.access.EntityProperties;
import de.takacick.deathmoney.commands.DeathShopCommand;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.registry.particles.ColoredParticleEffect;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

import java.util.List;

public class DeathShopPortalEntity extends Entity {
    private static final TrackedData<Boolean> DEAD = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "shop_dead"), TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SHOP = BionicDataTracker.registerData(new Identifier(DeathMoney.MOD_ID, "shop"), TrackedDataHandlerRegistry.BOOLEAN);
    private int progress = 0;
    private int prevProgress = 0;

    public DeathShopPortalEntity(EntityType<?> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(DEAD, false);
        getDataTracker().startTracking(SHOP, false);
    }

    public void pushAway(Entity entity) {
        if (entity.getType().equals(this.getType())) {
            return;
        }

        if (!(entity instanceof EntityProperties entityProperties)) {
            return;
        }

        if (entityProperties.isOnDeathShopPortalCooldown()) {
            entityProperties.setDeathShopPortalCooldown(10);
            return;
        }

        if (equals(DeathShopCommand.DEATH_SHOP_PORTAL)) {
            world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
            entityProperties.teleportBackFromDeathShop();
            entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
        } else if (DeathShopCommand.DEATH_SHOP_PORTAL != null && DeathShopCommand.DEATH_SHOP_PORTAL.isAlive()) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                serverPlayerEntity.teleport((ServerWorld) DeathShopCommand.DEATH_SHOP_PORTAL.getWorld(), DeathShopCommand.DEATH_SHOP_PORTAL.getX(), DeathShopCommand.DEATH_SHOP_PORTAL.getY() + 0.01, DeathShopCommand.DEATH_SHOP_PORTAL.getZ(), DeathShopCommand.DEATH_SHOP_PORTAL.getYaw(), 0);
                serverPlayerEntity.getWorld().playSound(null, serverPlayerEntity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
            } else {
                world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
                if (!world.equals(DeathShopCommand.DEATH_SHOP_PORTAL.getWorld())) {
                    entity.moveToWorld((ServerWorld) DeathShopCommand.DEATH_SHOP_PORTAL.getWorld());
                }
                entity.requestTeleport(DeathShopCommand.DEATH_SHOP_PORTAL.getX(), DeathShopCommand.DEATH_SHOP_PORTAL.getY() + 0.01, DeathShopCommand.DEATH_SHOP_PORTAL.getZ());
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 1f);
            }
            entityProperties.setDeathShopPortal(getWorld(), getX(), getY() + 0.01, getZ(), false);
            entityProperties.setDeathShopPortalCooldown(10);
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
            if (world.isClient && getProgress(0) < 1f) {
                for (int i = 0; i < 30; i++) {
                    double d = world.getRandom().nextGaussian() * 0.02;
                    double e = world.getRandom().nextGaussian() * 0.02;
                    double f = world.getRandom().nextGaussian() * 0.02;
                    world.addImportantParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.COAL_BLOCK.getDefaultState()), getX() + d * 8, getY() + e * 8, getZ() + f * 8, d, e, f);
                }

                Vec3f color = new Vec3f(Vec3d.unpackRgb(0x4DFF17));
                for (int i = 0; i < 8; i++) {
                    double d = world.getRandom().nextGaussian() * 0.08;
                    double e = world.getRandom().nextGaussian() * 0.08;
                    double f = world.getRandom().nextGaussian() * 0.08;
                    world.addImportantParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, color), getX() + d * 6, getY() + e * 6, getZ() + f * 6, d, e, f);
                }

                for (int i = 0; i < 2; i++) {
                    double d = world.getRandom().nextGaussian() * 0.08;
                    double e = world.getRandom().nextGaussian() * 0.08;
                    double f = world.getRandom().nextGaussian() * 0.08;
                    world.addImportantParticle(ParticleRegistry.DEATH_SOUL, getX() + d * 6, getY() + e * 6, getZ() + f * 6, d, e, f);
                }
                if (world.getRandom().nextDouble() <= 0.2) {
                    world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_WITHER_SKELETON_STEP, SoundCategory.BLOCKS, 0.3f, 1f + (float) world.getRandom().nextDouble() * 0.3f, false);
                }
            }

            this.prevProgress = progress;
            this.progress = Math.min(progress + 1, 40);

            if (this.progress >= 20) {
                tickCramming();
            }
        } else {
            this.prevProgress = progress;
            this.progress -= 2;

            if (world.isClient && getProgress(0) > 0f) {
                for (int i = 0; i < 30; i++) {
                    double d = world.getRandom().nextGaussian() * 0.02;
                    double e = world.getRandom().nextGaussian() * 0.02;
                    double f = world.getRandom().nextGaussian() * 0.02;
                    world.addImportantParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.COAL_BLOCK.getDefaultState()), getX() + d * 8, getY() + e * 8, getZ() + f * 8, d, e, f);
                }
                Vec3f color = new Vec3f(Vec3d.unpackRgb(0x4DFF17));
                for (int i = 0; i < 8; i++) {
                    double d = world.getRandom().nextGaussian() * 0.08;
                    double e = world.getRandom().nextGaussian() * 0.08;
                    double f = world.getRandom().nextGaussian() * 0.08;
                    world.addImportantParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_GLOW_SPARK, color), getX() + d * 6, getY() + e * 6, getZ() + f * 6, d, e, f);
                }

                for (int i = 0; i < 2; i++) {
                    double d = world.getRandom().nextGaussian() * 0.08;
                    double e = world.getRandom().nextGaussian() * 0.08;
                    double f = world.getRandom().nextGaussian() * 0.08;
                    world.addImportantParticle(ParticleRegistry.DEATH_SOUL, getX() + d * 6, getY() + e * 6, getZ() + f * 6, d, e, f);
                }
                if (world.getRandom().nextDouble() <= 0.2) {
                    world.playSound(getX(), getY(), getZ(), SoundEvents.ENTITY_WITHER_SKELETON_STEP, SoundCategory.BLOCKS, 0.3f, 1f + (float) world.getRandom().nextDouble() * 0.3f, false);
                }
            }

            if (!world.isClient) {
                if (this.progress <= 0) {
                    this.discard();
                }
            }
        }

        if (world.isClient) {
            for (int i = 0; i < 1; i++) {
                world.addParticle(ParticleRegistry.DEATH_PORTAL, getX() + world.getRandom().nextGaussian() * 0.25, getBodyY(1 + world.getRandom().nextGaussian() * 0.25), getZ() + world.getRandom().nextGaussian() * 0.25,
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

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance / 2);
    }

    public void setShop(boolean shop) {
        getDataTracker().set(SHOP, shop);
    }

    public boolean isShop() {
        return getDataTracker().get(SHOP);
    }

    public float getProgress(float delta) {
        return isShop() ? 1f : MathHelper.lerp(delta, prevProgress, progress) / 40f;
    }

    public void setDead(boolean dead) {
        if (getDataTracker().get(DEAD) != dead) {
            BionicUtils.sendEntityStatus((ServerWorld) world, this, DeathMoney.IDENTIFIER, 10);
        }

        getDataTracker().set(DEAD, dead);
    }
}
