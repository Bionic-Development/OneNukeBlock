package de.takacick.imagineanything.registry.entity.custom;

import de.takacick.imagineanything.registry.EntityRegistry;
import de.takacick.imagineanything.registry.ParticleRegistry;
import de.takacick.imagineanything.registry.particles.ColoredGlowParticleEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class HologramItemEntity extends Entity {

    private static final TrackedData<ItemStack> ITEM_STACK = DataTracker.registerData(HologramItemEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private static final TrackedData<Boolean> GLINT = DataTracker.registerData(HologramItemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public HologramItemEntity(EntityType<? extends HologramItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public HologramItemEntity(World world, double x, double y, double z, ItemStack itemStack) {
        this(EntityRegistry.HOLOGRAM_ITEM, world);
        this.setPosition(x, y, z);
        getDataTracker().set(ITEM_STACK, itemStack);
    }

    public static HologramItemEntity create(EntityType<HologramItemEntity> entityType, World world) {
        return new HologramItemEntity(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        return getDataTracker().get(GLINT);
    }

    @Override
    public int getTeamColorValue() {
        return 0x0000FF;
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    public boolean collides() {
        return !this.isRemoved();
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        NbtCompound nbtCompound = nbt.getCompound("customItemStack");
        getDataTracker().set(ITEM_STACK, ItemStack.fromNbt(nbtCompound));
        getDataTracker().set(GLINT, nbt.getBoolean("hasCustomGlint"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("hasCustomGlint", getDataTracker().get(GLINT));
        nbt.put("customItemStack", getDataTracker().get(ITEM_STACK).writeNbt(new NbtCompound()));
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ITEM_STACK, Items.BARRIER.getDefaultStack());
        this.dataTracker.startTracking(GLINT, false);
    }

    @Override
    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength();
        if (Double.isNaN(d)) {
            d = 1.0;
        }
        return distance < (d *= 64.0 * 5) * d;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }

    public ItemStack getItemStack() {
        return getDataTracker().get(ITEM_STACK);
    }

    @Override
    protected void pushOutOfBlocks(double x, double y, double z) {

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {

        world.playSound(getX(), getY() + calculateBoundingBox().getYLength() / 2, getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.AMBIENT, 0.5f, 1, true);
        if (!world.isClient) {
            this.discard();
            this.dropStack(getItemStack());
            Vec3f color = new Vec3f(Vec3d.unpackRgb(0x4fc3f7));
            for (int x = 0; x < 15; x++) {
                ((ServerWorld) getEntityWorld()).spawnParticles(new ColoredGlowParticleEffect(ParticleRegistry.GLOW_SPARK, color),
                        getX(), getY() + calculateBoundingBox().getYLength() / 2,
                        getZ(), 3, 0.0D, 0.0D, 0.0D,
                        0.35000000596046448D);
            }
        }

        return super.interact(player, hand);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}

