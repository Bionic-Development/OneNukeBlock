package de.takacick.deathmoney.registry.entity.custom;

import de.takacick.deathmoney.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class BlockBreakEntity extends Entity {

    private int damage = 5;
    private int lastDamage = 0;

    public BlockBreakEntity(EntityType<? extends BlockBreakEntity> entityType, World world) {
        super(entityType, world);
    }

    public BlockBreakEntity(World world, double x, double y, double z) {
        this(EntityRegistry.BLOCK_BREAK, world);
        this.setPosition(x, y, z);
    }

    public static BlockBreakEntity create(EntityType<BlockBreakEntity> entityType, World world) {
        return new BlockBreakEntity(entityType, world);
    }

    @Override
    public boolean isGlowing() {
        return true;
    }

    @Override
    protected void initDataTracker() {

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

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void tick() {
        if (getBlockStateAtPos().isAir()) {
            this.discard();
        }
        if (!world.isClient) {

            if (lastDamage >= 20) {
                this.discard();
            } else {
                lastDamage++;
            }
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    protected int getMaxProgress() {
        return 100;
    }

    public void setLastDamage(int lastDamage) {
        this.lastDamage = lastDamage;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}

