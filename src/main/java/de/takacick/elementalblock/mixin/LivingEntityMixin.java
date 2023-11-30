package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.access.PlayerProperties;
import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.elementalblock.registry.item.WaterArmorVessel;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicTrackedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow
    public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow
    protected abstract ItemStack getSyncedArmorStack(EquipmentSlot slot);

    @Shadow protected abstract void initDataTracker();

    @Unique
    private boolean elementalblock$waterArmorVessel = false;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.WATER_ARMOR_VESSEL)) {
                this.elementalblock$waterArmorVessel = true;
                addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, -1, 0, false, false, true));
                addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, -1, 0, false, false, true));
                addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 0, false, false, true));
            } else if (this.elementalblock$waterArmorVessel) {
                this.elementalblock$waterArmorVessel = false;
                removeStatusEffect(StatusEffects.WATER_BREATHING);
                removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
                removeStatusEffect(StatusEffects.RESISTANCE);
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (this.elementalblock$waterArmorVessel) {
            nbt.putBoolean("elementalblock$waterArmorVessel", true);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.elementalblock$waterArmorVessel = nbt.getBoolean("elementalblock$waterArmorVessel");
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    public float travel(float x) {
        if (getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.WATER_ARMOR_VESSEL)) {
            x += 0.05f;
        }

        return x;
    }

    @Inject(method = "getEquipmentChanges", at = @At(value = "RETURN"))
    private void getEquipmentChanges(CallbackInfoReturnable<@Nullable Map<EquipmentSlot, ItemStack>> info) {
        if (info.getReturnValue() != null) {
            ItemStack oldStack = this.getSyncedArmorStack(EquipmentSlot.CHEST);
            ItemStack itemStack = this.getEquippedStack(EquipmentSlot.CHEST);

            if (itemStack.getItem() instanceof WaterArmorVessel
                    && !(oldStack.getItem() instanceof WaterArmorVessel) && this.age > 1) {
                BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, OneElementalBlock.IDENTIFIER, 1);
            }
        }
    }
    @Inject(method = "onTrackedDataSet", at = @At(value = "RETURN"))
    private void onTrackedDataSet(TrackedData<?> data, CallbackInfo info) {
        if(this instanceof PlayerProperties playerProperties) {
            if (data instanceof BionicTrackedData bionicTrackedData
                    && bionicTrackedData.getIdentifier().equals(new Identifier(OneElementalBlock.MOD_ID, "lava_bionic"))) {
                if(playerProperties.isLavaBionic()) {

                }
            }
        }
    }
}