package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.registry.ItemRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {

    @ModifyArg(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private Item getFovMultiplier(Item item) {
        AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) (Object) this;
        if (abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.DEADLY_SUPER_MAGNET)) {
            return ItemRegistry.DEADLY_SUPER_MAGNET;
        }

        return item;
    }
}
