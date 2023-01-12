package de.takacick.immortalmobs.registry.entity.projectiles;

import de.takacick.immortalmobs.network.ImmortalFireworkExplosionHandler;
import de.takacick.immortalmobs.registry.EntityRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class ImmortalFireworkExplosionEntity
        extends ProjectileEntity
        implements FlyingItemEntity {
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(ImmortalFireworkExplosionEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    public int life;
    public int lifeTime;
    @Nullable
    private LivingEntity shooter;

    public static NbtCompound getFireworkNBT(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateSubNbt("Fireworks");

        nbtCompound.putByte("Flight", (byte) 1);
        NbtCompound explosion = new NbtCompound();
        explosion.putBoolean("Flicker", true);
        explosion.putBoolean("Trail", true);
        explosion.putByte("Type", (byte) 1);
        NbtList nbtList = new NbtList();
        nbtList.add(explosion);

        nbtCompound.put("Explosions", nbtList);
        return nbtCompound;
    }

    public ImmortalFireworkExplosionEntity(EntityType<? extends ImmortalFireworkExplosionEntity> entityType, World world) {
        super(entityType, world);
    }

    public ImmortalFireworkExplosionEntity(World world, double x, double y, double z, ItemStack stack) {
        super(EntityRegistry.IMMORTAL_FIREWORK_EXPLOSION, world);
        this.life = 0;
        this.setPosition(x, y, z);
        int i = 1;

        getFireworkNBT(stack);

        if (!stack.isEmpty() && stack.hasNbt()) {
            this.dataTracker.set(ITEM, stack.copy());
            i += stack.getOrCreateSubNbt("Fireworks").getByte("Flight");
        }
        this.setVelocity(this.random.nextTriangular(0.0, 0.002297), 0.05, this.random.nextTriangular(0.0, 0.002297));
        this.lifeTime = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);
    }

    public ImmortalFireworkExplosionEntity(World world, @Nullable Entity entity, double x, double y, double z, ItemStack stack) {
        this(world, x, y, z, stack);
        this.setOwner(entity);
    }

    public static ImmortalFireworkExplosionEntity create(EntityType<ImmortalFireworkExplosionEntity> entityType, World world) {
        return new ImmortalFireworkExplosionEntity(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ITEM, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3d vec3d3;

        double f = this.horizontalCollision ? 1.0 : 1.15;
        this.setVelocity(this.getVelocity().multiply(f, 1.0, f).add(0.0, 0.04, 0.0));

        vec3d3 = this.getVelocity();
        this.move(MovementType.SELF, vec3d3);
        this.setVelocity(vec3d3);

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (!this.noClip) {
            this.onCollision(hitResult);
            this.velocityDirty = true;
        }
        this.updateRotation();
        if (this.life == 0 && !this.isSilent()) {
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.AMBIENT, 3.0f, 1.0f);
        }
        ++this.life;
        if (this.world.isClient && this.life % 2 < 2) {
            this.world.addParticle(ParticleRegistry.IMMORTAL_FIREWORK, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
        }
        if (!this.world.isClient && this.life > this.lifeTime) {
            this.explodeAndRemove();
        }
    }

    private void explodeAndRemove() {
        this.world.sendEntityStatus(this, EntityStatuses.EXPLODE_FIREWORK_CLIENT);
        this.emitGameEvent(GameEvent.EXPLODE, this.getOwner());
        this.explode();
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.world.isClient) {
            return;
        }
        this.explodeAndRemove();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos blockPos = new BlockPos(blockHitResult.getBlockPos());
        this.world.getBlockState(blockPos).onEntityCollision(this.world, blockPos, this);
        if (!this.world.isClient() && this.hasExplosionEffects()) {
            this.explodeAndRemove();
        }
        super.onBlockHit(blockHitResult);
    }

    private boolean hasExplosionEffects() {
        ItemStack itemStack = this.dataTracker.get(ITEM);
        NbtCompound nbtCompound = itemStack.isEmpty() ? null : itemStack.getSubNbt("Fireworks");
        NbtList nbtList = nbtCompound != null ? nbtCompound.getList("Explosions", NbtElement.COMPOUND_TYPE) : null;
        return nbtList != null && !nbtList.isEmpty();
    }

    private void explode() {
        ImmortalFireworkExplosionHandler.createExplosion((ServerWorld) world, getOwner(), null, null,
                getX(), getBodyY(0.5), getZ(), 2f, false,
                Explosion.DestructionType.DESTROY);
    }

    @Override
    public void handleStatus(byte status) {

        super.handleStatus(status);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Life", this.life);
        nbt.putInt("LifeTime", this.lifeTime);
        ItemStack itemStack = this.dataTracker.get(ITEM);
        if (!itemStack.isEmpty()) {
            nbt.put("FireworksItem", itemStack.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.life = nbt.getInt("Life");
        this.lifeTime = nbt.getInt("LifeTime");
        ItemStack itemStack = ItemStack.fromNbt(nbt.getCompound("FireworksItem"));
        if (!itemStack.isEmpty()) {
            this.dataTracker.set(ITEM, itemStack);
        }
    }

    @Override
    public ItemStack getStack() {
        ItemStack itemStack = this.dataTracker.get(ITEM);
        return itemStack.isEmpty() ? new ItemStack(Items.FIREWORK_ROCKET) : itemStack;
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}

