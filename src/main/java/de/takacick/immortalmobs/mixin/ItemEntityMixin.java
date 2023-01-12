package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.registry.entity.custom.ImmortalItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "canMerge()Z", at = @At(value = "HEAD"), cancellable = true)
    public void canMerge(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ImmortalItemEntity) {
            cir.setReturnValue(false);
        }
    }
}