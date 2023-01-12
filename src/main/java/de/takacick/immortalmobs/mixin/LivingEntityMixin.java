package de.takacick.immortalmobs.mixin;

import de.takacick.immortalmobs.ImmortalMobs;
import de.takacick.immortalmobs.access.LivingProperties;
import de.takacick.immortalmobs.registry.ItemRegistry;
import de.takacick.immortalmobs.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "immortalmobs$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    protected abstract void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity);

    @Shadow
    public abstract SoundEvent getEatSound(ItemStack stack);

    @Shadow
    public abstract ItemStack getMainHandStack();

    private boolean immortalmobs$immortalTrapped = false;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo info) {
        if (immortalmobs$immortalTrapped && (Object) this instanceof MobEntity mobEntity) {
            if (world.getBlockState(getBlockPos().add(0, -1, 0)).isOf(ItemRegistry.IMMORTAL_CHAIN_TRAP)) {
                mobEntity.setAiDisabled(true);
                BionicUtils.sendEntityStatus((ServerWorld) world, mobEntity, ImmortalMobs.IDENTIFIER, 13);
            } else {
                BionicUtils.sendEntityStatus((ServerWorld) world, mobEntity, ImmortalMobs.IDENTIFIER, 14);
                immortalmobs$immortalTrapped = false;
                mobEntity.setAiDisabled(false);
            }
        }
    }

    @Inject(method = "getPreferredEquipmentSlot", at = @At(value = "HEAD"), cancellable = true)
    private static void getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
        if (stack.isOf(ItemRegistry.IMMORTAL_SHIRT)) {
            info.setReturnValue(EquipmentSlot.CHEST);
        }
    }

    @Inject(method = "travel", at = @At(value = "HEAD"), cancellable = true)
    private void travel(Vec3d movementInput, CallbackInfo info) {
        if (immortalmobs$immortalTrapped) {
            info.cancel();
        }
    }

    @Inject(method = "getMovementSpeed(F)F", at = @At(value = "HEAD"), cancellable = true)
    private void getMovementSpeed(float slipperiness, CallbackInfoReturnable<Float> info) {
        if (immortalmobs$immortalTrapped) {
            info.setReturnValue(0f);
        }
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At(value = "HEAD"))
    public void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo info) {
        if (getMainHandStack().isOf(ItemRegistry.IMMORTAL_SWORD)) {
            double d = -MathHelper.sin(this.getYaw() * ((float) Math.PI / 180));
            double e = MathHelper.cos(this.getYaw() * ((float) Math.PI / 180));
            if (this.world instanceof ServerWorld) {
                ((ServerWorld) this.world).spawnParticles(ParticleRegistry.IMMORTAL_SWEEP_ATTACK, this.getX() + d, this.getBodyY(0.5) + getRotationVector().getY() * 0.75, this.getZ() + e, 0, d, 0.0, e, 0.0);
            }
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0f, 0.5f);
        }
    }

    @Inject(method = "eatFood", at = @At(value = "HEAD"), cancellable = true)
    public void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (stack.isOf(ItemRegistry.IMMORTAL_PORKCHOP)) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            world.playSound(null, this.getX(), this.getY(), this.getZ(), this.getEatSound(stack), SoundCategory.NEUTRAL, 1.0f, 1.0f + (world.random.nextFloat() - world.random.nextFloat()) * 0.4f);
            this.applyFoodEffects(stack, world, livingEntity);
            this.emitGameEvent(GameEvent.EAT);
            info.setReturnValue(stack);
        }
    }

    public void immortalmobs$setImmortalTrapped(boolean immortalTrapped) {
        this.immortalmobs$immortalTrapped = immortalTrapped;
    }

    public boolean immortalmobs$isImmortalTrapped() {
        return this.immortalmobs$immortalTrapped;
    }
}