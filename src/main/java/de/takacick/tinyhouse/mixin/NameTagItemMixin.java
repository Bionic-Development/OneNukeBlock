package de.takacick.tinyhouse.mixin;

import de.takacick.tinyhouse.registry.EntityRegistry;
import de.takacick.tinyhouse.registry.entity.living.RatEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NameTagItem.class)
public abstract class NameTagItemMixin extends Item {

    public NameTagItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setCustomName(Lnet/minecraft/text/Text;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if (entity instanceof SilverfishEntity) {
            if (stack.getName().getString().equals("Rat")) {
                RatEntity ratEntity = new RatEntity(EntityRegistry.RAT, entity.getWorld());
                ratEntity.copyFrom(entity);
                entity.discard();
                entity.getWorld().spawnEntity(ratEntity);

                info.setReturnValue(ActionResult.success(user.getWorld().isClient));
            }
        }
    }
}