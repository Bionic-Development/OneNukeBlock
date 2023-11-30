package de.takacick.elementalblock.mixin;

import de.takacick.elementalblock.OneElementalBlock;
import de.takacick.elementalblock.access.PlayerProperties;
import de.takacick.elementalblock.registry.ItemRegistry;
import de.takacick.elementalblock.registry.item.TerraDrill;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.UtilsProperties;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
@Implements({@Interface(iface = PlayerProperties.class, prefix = "elementalblock$")})
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Unique
    private static final TrackedData<Boolean> elementalblock$LAVA_BIONIC = BionicDataTracker.registerData(new Identifier(OneElementalBlock.MOD_ID, "lava_bionic"), TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private int elementalblock$lavaBionicTicks;

    protected PlayerEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        this.getDataTracker().startTracking(elementalblock$LAVA_BIONIC, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {

            if (this.elementalblock$lavaBionicTicks > 0) {
                this.elementalblock$lavaBionicTicks--;
                elementalblock$setLavaBionic(this.elementalblock$lavaBionicTicks);

                if (elementalblock$isLavaBionic()) {
                    ((UtilsProperties) this).setMainHeart(ItemRegistry.LAVA_HEART);
                    addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 0, false, false, false));
                    addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, -1, 0, false, false, false));
                    addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, -1, 0, false, false, false));
                } else {
                    BionicUtils.sendEntityStatus((ServerWorld) getWorld(), this, OneElementalBlock.IDENTIFIER, 4);
                }
            }
        }
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    public void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> info) {
        if (stack.isOf(ItemRegistry.TERRA_DRILL)) {
            TerraDrill.setRotation(stack, TerraDrill.getRotation(stack, 1f));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (this.elementalblock$lavaBionicTicks > 0) {
            nbt.putInt("elementalblock$lavaBionicTicks", this.elementalblock$lavaBionicTicks);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("elementalblock$lavaBionicTicks", NbtElement.INT_TYPE)) {
            this.elementalblock$lavaBionicTicks = nbt.getInt("elementalblock$lavaBionicTicks");
            getDataTracker().set(elementalblock$LAVA_BIONIC, this.elementalblock$lavaBionicTicks > 0);
        }
    }

    public void elementalblock$setLavaBionic(int lavaBionicTicks) {
        this.elementalblock$lavaBionicTicks = lavaBionicTicks;
        boolean lavaBionic = lavaBionicTicks > 0;
        if (elementalblock$isLavaBionic() && !lavaBionic) {
            removeStatusEffect(StatusEffects.RESISTANCE);
            removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
            removeStatusEffect(StatusEffects.STRENGTH);
            ((UtilsProperties) this).setMainHeart(null);
            OneElementalBlock.updateEntityHealth(this, 20, false);
        } else if (!elementalblock$isLavaBionic() && lavaBionic) {
            ((UtilsProperties) this).setMainHeart(ItemRegistry.LAVA_HEART);
            OneElementalBlock.updateEntityHealth(this, 80, true);
        }

        getDataTracker().set(elementalblock$LAVA_BIONIC, lavaBionic);
    }

    public boolean elementalblock$isLavaBionic() {
        return getDataTracker().get(elementalblock$LAVA_BIONIC);
    }
}
