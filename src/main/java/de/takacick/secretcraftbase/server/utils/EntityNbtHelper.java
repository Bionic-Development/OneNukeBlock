package de.takacick.secretcraftbase.server.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class EntityNbtHelper {

    public static ItemStack fromEntity(Item item, Entity entity) {
        ItemStack itemStack = item.getDefaultStack();

        NbtCompound nbtCompound = itemStack.getOrCreateSubNbt("entityData");
        entity.writeNbt(nbtCompound);
        nbtCompound.putShort("HurtTime", (short) 0);
        nbtCompound.putInt("HurtByTimestamp", 0);

        EntityType<?> entityType = entity.getType();
        nbtCompound = itemStack.getOrCreateSubNbt("entityType");
        nbtCompound.putString("type", EntityType.getId(entityType).toString());

        return itemStack;
    }

    public static ItemStack setNbt(ItemStack itemStack, NbtCompound compound) {
        NbtCompound nbtCompound = itemStack.getOrCreateSubNbt("entityData");
        nbtCompound.copyFrom(compound);
        nbtCompound.putShort("HurtTime", (short) 0);
        nbtCompound.putInt("HurtByTimestamp", 0);
        return itemStack;
    }

    public static EntityType<?> getEntityType(ItemStack itemStack) {
        NbtCompound type = itemStack.getSubNbt("entityType");
        EntityType<?> entityType = EntityType.PIG;

        if (type != null) {
            entityType = Registries.ENTITY_TYPE.get(new Identifier(type.getString("type")));
        }

        return entityType;
    }

    public static EntityType<?> getEntityType(EntityType<?> defaultType, ItemStack itemStack) {
        NbtCompound type = itemStack.getSubNbt("entityType");
        EntityType<?> entityType = defaultType;

        if (type != null) {
            entityType = Registries.ENTITY_TYPE.get(new Identifier(type.getString("type")));
        }

        return entityType;
    }

    public static NbtCompound getEntityNbt(ItemStack itemStack) {
        NbtCompound nbtCompound = itemStack.getSubNbt("entityData");
        if (nbtCompound == null) {
            nbtCompound = new NbtCompound();
        }

        return nbtCompound;
    }
}
