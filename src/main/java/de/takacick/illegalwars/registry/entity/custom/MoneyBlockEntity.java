package de.takacick.illegalwars.registry.entity.custom;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.registry.EntityRegistry;
import de.takacick.illegalwars.registry.ParticleRegistry;
import de.takacick.illegalwars.registry.particles.ColoredParticleEffect;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

public class MoneyBlockEntity extends Entity {

    protected static final TrackedData<BlockPos> BLOCK_POS = DataTracker.registerData(MoneyBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(MoneyBlockEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> ROTATION_SPEED = DataTracker.registerData(MoneyBlockEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final int DEFAULT_FUSE = 61;
    private float rotation;
    private float prevRotation;

    public MoneyBlockEntity(EntityType<MoneyBlockEntity> type, World world) {
        super(type, world);
        this.setFuse(DEFAULT_FUSE);
        this.setNoGravity(true);
        this.intersectionChecked = true;
        getDataTracker().set(BLOCK_POS, getBlockPos());
    }

    public MoneyBlockEntity(World world, double x, double y, double z) {
        this(EntityRegistry.MONEY_BLOCK, world);
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
        if (this.age == 1 && !this.isSilent()) {
            this.getWorld().playSoundFromEntity(null, this, ParticleRegistry.MONEY_REWARD, SoundCategory.BLOCKS, 5.0f, 1.0f);
        }

        if (!getWorld().isClient) {
            this.setRotationSpeed(Math.min(getRotationSpeed() + 2f, 30));
        }

        this.addVelocity(0, 0.01, 0);

        this.prevRotation = this.rotation;
        this.rotation += getRotationSpeed();

        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.4, 0.0));
        }

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
            if (this.getWorld().isClient) {
                List<Vector3f> colors = Arrays.asList(0x80E38E, 0x64C972, 0x45D65A).stream().map(hex -> Vec3d.unpackRgb(hex).toVector3f()).toList();

                this.getWorld().addParticle(new ColoredParticleEffect(ParticleRegistry.COLORED_FIREWORK,
                        colors.get(getWorld().getRandom().nextInt(colors.size())), getId()), this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
            }
        }

        if (getWorld().isClient && getRotationSpeed() > 10) {

            Random random = getWorld().getRandom();

            for (int j = 1; j < 5; j++) {
                double x = random.nextGaussian();
                double y = random.nextGaussian();
                double z = random.nextGaussian();

                getWorld().addParticle(random.nextDouble() <= 0.6 ? ParticleRegistry.DOLLAR : ParticleRegistry.COIN,
                        getX() + x * 0.25, getY() + 0.5 + y * 0.25, getZ() + z * 0.25,
                        x * 0.45, y * 0.45, z * 0.45);
            }
        }
    }

    private void explode() {
        BionicUtils.sendEntityStatus(getWorld(), this, IllegalWars.IDENTIFIER, 6);
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
