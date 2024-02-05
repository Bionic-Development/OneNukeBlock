package de.takacick.secretcraftbase.registry.entity.living;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SecretGiantJumpySlimeEntity extends SlimeEntity {

    public SecretGiantJumpySlimeEntity(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
        this.setSize(6, true);
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.setSize(6, true);
        return entityData;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        this.setRemoved(reason);
        this.brain.forgetAll();
    }
}
