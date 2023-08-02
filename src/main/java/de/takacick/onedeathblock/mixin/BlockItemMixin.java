package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.access.PlayerProperties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin extends Item {

    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", shift = At.Shift.BEFORE))
    public void place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> info) {
        PlayerEntity playerEntity = context.getPlayer();

        if (playerEntity instanceof PlayerProperties playerProperties && playerProperties.hasExplosivePlacing()) {
            if (!playerEntity.getWorld().isClient) {
                BlockPos blockPos = context.getBlockPos();
                Vec3d center = Vec3d.ofCenter(blockPos);

                playerEntity.getWorld().createExplosion(null, center.getX(), center.getY(), center.getZ(), 2.3f, Explosion.DestructionType.NONE);
            }
        }
    }
}

