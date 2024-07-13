package de.takacick.onegirlboyblock.mixin.armor;

import de.takacick.onegirlboyblock.registry.item.ButterflyWings;
import de.takacick.onegirlboyblock.registry.item.Tiara;
import de.takacick.utils.common.event.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    @Nullable
    public abstract EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract boolean removeStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow
    protected abstract ItemStack getSyncedArmorStack(EquipmentSlot slot);

    @Shadow
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow
    public abstract boolean isFallFlying();

    @Unique
    private boolean onegirlboyblock$tiara = false;

    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (this.onegirlboyblock$tiara) {

                if (!(getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof Tiara)) {
                    removeStatusEffect(StatusEffects.RESISTANCE);
                    removeStatusEffect(StatusEffects.SPEED);
                    removeStatusEffect(StatusEffects.REGENERATION);
                    this.onegirlboyblock$tiara = false;
                } else {
                    addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 1, false, false, true));
                    addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, -1, 0, false, false, true));
                    if (!hasStatusEffect(StatusEffects.REGENERATION)) {
                        addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, -1, 1, false, false, true));
                    }
                }
            }
        }
    }

    @Inject(method = "getEquipmentChanges", at = @At(value = "RETURN"))
    private void getEquipmentChanges(CallbackInfoReturnable<@Nullable Map<EquipmentSlot, ItemStack>> info) {
        if (info.getReturnValue() != null) {
            int additionalHearts = 0;
            ItemStack oldStack = this.getSyncedArmorStack(EquipmentSlot.HEAD);
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.HEAD);

            boolean tiara = this.onegirlboyblock$tiara;
            if (oldStack.getItem() instanceof Tiara) {
                additionalHearts -= 60;
                tiara = false;
            }

            if (itemStack.getItem() instanceof Tiara) {
                additionalHearts += 60;
                tiara = true;
            }

            if (this.onegirlboyblock$tiara && !tiara) {
                removeStatusEffect(StatusEffects.RESISTANCE);
                removeStatusEffect(StatusEffects.SPEED);
                removeStatusEffect(StatusEffects.REGENERATION);
            }

            this.onegirlboyblock$tiara = tiara;

            if (additionalHearts != 0) {
                onegirlboyblock$resetTiaraHearts();
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("onegirlboyblock$tiara", this.onegirlboyblock$tiara);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.onegirlboyblock$tiara = nbt.getBoolean("onegirlboyblock$tiara");
    }

    private void onegirlboyblock$resetTiaraHearts() {
        int tiaraArmorHearts = 0;

        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.HEAD);
        if (itemStack.getItem() instanceof Tiara) {
            tiaraArmorHearts = 60;
        }

        EntityAttributeModifier entityAttributeModifier = getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getModifier(Tiara.HEARTS);

        if (entityAttributeModifier != null) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                    .removeModifier(Tiara.HEARTS);
        }

        if (tiaraArmorHearts > 0) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)
                    .addTemporaryModifier(new EntityAttributeModifier(Tiara.HEARTS, tiaraArmorHearts, EntityAttributeModifier.Operation.ADD_VALUE));
            if (entityAttributeModifier == null || tiaraArmorHearts > entityAttributeModifier.value()) {
                setHealth((float) (getHealth() + (tiaraArmorHearts - (entityAttributeModifier == null ? 0 : entityAttributeModifier.value()))));
            }
        }

        EventHandler.sendEntityHealthUpdate((LivingEntity) (Object) this);
    }
}

