package de.takacick.onegirlboyblock.mixin.armor.butterflywings;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.takacick.onegirlboyblock.registry.item.ButterflyWings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "checkFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    public boolean checkFallFlying(boolean original) {
        if (this.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ButterflyWings) {
            return true;
        }

        return original;
    }
}

