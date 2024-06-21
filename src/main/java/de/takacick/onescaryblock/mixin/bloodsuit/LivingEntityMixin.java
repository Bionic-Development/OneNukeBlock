package de.takacick.onescaryblock.mixin.bloodsuit;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.BloodBorderProperties;
import de.takacick.onescaryblock.registry.ItemRegistry;
import de.takacick.onescaryblock.registry.entity.living.Entity303Entity;
import de.takacick.onescaryblock.utils.datatracker.BloodBorderSuitHelper;
import de.takacick.onescaryblock.utils.datatracker.OneScaryBlockDataTracker;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = BloodBorderProperties.class, prefix = "onescaryblock$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Unique
    private static final TrackedData<BloodBorderSuitHelper> onescaryblock$BLOOD_BARRIER_SUIT = BionicDataTracker.registerData(new Identifier(OneScaryBlock.MOD_ID, "blood_border_suit"), OneScaryBlockDataTracker.BLOOD_BARRIER_SUIT);
    @Unique
    private BloodBorderSuitHelper onescaryblock$bloodBorderSuitHelper = new BloodBorderSuitHelper();

    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onescaryblock$BLOOD_BARRIER_SUIT, new BloodBorderSuitHelper());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        BloodBorderSuitHelper bloodBorderSuitHelper = onescaryblock$getBloodBorderSuitHelper();
        bloodBorderSuitHelper.tick();
        if (getWorld().isClient) {
            bloodBorderSuitHelper.sync(getDataTracker().get(onescaryblock$BLOOD_BARRIER_SUIT));
        } else if (bloodBorderSuitHelper.isDirty()) {
            onescaryblock$setBloodBorderSuitHelper(bloodBorderSuitHelper);
            bloodBorderSuitHelper.setDirty(false);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        if (source.getSource() instanceof Entity303Entity && getEquippedStack(EquipmentSlot.CHEST).isOf(ItemRegistry.BLOOD_BORDER_SUIT)) {
            BloodBorderSuitHelper bloodBorderSuitHelper = onescaryblock$getBloodBorderSuitHelper();
            bloodBorderSuitHelper.setTick(bloodBorderSuitHelper.getMaxTicks());

            BionicUtils.sendEntityStatus(getWorld(), this, OneScaryBlock.IDENTIFIER, 2);
            info.setReturnValue(false);
        }
    }

    public void onescaryblock$setBloodBorderSuitHelper(BloodBorderSuitHelper bloodBorderSuitHelper) {
        getDataTracker().set(onescaryblock$BLOOD_BARRIER_SUIT, BloodBorderSuitHelper.copy(bloodBorderSuitHelper));
        this.onescaryblock$bloodBorderSuitHelper = bloodBorderSuitHelper;
    }

    public BloodBorderSuitHelper onescaryblock$getBloodBorderSuitHelper() {
        return this.onescaryblock$bloodBorderSuitHelper;
    }
}

