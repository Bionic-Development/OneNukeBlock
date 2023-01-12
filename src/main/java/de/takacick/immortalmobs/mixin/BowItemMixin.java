package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.entity.projectiles.ImmortalArrowEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @ModifyArg(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"), index = 0)
    public Entity onStoppedUsing(Entity entity) {

        LivingEntity livingEntity = (LivingEntity) Objects.requireNonNull(((PersistentProjectileEntity) entity).getOwner());
        ArrowEntity persistentProjectile = (ArrowEntity) entity;

        if (livingEntity instanceof PlayerEntity playerEntity) {

            ItemStack stack = playerEntity.getMainHandStack().getItem() instanceof BowItem ? playerEntity.getMainHandStack() : playerEntity.getOffHandStack();
            World world = playerEntity.getEntityWorld();
            ItemStack itemStack = playerEntity.getArrowType(stack);

            if (itemStack.isOf(ItemRegistry.IMMORTAL_ARROW)) {
                ImmortalArrowEntity immortalArrowEntity = new ImmortalArrowEntity(world, playerEntity);
                immortalArrowEntity.copyFrom(persistentProjectile);
                return immortalArrowEntity;
            }
        }

        return persistentProjectile;
    }
}