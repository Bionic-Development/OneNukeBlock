package de.takacick.elementalblock.mixin;

import com.mojang.authlib.GameProfile;
import de.takacick.elementalblock.registry.ItemRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @ModifyArg(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private Item getFovMultiplier(Item item) {
        AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) (Object) this;

        if (abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.WHISPERWIND_BOW)) {
            return ItemRegistry.WHISPERWIND_BOW;
        } else if (abstractClientPlayerEntity.getActiveItem().isOf(ItemRegistry.COBBLE_GAUNTLET)) {
            return ItemRegistry.COBBLE_GAUNTLET;
        }

        return item;
    }
}

