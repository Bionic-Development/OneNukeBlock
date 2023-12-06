package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.EmeraldMoney;
import de.takacick.emeraldmoney.access.PlayerProperties;
import de.takacick.emeraldmoney.registry.ItemRegistry;
import de.takacick.emeraldmoney.registry.ParticleRegistry;
import de.takacick.utils.BionicUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    public abstract ItemStack getOffHandStack();

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    public abstract boolean clearStatusEffects();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onEquipStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
    private void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        if (slot.equals(EquipmentSlot.CHEST)) {
            if (newStack.isOf(ItemRegistry.VILLAGER_ROBE)) {
                BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, EmeraldMoney.IDENTIFIER, 2);
            }
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onDeath(DamageSource damageSource, CallbackInfo info) {
        if (!getWorld().isClient) {
            if (damageSource.getSource() instanceof LivingEntity livingEntity
                    && livingEntity.getMainHandStack().isOf(ItemRegistry.EMERALD_GAUNTLET)) {
                for (int i = 0; i < random.nextBetween(1, 2); i++) {
                    dropItem(Items.EMERALD);
                }

                ((ServerWorld) getWorld()).spawnParticles(ParticleRegistry.EMERALD_EXPLOSION,
                        getX(), getBodyY(0.5), getZ(), 1, 0, 0, 0, 0);
            }
        }
    }

    @Inject(method = "tryUseTotem", at = @At("HEAD"), cancellable = true)
    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if ((Object) this instanceof PlayerEntity playerEntity
                && playerEntity instanceof PlayerProperties playerProperties) {
            ItemStack itemStack = getMainHandStack().isOf(ItemRegistry.EMERALD_TOTEM) ? getMainHandStack() : getOffHandStack();

            if (itemStack.isOf(ItemRegistry.EMERALD_TOTEM)) {
                this.velocityModified = false;
                this.setHealth(1.0f);

                int emeralds = getRandom().nextBetween(1000, 2000);

                if (!playerProperties.hasEmeraldWallet()) {
                    for (int dropped = 0; dropped < emeralds; ) {
                        int drop = Math.min(getWorld().getRandom().nextBetween(5, 32), emeralds);
                        ItemEntity itemEntity = new ItemEntity(getWorld(), getX(), getBodyY(0.5), getZ(), new ItemStack(Items.EMERALD, drop), getRandom().nextGaussian() * 0.45, getRandom().nextGaussian() * 0.45, getRandom().nextGaussian() * 0.45);
                        itemEntity.setPickupDelay(15);
                        getWorld().spawnEntity(itemEntity);
                        dropped += drop;
                    }
                } else {
                    playerProperties.addEmeralds(emeralds, true);
                }

                this.clearStatusEffects();
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));

                BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, EmeraldMoney.IDENTIFIER, 3);
                info.setReturnValue(true);
                itemStack.decrement(1);
            }
        }
    }
}