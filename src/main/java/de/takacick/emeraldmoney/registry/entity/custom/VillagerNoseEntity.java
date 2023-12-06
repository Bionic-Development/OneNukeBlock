package de.takacick.emeraldmoney.registry.entity.custom;

import de.takacick.emeraldmoney.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class VillagerNoseEntity extends Entity {
    protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(VillagerNoseEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public VillagerNoseEntity(EntityType<? extends VillagerNoseEntity> entityType, World world) {
        super(entityType, world);
    }

    public VillagerNoseEntity(World world, double x, double y, double z) {
        this(EntityRegistry.VILLAGER_NOSE, world);
        this.setPosition(x, y, z);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    public static VillagerNoseEntity create(EntityType<VillagerNoseEntity> entityType, World world) {
        return new VillagerNoseEntity(entityType, world);
    }

    @Override
    public boolean isAttackable() {
        return super.isAttackable();
    }

    @Override
    public boolean isGlowing() {
        return true;
    }

    @Override
    public int getTeamColorValue() {
        return 0x41F384;
    }

    @Override
    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public void tick() {
        if (!getWorld().isClient) {
            if (!getWorld().getBlockState(getBlockPos()).isIn(BlockTags.EMERALD_ORES)) {
                this.discard();
            }

            if (age > 600) {
                this.discard();
            }
        }
    }

    @Override
    public boolean shouldRender(double distance) {
        return getWorld().getBlockState(getBlockPos()).isIn(BlockTags.EMERALD_ORES);
    }

    @Override
    public boolean shouldSave() {
        return false;
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void setOwner(PlayerEntity player) {
        this.setOwnerUuid(player.getUuid());
    }
}

