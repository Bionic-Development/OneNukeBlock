package de.takacick.secretcraftbase.mixin;

import de.takacick.secretcraftbase.access.LivingProperties;
import de.takacick.secretcraftbase.registry.ItemRegistry;
import de.takacick.secretcraftbase.server.utils.EntityNbtHelper;
import net.minecraft.block.AbstractTorchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "secretcraftbase$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    @Nullable
    protected abstract SoundEvent getHurtSound(DamageSource source);

    @Shadow
    protected abstract float getSoundVolume();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    public void handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        if (getType().equals(EntityType.PIG)) {
            if (!getWorld().isClient) {
                BlockState blockState = getWorld().getBlockState(getBlockPos());
                if (blockState.getBlock() instanceof AbstractTorchBlock) {
                    getWorld().playSound(null, getX(), getY(), getZ(), getHurtSound(damageSource), getSoundCategory(), getSoundVolume(), getPitch());
                    dropStack(EntityNbtHelper.fromEntity(ItemRegistry.PIG, this));
                    this.discard();
                    info.setReturnValue(false);
                }
            }
        }
    }

    public void secretcraftbase$setPoopTicks(int poopTicks) {

    }

    public boolean secretcraftbase$hasPoop() {
        return false;
    }
}