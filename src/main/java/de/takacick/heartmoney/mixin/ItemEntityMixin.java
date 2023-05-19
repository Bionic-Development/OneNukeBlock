package de.takacick.heartmoney.mixin;

import de.takacick.heartmoney.HeartMoney;
import de.takacick.heartmoney.access.PlayerProperties;
import de.takacick.heartmoney.registry.ItemRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        if (getStack().isOf(ItemRegistry.HEART) || getStack().isOf(ItemRegistry.HEART_ANGEL_HEART)) {
            if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(player.getUuid()))) {
                if(player.getActiveItem() != null && player.getActiveItem().isOf(ItemRegistry.STRONG_HEART_SUCKER)) {
                    if(world.getRandom().nextDouble() <= 0.7) {
                        BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 10);
                    }
                } else {
                    BionicUtils.sendEntityStatus((ServerWorld) world, this, HeartMoney.IDENTIFIER, 1);
                }
                player.sendPickup(this, getStack().getCount());

                float multiplier = (float) ((PlayerProperties) player).getHeartMultiplier();
                float health = 2f * getStack().getCount() * multiplier;

                player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                        .setBaseValue(player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue() + health);
                player.setHealth(player.getHealth() + health);

                this.discard();
            }

            info.cancel();
        }
    }

    @Inject(method = "canMerge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private static void canMerge(ItemStack stack1, ItemStack stack2, CallbackInfoReturnable<Boolean> info) {
        if (stack1.isOf(ItemRegistry.HEART) || stack2.isOf(ItemRegistry.HEART) || stack1.isOf(ItemRegistry.HEART_ANGEL_HEART) || stack2.isOf(ItemRegistry.HEART_ANGEL_HEART)) {
            info.setReturnValue(false);
        }
    }
}