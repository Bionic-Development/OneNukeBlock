package de.takacick.onescaryblock.mixin.lightning;

import de.takacick.onescaryblock.OneScaryBlock;
import de.takacick.onescaryblock.access.HerobrineLightningProperties;
import de.takacick.onescaryblock.utils.datatracker.HerobrineLightningDamageHelper;
import de.takacick.onescaryblock.utils.datatracker.OneScaryBlockDataTracker;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = HerobrineLightningProperties.class, prefix = "onescaryblock$")})
public abstract class LivingEntityMixin extends Entity {

    @Unique
    private static final TrackedData<HerobrineLightningDamageHelper> onescaryblock$HEROBRINE_LIGHTNING = BionicDataTracker.registerData(new Identifier(OneScaryBlock.MOD_ID, "herobrine_lightning"), OneScaryBlockDataTracker.HEROBRINE_LIGHTNING);
    @Unique
    private HerobrineLightningDamageHelper onescaryblock$herobrineLightningDamageHelper = new HerobrineLightningDamageHelper();

    protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onescaryblock$HEROBRINE_LIGHTNING, new HerobrineLightningDamageHelper());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo info) {
        HerobrineLightningDamageHelper herobrineLightningDamageHelper = onescaryblock$getHerobrineLightningHelper();
        herobrineLightningDamageHelper.tick((LivingEntity) (Object) this);
        if (getWorld().isClient) {
            herobrineLightningDamageHelper.sync(getDataTracker().get(onescaryblock$HEROBRINE_LIGHTNING));
        } else if (herobrineLightningDamageHelper.isDirty()) {
            onescaryblock$setHerobrineLightningHelper(herobrineLightningDamageHelper);
            herobrineLightningDamageHelper.setDirty(false);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        HerobrineLightningDamageHelper herobrineLightningDamageHelper = onescaryblock$getHerobrineLightningHelper();

        if (herobrineLightningDamageHelper != null) {
            nbt.put("onescaryblock$herobrineLightningDamageHelper", herobrineLightningDamageHelper.writeNbt(new NbtCompound()));
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("onescaryblock$herobrineLightningDamageHelper", NbtElement.COMPOUND_TYPE)) {
            HerobrineLightningDamageHelper herobrineLightningDamageHelper = onescaryblock$getHerobrineLightningHelper();
            herobrineLightningDamageHelper.readNbt(nbt.getCompound("onescaryblock$herobrineLightningDamageHelper"));
            onescaryblock$setHerobrineLightningHelper(herobrineLightningDamageHelper);
        }
    }

    public void onescaryblock$setHerobrineLightningHelper(HerobrineLightningDamageHelper herobrineLightningDamageHelper) {
        getDataTracker().set(onescaryblock$HEROBRINE_LIGHTNING, HerobrineLightningDamageHelper.copy(herobrineLightningDamageHelper));
        this.onescaryblock$herobrineLightningDamageHelper = herobrineLightningDamageHelper;
    }

    public HerobrineLightningDamageHelper onescaryblock$getHerobrineLightningHelper() {
        return this.onescaryblock$herobrineLightningDamageHelper;
    }
}

