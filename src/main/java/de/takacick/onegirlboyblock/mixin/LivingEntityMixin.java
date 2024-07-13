package de.takacick.onegirlboyblock.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import de.takacick.onegirlboyblock.registry.ItemRegistry;
import de.takacick.onegirlboyblock.registry.ParticleRegistry;
import de.takacick.onegirlboyblock.registry.item.BaseballBat;
import de.takacick.onegirlboyblock.utils.data.AttachmentTypes;
import de.takacick.onegirlboyblock.utils.data.attachments.TetrisDamageHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getActiveItem();

    @Shadow
    public abstract boolean isUsingItem();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        Optional.ofNullable(getAttached(AttachmentTypes.SIZE_HELPER))
                .ifPresent(attachment -> {
                    attachment.tick((LivingEntity) (Object) this);
                    if (!attachment.isRunning()) {
                        removeAttached(AttachmentTypes.SIZE_HELPER);
                    }
                });

        Optional.ofNullable(getAttached(AttachmentTypes.TETRIS_DAMAGE_HELPER))
                .ifPresent(attachment -> {
                    attachment.tick((LivingEntity) (Object) this);
                    if (attachment.shouldRemove()) {
                        removeAttached(AttachmentTypes.TETRIS_DAMAGE_HELPER);
                    }
                });
    }

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true)
    public void isBlocking(CallbackInfoReturnable<Boolean> info) {
        if (this.isUsingItem() && getActiveItem().getItem() instanceof BaseballBat) {
            info.setReturnValue(true);
        }
    }

    @ModifyExpressionValue(method = "playHurtSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getHurtSound(Lnet/minecraft/entity/damage/DamageSource;)Lnet/minecraft/sound/SoundEvent;"))
    public SoundEvent playHurtSound(SoundEvent original, @Local(argsOnly = true) DamageSource damageSource) {
        if (damageSource.isOf(TetrisDamageHelper.TETRIS)) {
            return ParticleRegistry.BIT_HIT;
        }
        return original;
    }

    @WrapWithCondition(method = "spawnConsumptionEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", ordinal = 1))
    public boolean eatFood(LivingEntity instance, SoundEvent soundEvent, float volume, float pitch, @Local(argsOnly = true) ItemStack stack) {

        if (stack.isOf(ItemRegistry.STRAWBERRY_SHORTCAKE)) {
            getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1f, getWorld().getRandom().nextFloat() * 0.2f + 1.5f);
            return false;
        }

        return true;
    }
}

