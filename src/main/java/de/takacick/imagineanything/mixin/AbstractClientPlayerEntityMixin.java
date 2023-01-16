package de.takacick.imagineanything.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.imagineanything.ImagineAnything;
import de.takacick.imagineanything.registry.ItemRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    private final Identifier IRON_MAN_SUIT = new Identifier(ImagineAnything.MOD_ID, "textures/entity/iron_man_suit.png");

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @ModifyArg(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private Item getFovMultiplier(Item item) {
        AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) (Object) this;
        if (abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.MYSTERIOUS_LAMP)) {
            return ItemRegistry.MYSTERIOUS_LAMP;
        }
        return item;
    }


    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    public void getModel(CallbackInfoReturnable<String> info) {
        if (getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
            info.setReturnValue("slim");
        }
    }

    @Inject(method = "getSkinTexture", at = @At("HEAD"), cancellable = true)
    public void getSkinTexture(CallbackInfoReturnable<Identifier> info) {
        if (getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.IRON_MAN_SUIT)) {
            info.setReturnValue(IRON_MAN_SUIT);
        }
    }
}
