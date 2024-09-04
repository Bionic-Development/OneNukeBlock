package de.takacick.onenukeblock.mixin.diamondhazmatarmor;

import de.takacick.onenukeblock.registry.item.DiamondHazmatArmorItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    public abstract boolean removeStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Unique
    private boolean onenukeblock$hazmatChestplate = false;
    @Unique
    private boolean onenukeblock$hazmatLeggings = false;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {

            if (getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof DiamondHazmatArmorItem) {
                addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 0, false, false, true));
                this.onenukeblock$hazmatChestplate = true;
            } else if (this.onenukeblock$hazmatChestplate) {
                this.onenukeblock$hazmatChestplate = false;
                removeStatusEffect(StatusEffects.RESISTANCE);
            }

            if (getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof DiamondHazmatArmorItem) {
                if (!this.hasStatusEffect(StatusEffects.REGENERATION)) {
                    addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, -1, 0, false, false, true));
                }
                this.onenukeblock$hazmatLeggings = true;
            } else if (this.onenukeblock$hazmatLeggings) {
                this.onenukeblock$hazmatLeggings = false;
                removeStatusEffect(StatusEffects.REGENERATION);
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("onenukeblock$hazmatChestplate", this.onenukeblock$hazmatChestplate);
        nbt.putBoolean("onenukeblock$hazmatLeggings", this.onenukeblock$hazmatLeggings);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.onenukeblock$hazmatChestplate = nbt.getBoolean("onenukeblock$hazmatChestplate");
        this.onenukeblock$hazmatLeggings = nbt.getBoolean("onenukeblock$hazmatLeggings");
    }
}
