package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.registry.item.Cloud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getStack();

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tick(CallbackInfo info) {

        if (getStack().getItem() instanceof Cloud) {
            setNoGravity(true);

            setVelocity(getVelocity().multiply(0.9, 0.9, 0.9));

            if (getWorld().getRandom().nextDouble() <= 0.1) {
                getWorld().playSound(getX(), getBodyY(0.5), getZ(), SoundEvents.ENTITY_PHANTOM_FLAP, getSoundCategory(), 0.33f, 1.0f, false);
            }
            for (int i = 0; i < 1; i++) {
                getWorld().addParticle(ParticleTypes.CLOUD, getParticleX(getWidth()), getRandomBodyY(), getParticleZ(getWidth()), 0, 0, 0);
            }
        }
    }
}