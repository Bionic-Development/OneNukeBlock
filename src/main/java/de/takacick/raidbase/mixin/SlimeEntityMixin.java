package de.takacick.raidbase.mixin;

import de.takacick.raidbase.RaidBase;
import de.takacick.raidbase.access.SlimeProperties;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.SlimeEntity;
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

@Mixin(SlimeEntity.class)
@Implements({@Interface(iface = SlimeProperties.class, prefix = "raidbase$")})
public abstract class SlimeEntityMixin extends MobEntity
        implements Monster {

    @Unique
    private static final TrackedData<Boolean> raidbase$SHEARED = BionicDataTracker.registerData(new Identifier(RaidBase.MOD_ID, "slime_sheared"), TrackedDataHandlerRegistry.BOOLEAN);

    protected SlimeEntityMixin(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(raidbase$SHEARED, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (raidbase$isSlimeSheared()) {
            nbt.putBoolean("raidbase$slimeSheared", raidbase$isSlimeSheared());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("raidbase$slimeSheared", NbtElement.BYTE_TYPE)) {
            raidbase$setSlimeSheared(nbt.getBoolean("raidbase$slimeSheared"));
        }
    }

    public void raidbase$setSlimeSheared(boolean pigSoldier) {
        getDataTracker().set(raidbase$SHEARED, pigSoldier);
    }

    public boolean raidbase$isSlimeSheared() {
        return getDataTracker().get(raidbase$SHEARED);
    }

}
