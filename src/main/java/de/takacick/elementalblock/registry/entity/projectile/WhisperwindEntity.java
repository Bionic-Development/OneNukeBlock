package de.takacick.elementalblock.registry.entity.projectile;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.registry.EntityRegistry;
import de.takacick.elementalblock.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class WhisperwindEntity extends ProjectileEntity {

    public static final RegistryKey<DamageType> WHISPERWIND_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(OneElementalBlock.MOD_ID, "whisperwind"));

    private static final TrackedData<Integer> TARGET = DataTracker.registerData(WhisperwindEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Vector3f> VECTOR = DataTracker.registerData(WhisperwindEntity.class, TrackedDataHandlerRegistry.VECTOR3F);

    @Nullable
    private Entity target;

    public WhisperwindEntity(EntityType<? extends WhisperwindEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(TARGET, -1);
        this.dataTracker.startTracking(VECTOR, new Vector3f(0, 0, 0));
    }

    public WhisperwindEntity(World world, LivingEntity owner, Entity target) {
        this(EntityRegistry.WHISPERWIND, world);
        this.setOwner(owner);
        setTarget(target);
    }

    public static WhisperwindEntity create(EntityType<WhisperwindEntity> entityType, World world) {
        return new WhisperwindEntity(entityType, world);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.target != null) {
            nbt.putUuid("Target", this.target.getUuid());
        }
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void tick() {
        super.tick();

        if (!getWorld().isClient && !isRemoved()) {
            if (target != null && target.isAlive()) {
                Vec3d ownerPos = target.getPos().add(0, -target.getHeight() / 2, 0);

                if (target.getType().equals(EntityType.ENDER_DRAGON)) {
                    ownerPos = ownerPos.multiply(1, 0, 1).add(0, target.getBodyY(0.35), 0);
                } else {
                    ownerPos = ownerPos.multiply(1, 0, 1).add(0, target.getBodyY(0.65), 0);
                }

                Vec3d thisPos = getPos();
                Vec3d difference = ownerPos.subtract(thisPos).normalize().multiply(2.0);

                getDataTracker().set(VECTOR, approach(getVelocity(), difference, 0.2, 2.0).toVector3f());
            } else {
                if (age >= 100) {
                    this.discard();
                }

                getDataTracker().set(VECTOR, getVelocity().toVector3f());
            }
        } else {
            getWorld().addParticle(ParticleRegistry.CLOUD, getX(), getBodyY(0.5), getZ(), 0, 0, 0);
        }

        Vec3d vec3d = getVelocity();
        Vec3d vec3d3 = this.getPos();
        Vec3d vec3d4 = vec3d3.add(vec3d);
        HitResult hitResult = this.getWorld().raycast(new RaycastContext(vec3d3, vec3d4, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d4 = hitResult.getPos();
        }

        while (!this.isRemoved()) {
            EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d4);
            if (entityHitResult != null) {
                hitResult = entityHitResult;
            }

            if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) hitResult).getEntity();
                Entity entity2 = this.getOwner();
                if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity) entity2).shouldDamagePlayer((PlayerEntity) entity)) {
                    hitResult = null;
                    entityHitResult = null;
                }
            }

            if (hitResult != null) {
                this.onCollision(hitResult);
                this.velocityDirty = true;
            }

            if (entityHitResult == null) {
                break;
            }

            hitResult = null;
        }

        vec3d = new Vec3d(getDataTracker().get(VECTOR));

        if (vec3d != null) {
            setVelocity(vec3d.getX(), vec3d.getY(), vec3d.getZ());
            updateRotation();
        }

        this.checkBlockCollision();
        this.move(MovementType.SELF, this.getVelocity());
        this.setVelocity(this.getVelocity().multiply(0.99));
    }

    public Vec3d approach(Vec3d vec3d, Vec3d target, double stepSize, double maxDistance) {
        Vec3d delta = target.subtract(vec3d);
        double distance = delta.length();

        if (distance > maxDistance) {
            delta = delta.multiply(maxDistance / distance);
        }

        return vec3d.add(delta.multiply(stepSize));
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && !entity.noClip && !entity.equals(getOwner());
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 16384.0;
    }

    @Override
    public float getBrightnessAtEyes() {
        return 1.0f;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Entity entity2 = this.getOwner();
        LivingEntity livingEntity = entity2 instanceof LivingEntity ? (LivingEntity) entity2 : null;
        boolean bl = entity.damage(this.getDamageSources().create(WHISPERWIND_TYPE, this, livingEntity), 7.0f);

        if (entity instanceof LivingEntity target) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 0));
        }

        if (bl) {
            this.applyDamageEffects(livingEntity, entity);
        }

        if (!entityHitResult.getType().equals(HitResult.Type.MISS)) {
            this.discard();
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!getWorld().isClient) {
            if (!hitResult.getType().equals(HitResult.Type.MISS)) {
                this.discard();
            }
        }
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        double d = packet.getVelocityX();
        double e = packet.getVelocityY();
        double f = packet.getVelocityZ();
        this.setVelocity(d, e, f);
    }

    public void setTarget(Entity target) {
        this.target = target;
        this.dataTracker.set(TARGET, target == null ? -1 : target.getId());
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    public void onRemoved() {

        if (getWorld().isClient) {
            for (int i = 0; i < random.nextInt(10); i++) {
                getWorld().addParticle(ParticleTypes.CLOUD,
                        getX(), getBodyY(0.5 + getWorld().getRandom().nextGaussian() * 0.25), getZ(),
                        getWorld().getRandom().nextGaussian() * 0.25,
                        getWorld().getRandom().nextGaussian() * 0.25,
                        getWorld().getRandom().nextGaussian() * 0.25);
            }
        }

        super.onRemoved();
    }

    public Entity getTarget() {
        return getDataTracker().get(TARGET) >= 0 ? getWorld().getEntityById(getDataTracker().get(TARGET)) : null;
    }
}

