package de.takacick.deathmoney.mixin;

import de.takacick.deathmoney.registry.item.DamageableSuit;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Nameable;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements Inventory, Nameable {

    @Shadow
    @Final
    public DefaultedList<ItemStack> armor;

    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(method = "damageArmor", at = @At("TAIL"))
    public void damageArmor(DamageSource damageSource, float amount, int[] slots, CallbackInfo info) {
        for (int i : slots) {
            ItemStack itemStack = this.armor.get(i);
            if (damageSource.isFire() && itemStack.getItem().isFireproof() || !(itemStack.getItem() instanceof DamageableSuit))
                continue;
            itemStack.damage((int) amount, this.player, player -> player.sendEquipmentBreakStatus(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i)));
        }
    }
}

