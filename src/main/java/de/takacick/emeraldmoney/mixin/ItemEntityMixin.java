package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.access.PlayerProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    private int pickupDelay;

    @Shadow
    private @Nullable UUID owner;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;getStack()Lnet/minecraft/item/ItemStack;", shift = At.Shift.BEFORE), cancellable = true)
    public void onPlayerCollision(PlayerEntity player, CallbackInfo info) {
        if (player instanceof PlayerProperties playerProperties && playerProperties.hasEmeraldWallet()) {
            int emeralds = getStack().isOf(Items.EMERALD) ? 1 : getStack().isOf(Items.EMERALD_BLOCK) ? 9 : 0;
            if (emeralds > 0) {
                if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUuid()))) {
                    emeralds *= getStack().getCount();

                    playerProperties.addEmeralds(emeralds, true);
                    player.sendPickup(this, getStack().getCount());
                    this.discard();
                }

                info.cancel();
            }
        }
    }
}