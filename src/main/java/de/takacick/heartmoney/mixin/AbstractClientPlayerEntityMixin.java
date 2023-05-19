package de.takacick.heartmoney.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.heartmoney.registry.ItemRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @ModifyArg(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private Item getFovMultiplier(Item item) {
        AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) (Object) this;

        if (abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.BLOOD_KATANA)) {
            return ItemRegistry.BLOOD_KATANA;
        } else if (abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.HEART_PULSE_BLASTER)) {
            return ItemRegistry.HEART_PULSE_BLASTER;
        }

        return item;
    }
}

