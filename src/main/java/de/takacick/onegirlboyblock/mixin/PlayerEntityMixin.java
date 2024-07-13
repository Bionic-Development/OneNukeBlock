package de.takacick.onegirlboyblock.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.item.GlitterBlade;
import de.takacick.onegirlboyblock.registry.item.HandheldItem;
import de.takacick.onegirlboyblock.utils.data.AttachmentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract PlayerInventory getInventory();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @WrapWithCondition(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    public boolean eatFood(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, @Local(argsOnly = true) ItemStack stack) {

        if (stack.isOf(ItemRegistry.STRAWBERRY_SHORTCAKE)) {
            world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1f, world.getRandom().nextFloat() * 0.1f + 1.5f);
            return false;
        }

        return true;
    }

    @Inject(method = "spawnSweepAttackParticles", at = @At("HEAD"), cancellable = true)
    public void spawnSweepAttackParticles(CallbackInfo info) {
        if (getMainHandStack().getItem() instanceof GlitterBlade) {
            info.cancel();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        getAttachedOrCreate(AttachmentTypes.BASEBALL_BAT).tick(this);

        for (int i = 0; i < getInventory().size(); i++) {
            ItemStack itemStack = getInventory().getStack(i);
            if (itemStack.getItem() instanceof HandheldItem handheldItem) {
                handheldItem.tickInventory(this, itemStack);
            }
        }
    }
}

