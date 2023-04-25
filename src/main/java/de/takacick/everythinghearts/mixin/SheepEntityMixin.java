package de.takacick.everythinghearts.mixin;

import de.takacick.everythinghearts.access.LivingProperties;
import de.takacick.everythinghearts.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends Entity {

    public SheepEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "sheared", at = @At("HEAD"))
    public void sheared(SoundCategory shearedSoundCategory, CallbackInfo info) {
        if (((LivingProperties) this).isHeartInfected()) {
            int i = 1 + this.random.nextInt(2);
            for (int j = 0; j < i; ++j) {
                ItemEntity itemEntity = this.dropItem(ItemRegistry.HEART, 1);
                if (itemEntity == null) continue;
                itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
            }
        }
    }
}
