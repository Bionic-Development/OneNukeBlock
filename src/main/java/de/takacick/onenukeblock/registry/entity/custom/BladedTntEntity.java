package de.takacick.onenukeblock.registry.entity.custom;

import de.takacick.onenukeblock.registry.EntityRegistry;
import de.takacick.onenukeblock.registry.entity.projectiles.DiamondSwordEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BladedTntEntity
        extends Entity
        implements Ownable {
    private static final TrackedData<Integer> FUSE = DataTracker.registerData(BladedTntEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int DEFAULT_FUSE = 80;
    public static final String FUSE_NBT_KEY = "fuse";
    private static final ExplosionBehavior TELEPORTED_EXPLOSION_BEHAVIOR = new ExplosionBehavior() {

        @Override
        public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
            if (state.isOf(Blocks.NETHER_PORTAL)) {
                return false;
            }
            return super.canDestroyBlock(explosion, world, pos, state, power);
        }

        @Override
        public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
            if (blockState.isOf(Blocks.NETHER_PORTAL)) {
                return Optional.empty();
            }
            return super.getBlastResistance(explosion, world, pos, blockState, fluidState);
        }
    };
    @Nullable
    private LivingEntity causingEntity;
    private boolean teleported;

    public BladedTntEntity(EntityType<? extends BladedTntEntity> entityType, World world) {
        super(entityType, world);
        this.intersectionChecked = true;
    }

    public BladedTntEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityRegistry.BLADED_TNT, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.setFuse(80);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(FUSE, 80);
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
    protected double getGravity() {
        return 0.04;
    }

    @Override
    public void tick() {
        this.tickPortalTeleportation();
        this.applyGravity();
        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.98));
        if (this.isOnGround()) {
            this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
        }
        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.getWorld().isClient) {
                this.explode();
            }
        } else {
            this.updateWaterState();
            if (this.getWorld().isClient) {
                this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    private void explode() {
        float f = 4.0f;
        this.getWorld().createExplosion(this, Explosion.createDamageSource(this.getWorld(), this), this.teleported ? TELEPORTED_EXPLOSION_BEHAVIOR : null, this.getX(), this.getBodyY(0.0625), this.getZ(), 1.0f, false, World.ExplosionSourceType.TNT, ParticleTypes.EXPLOSION_EMITTER, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);

        World world = getWorld();

        for (int i = 0; i < world.getRandom().nextBetween(40, 50); i++) {
            Vec3d vec3d = new Vec3d(getRandom().nextGaussian(), getRandom().nextDouble(), getRandom().nextGaussian());
            DiamondSwordEntity diamondSwordEntity = new DiamondSwordEntity(world, getX(), getBodyY(0.5), getZ());
            diamondSwordEntity.setVelocity(vec3d.getX(), vec3d.getY(), vec3d.getZ(), 2f, 6.6666665f);
            world.spawnEntity(diamondSwordEntity);

            world.playSound(null, getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putShort(FUSE_NBT_KEY, (short) this.getFuse());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.setFuse(nbt.getShort(FUSE_NBT_KEY));
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        return this.causingEntity;
    }

    @Override
    public void copyFrom(Entity original) {
        super.copyFrom(original);
        if (original instanceof BladedTntEntity tntEntity) {
            this.causingEntity = tntEntity.causingEntity;
        }
    }

    public void setFuse(int fuse) {
        this.dataTracker.set(FUSE, fuse);
    }

    public int getFuse() {
        return this.dataTracker.get(FUSE);
    }

    private void setTeleported(boolean teleported) {
        this.teleported = teleported;
    }

    @Override
    @Nullable
    public Entity teleportTo(TeleportTarget teleportTarget) {
        Entity entity = super.teleportTo(teleportTarget);
        if (entity instanceof BladedTntEntity tntEntity) {
            tntEntity.setTeleported(true);
        }
        return entity;
    }
}

