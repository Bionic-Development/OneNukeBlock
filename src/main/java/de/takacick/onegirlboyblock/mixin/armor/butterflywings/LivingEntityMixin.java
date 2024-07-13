package de.takacick.onegirlboyblock.mixin.armor.butterflywings;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.onegirlboyblock.registry.item.ButterflyWings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow
    public abstract boolean isFallFlying();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "tickFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    public boolean checkFallFlying(boolean original) {
        if (this.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ButterflyWings) {
            return true;
        }

        return original;
    }


    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (isFallFlying() && getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ButterflyWings) {
            Vec3d vec3d = this.getRotationVector();
            double multiplier = 0.5f;

            Vec3d vec3d2 = this.getVelocity();
            this.setVelocity(vec3d2.add(vec3d.x * 0.1 + (vec3d.x * multiplier - vec3d2.x) * 0.5, vec3d.y * 0.1 + (vec3d.y * multiplier - vec3d2.y) * 0.5, vec3d.z * 0.1 + (vec3d.z * multiplier - vec3d2.z) * 0.5));

            if (getWorld().isClient) {
                Vec3d vector = getRotationVector().multiply(-0.3);
                for (int i = 0; i < 2; i++) {
                    Vec3d offset = new Vec3d(this.random.nextGaussian(), this.random.nextGaussian(), this.random.nextGaussian());
                    Vec3d rotation = getRotationVector(getPitch(), getYaw() + 90f);
                    this.getWorld().addParticle(ParticleRegistry.BUTTERFLY_GLITTER, getX() + vector.getX() + rotation.getX() * 0.5 * offset.getX(), getBodyY(0.5) + vector.getY() + offset.getY() * 0.15, getZ() + vector.getZ() + rotation.getZ() * 0.5 * offset.getZ(), offset.getX() * 0.05, offset.getY() * 0.05, offset.getZ() * 0.05);
                }
            }
        }
    }
}

