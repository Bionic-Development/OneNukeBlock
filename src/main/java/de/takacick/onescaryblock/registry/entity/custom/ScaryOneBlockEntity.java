package de.takacick.onescaryblock.registry.entity.custom;

import de.takacick.onescaryblock.registry.EntityRegistry;
import de.takacick.onescaryblock.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ScaryOneBlockEntity extends Entity {

    protected static final TrackedData<BlockPos> BLOCK_POS = DataTracker.registerData(ScaryOneBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(ScaryOneBlockEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> ROTATION_SPEED = DataTracker.registerData(ScaryOneBlockEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final int DEFAULT_FUSE = 61;
    private float rotation;
    private float prevRotation;

    public ScaryOneBlockEntity(EntityType<ScaryOneBlockEntity> type, World world) {
        super(type, world);
        this.setFuse(DEFAULT_FUSE);
        this.setNoGravity(true);
        this.intersectionChecked = true;
        getDataTracker().set(BLOCK_POS, getBlockPos());
    }

    public ScaryOneBlockEntity(World world, double x, double y, double z) {
        this(EntityRegistry.SCARY_ONE_BLOCK, world);
        this.setNoGravity(true);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(FUSE, DEFAULT_FUSE);
        getDataTracker().startTracking(ROTATION_SPEED, 0f);
        getDataTracker().startTracking(BLOCK_POS, BlockPos.ORIGIN);
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        if (!getWorld().isClient) {
            this.setRotationSpeed(Math.min(getRotationSpeed() + 0.2f, 30));
        } else {
            getWorld().playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.HOSTILE, 3f, 0.4f + getWorld().getRandom().nextFloat() * 0.2f, false);
            getWorld().playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.HOSTILE, 3f, 0.4f + getWorld().getRandom().nextFloat() * 0.2f, false);

            Random random = Random.create();
            Vec3d pos = getPos().add(0, getHeight() * 0.5, 0);
            for (int i = 0; i < 60; ++i) {
                Vec3d vel = new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().multiply(0.55);

                getWorld().addImportantParticle(ParticleRegistry.FALLING_BLOOD, true,
                        pos.getX() + vel.getX(), pos.getY() + vel.getY(), pos.getZ() + vel.getZ(),
                        vel.getX() * 0.6, vel.getY() * 1.6, vel.getZ() * 0.6);
            }
        }

        this.addVelocity(0, 0.01, 0);

        this.prevRotation = this.rotation;
        this.rotation += getRotationSpeed();

        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.85));

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            if (!this.getWorld().isClient) {
                this.explode();
            }
            this.discard();
        } else {
            this.updateWaterState();
        }
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    private void explode() {
        if (getWorld() instanceof ServerWorld world) {
            world.getPlayers().stream().toList().forEach(serverPlayerEntity -> {
                serverPlayerEntity.networkHandler.disconnect(Text.of("§c\uD835\uDE83\uD835\uDE77\uD835\uDE74 \uD835\uDE72\uD835\uDE84\uD835\uDE81\uD835\uDE82\uD835\uDE74 \uD835\uDE77\uD835\uDE70\uD835\uDE82 \uD835\uDE71\uD835\uDE74\uD835\uDE74\uD835\uDE7D \uD835\uDE7B\uD835\uDE78\uD835\uDE75\uD835\uDE83\uD835\uDE74\uD835\uDE73"));
            });
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Age", (short) this.age);
        nbt.putShort("Fuse", (short) this.getFuse());
        nbt.putFloat("RotationSpeed", this.getRotationSpeed());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.age = nbt.getInt("Age");
        if (nbt.contains("Fuse", NbtCompound.SHORT_TYPE)) {
            this.setFuse(nbt.getShort("Fuse"));
        }

        if (nbt.contains("RotationSpeed", NbtCompound.FLOAT_TYPE)) {
            this.setRotationSpeed(nbt.getFloat("RotationSpeed"));
        }
    }

    public float getFuseProgress(float tickDelta) {
        return MathHelper.getLerpProgress(this.getFuse() - tickDelta, DEFAULT_FUSE, 0);
    }

    public void setRotationSpeed(float speed) {
        getDataTracker().set(ROTATION_SPEED, speed);
    }

    public float getRotationSpeed() {
        return getDataTracker().get(ROTATION_SPEED);
    }

    public float getRotation(float tickDelta) {
        return MathHelper.lerp(tickDelta, this.prevRotation, this.rotation);
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    public BlockPos getFromBlockPos() {
        return getDataTracker().get(BLOCK_POS);
    }

    @Override
    public boolean isImmuneToExplosion(Explosion explosion) {
        return true;
    }
}
