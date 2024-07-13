package de.takacick.onegirlboyblock.registry.entity.projectiles;

import de.takacick.onegirlboyblock.OneGirlBoyBlock;
import de.takacick.onegirlboyblock.registry.EntityRegistry;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.onegirlboyblock.registry.particles.TetrisParticleEffect;
import de.takacick.onegirlboyblock.utils.data.AttachmentTypes;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Util;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.IntFunction;

public class TetrisEntity extends PersistentProjectileEntity {
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(TetrisEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public TetrisEntity(EntityType<? extends TetrisEntity> entityType, World world) {
        super(entityType, world);
    }

    public TetrisEntity(World world, double x, double y, double z, @Nullable LivingEntity owner) {
        this(EntityRegistry.TETRIS, world);
        this.setPosition(x, y, z);
        double d = world.random.nextDouble() * 6.2831854820251465;
        this.setOwner(owner);
        this.setVelocity(-Math.sin(d) * 0.02, 0.2f, -Math.cos(d) * 0.02);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

        builder.add(VARIANT, 0);

        super.initDataTracker(builder);
    }

    @Override
    public void setVelocity(Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        float f = -MathHelper.sin(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
        float g = -MathHelper.sin((pitch + roll) * ((float) Math.PI / 180));
        float h = MathHelper.cos(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
        this.setVelocity(f, g, h, speed, divergence);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("variant", this.getVariant().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(Variant.byId(nbt.getInt("variant")));
    }

    @Override
    public void tick() {
        Vec3d velocity = getVelocity();
        super.tick();

        setVelocity(velocity.multiply(1, 0.99, 1));
        if (!getWorld().isClient) {
            if (isOnGround() || isInsideWall() || this.age > 200) {
                this.explode();
            }
        } else {

            velocity = getVelocity().multiply(0.3);
            for (int i = 0; i < 1; i++) {
                Vec3d vec3d = new Vec3d(this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian());

                getWorld().addImportantParticle(new TetrisParticleEffect(ParticleRegistry.TETRIS, this.getVariant().getId()),
                        false,
                        getX() + vec3d.getX() * 0.05,
                        getBodyY(0.5),
                        getZ() + vec3d.getZ() * 0.05,
                        -velocity.getX() + vec3d.getX() * 0.25,
                        -velocity.getY() + vec3d.getY() * 0.25,
                        -velocity.getZ() + vec3d.getZ() * 0.25
                );
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!getWorld().isClient) {
            if (!hitResult.getType().equals(HitResult.Type.MISS)) {
                if (hitResult instanceof EntityHitResult entityHitResult) {
                    if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
                        livingEntity.getAttachedOrCreate(AttachmentTypes.TETRIS_DAMAGE_HELPER).addTetrisDamage(this.getVariant());
                    }
                }

                explode();
                this.discard();
                return;
            }
        }

        super.onCollision(hitResult);
    }

    private void explode() {
        EventHandler.sendEntityStatus(getWorld(), this, OneGirlBoyBlock.IDENTIFIER, 5, 0);
    }

    public Variant getVariant() {
        return Variant.byId(this.dataTracker.get(VARIANT));
    }

    public void setVariant(Variant variant) {
        this.dataTracker.set(VARIANT, variant.getId());
    }

    public byte getPierceLevel() {
        return (byte) 0;
    }

    @Override
    protected boolean canHit(Entity entity) {
        return !getType().equals(entity.getType()) && !entity.equals(getOwner()) && super.canHit(entity);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemRegistry.BIT_CANNON.getDefaultStack();
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.getWorld(), this, currentPosition, nextPosition, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), this::canHit);
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    @Override
    public boolean isImmuneToExplosion(Explosion explosion) {
        return true;
    }

    public enum Variant {
        YELLOW(0, 0xAEA001),
        PURPLE(1, 0xAB73FF),
        ORANGE(2, 0xF37F01),
        BLUE(3, 0x00C9FF),
        CYAN(4, 0x01D7B6),
        RED(5, 0xFF6666),
        GREEN(6, 0x17ED01);

        private static final IntFunction<Variant> BY_ID;
        private final int id;
        private final int color;

        private Variant(int id, int color) {
            this.id = id;
            this.color = color;
        }

        public String getName() {
            return this.name().toLowerCase();
        }

        public int getColor() {
            return this.color;
        }

        public int getId() {
            return this.id;
        }

        public static Variant byId(int id) {
            return BY_ID.apply(id);
        }

        private static Variant getRandom(Random random) {
            Variant[] variants = Arrays.stream(AxolotlEntity.Variant.values()).toArray(Variant[]::new);
            return Util.getRandom(variants, random);
        }

        static {
            BY_ID = ValueLists.createIdToValueFunction(Variant::getId, Variant.values(), ValueLists.OutOfBoundsHandling.ZERO);
        }
    }
}