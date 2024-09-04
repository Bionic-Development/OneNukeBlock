package de.takacick.onenukeblock.mixin.explosivegummybear;

import de.takacick.onenukeblock.registry.item.ExplosiveGummyBear;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "spawnConsumptionEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;spawnItemParticles(Lnet/minecraft/item/ItemStack;I)V"))
    public void spawnConsumptionEffects(ItemStack stack, int particleCount, CallbackInfo info) {
        if (stack.getItem() instanceof ExplosiveGummyBear) {
            this.playSound(SoundEvents.BLOCK_MUD_HIT, 0.5f + 0.5f * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
    }
}

