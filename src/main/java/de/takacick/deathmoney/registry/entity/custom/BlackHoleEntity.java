package de.takacick.deathmoney.registry.entity.custom;

import de.takacick.deathmoney.registry.EntityRegistry;
import de.takacick.deathmoney.registry.ParticleRegistry;
import de.takacick.deathmoney.registry.entity.projectiles.CustomBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class BlackHoleEntity extends HostileEntity {

    private Entity player;

    private static final TrackedData<Float> SIZE = DataTracker.registerData(BlackHoleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Boolean> UNSTABLE = DataTracker.registerData(BlackHoleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private boolean preview = false;
    public int unstableTicks = 0;

    public BlackHoleEntity(EntityType<? extends BlackHoleEntity> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.noClip = true;
        this.setNoGravity(true);
    }

    @Override
    protected void initDataTracker() {

        getDataTracker().startTracking(SIZE, 0.5f);
        getDataTracker().startTracking(UNSTABLE, false);

        super.initDataTracker();
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Entity getPlayer() {
        return player;
    }

    @Override
    protected void initGoals() {

    }

    @Override
    public void tick() {
        super.tick();

        if (age % 5 == 0) {
            world.playSound(getX(), getY(), getZ(), SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.AMBIENT, 3, 1, false);
        }

        if (isUnstable()) {
            unstableTicks++;
        }

        if (!world.isClient) {

            if (getSize(1f) >= 6f) {

                setUnstable(true);
                if (unstableTicks > 5) {
                    for (int d = 0; d < 40; d++) {
                        double x = getX();
                        double z = getZ();

                        double increment = (Math.PI) / 20;
                        double angle = (d) * increment;
                        x = x + (2 * Math.cos(angle));
                        z = z + (2 * Math.sin(angle));

                        BlackMatterShockwaveEntity blackMatterShockwaveEntity = new BlackMatterShockwaveEntity(world, new Vec3d(x, getY(), z).subtract(getPos()).normalize(), x, getY() + 0.5, z);
                        blackMatterShockwaveEntity.setPos(x, getBodyY(0.5), z);
                        world.spawnEntity(blackMatterShockwaveEntity);
                    }
                    world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT, 3, 1.5f);
                    world.playSound(null, getX(), getY(), getZ(), ParticleRegistry.DEATH_SHOP_EMERGE, SoundCategory.AMBIENT, 3, 1.5f);
                    this.discard();
                } else {
                    Vec3d pos = new Vec3d(getX(), getY() + getSize(1f) / 2f, getZ());
                    world.getOtherEntities(getPlayer(), new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ())
                            .expand(30)).forEach(entity -> {
                        if (entity instanceof CustomBlockEntity) {
                            entity.discard();
                        }
                    });
                }
                return;
            }

            Vec3d pos = new Vec3d(getX(), getY() + getSize(1f) / 2f, getZ());

            world.getOtherEntities(getPlayer(), new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ())
                    .expand(30)).forEach(entity -> {
                if (entity instanceof CustomBlockEntity) {
                    double distance = entity.getPos().distanceTo(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
                    if (distance <= 1.7 * getSize(1f)) {
                        setSize(getSize(1) + 0.004f);
                        entity.discard();
                    }

                    Vec3d pull = pos.subtract(entity.getPos()).normalize();
                    pull = pull.multiply(Math.pow(1f, (1.5F) / (1 * 2.5F))).multiply(0.4f);

                    entity.addVelocity(MathHelper.clamp(pull.getX(), -1F, +1F), MathHelper.clamp(pull.getY(), -1F, +1F), MathHelper.clamp(pull.getZ(), -1F, +1F));
                    entity.velocityModified = true;
                    entity.velocityDirty = true;
                }
            });

            destroy(world, getBlockX(), getBlockY(), getBlockZ(), (int) (25 * getSize(1)));
            destroy(world, getBlockX(), getBlockY(), getBlockZ(), (int) (10 * getSize(1)));
        } else {
            for (int i = 0; i < 1; i++) {
                world.addParticle(ParticleRegistry.BLACK_MATTER_PORTAL, getX() + world.getRandom().nextGaussian() * 0.25, getBodyY(1 + world.getRandom().nextGaussian() * 0.25), getZ() + world.getRandom().nextGaussian() * 0.25,
                        (float) world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8, world.getRandom().nextGaussian() * 8);
            }
        }

        calculateBoundingBox();
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entity) {

    }

    public boolean shouldRender(double distance) {
        double d = 256.0D * getRenderDistanceMultiplier();
        return distance < d * d;
    }

    @Override
    protected Box calculateBoundingBox() {
        float size = Math.max(getSize(1f) / 2f - 1f, 0.0f);
        return super.calculateBoundingBox().expand(size, size, size);
    }

    @Override
    public boolean isPartOfGame() {
        return false;
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_MAX_HEALTH, 500).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.11).add(EntityAttributes.GENERIC_ARMOR).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D).add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    public static void destroy(World world, int x, int y, int z, int radius) {
        int pickupRadius = (int) (radius * 2.5F);

        int chunkRadius = Math.max(1, radius / 16) + 6;

        for (int chunkX = (x >> 4) - chunkRadius; chunkX < (x >> 4) + chunkRadius; ++chunkX) {
            for (int chunkZ = (z >> 4) - chunkRadius; chunkZ < (z >> 4) + chunkRadius; ++chunkZ) {
                WorldChunk chunk = world.getChunk(chunkX, chunkZ);

                int rX = chunk.getWorld().random.nextInt(16);
                int rZ = chunk.getWorld().random.nextInt(16);

                int rCX = (chunkX * 16 + rX - x) * (chunkX * 16 + rX - x);
                int rCZ = (chunkZ * 16 + rZ - z) * (chunkZ * 16 + rZ - z);

                if (rCX + rCZ < pickupRadius * pickupRadius) {
                    BlockPos topPosition = chunk.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE, new BlockPos(chunkX * 16 + rX, 0, chunkZ * 16 + rZ)).add(0, -1, 0);

                    BlockState state = chunk.getBlockState(topPosition);

                    if (!state.isAir() && !(state.getBlock() instanceof FluidBlock)) {
                        if (world.random.nextInt(8) == 0) {
                            world.setBlockState(topPosition, Blocks.AIR.getDefaultState());

                            CustomBlockEntity customBlockEntity = new CustomBlockEntity(EntityRegistry.FALLING_BLOCK, world);
                            customBlockEntity.setItemStack(state, state.getBlock().asItem().getDefaultStack());
                            customBlockEntity.noClip = true;
                            customBlockEntity.setPos(topPosition.getX(), topPosition.getY(), topPosition.getZ());
                            world.spawnEntity(customBlockEntity);
                        }
                    }
                }
            }
        }
    }

    public void setSize(float size) {
        getDataTracker().set(SIZE, size);
    }

    public float getSize(float tickDelta) {
        return getDataTracker().get(SIZE);
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    @Override
    public boolean isUndead() {
        return false;
    }

    public void setUnstable(boolean unstable) {
        if (unstable) {
            world.playSound(null, getX(), getY(), getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.AMBIENT, 3, 1.5f);
        }
        getDataTracker().set(UNSTABLE, unstable);
    }

    public boolean isUnstable() {
        return getDataTracker().get(UNSTABLE);
    }
}