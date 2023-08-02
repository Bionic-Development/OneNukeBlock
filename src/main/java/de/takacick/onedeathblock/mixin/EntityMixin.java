package de.takacick.onedeathblock.mixin;

import de.takacick.onedeathblock.damage.DeathDamageSources;
import de.takacick.onedeathblock.registry.item.SpikyIronArmorSuit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public World world;

    @Inject(method = "move", at = @At("HEAD"))
    public void move(MovementType movementType, Vec3d movement, CallbackInfo info) {
        if (!this.world.isClient && (Object) this instanceof PlayerEntity playerEntity) {
            if (playerEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof SpikyIronArmorSuit) {
                if (movement.length() > 0.08) {
                    playerEntity.damage(DeathDamageSources.SPIKY_IRON_ARMOR, 10f);
                }
            }
        }
    }
}