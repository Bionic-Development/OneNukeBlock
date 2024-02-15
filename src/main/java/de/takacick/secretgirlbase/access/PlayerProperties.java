package de.takacick.secretgirlbase.access;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.UUID;

public interface PlayerProperties {

    void setPhonePlayer(PlayerEntity playerEntity);

    Optional<UUID> getPhonePlayer();

    void setSmokeDrop(ItemStack itemStack);

    Optional<ItemStack> getSmokeDrop();

}
