package de.takacick.upgradebody.mixin;

import de.takacick.upgradebody.UpgradeBody;
import de.takacick.upgradebody.access.ExperienceOrbProperties;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrbEntity.class)
@Implements({@Interface(iface = ExperienceOrbProperties.class, prefix = "upgradebody$")})
public abstract class ExperienceOrbEntityMixin extends Entity {

    @Shadow
    protected abstract int repairPlayerGears(PlayerEntity player, int amount);

    @Shadow
    private int amount;
    @Shadow
    private int pickingCount;

    @Shadow
    public abstract int getExperienceAmount();

    @Shadow
    protected abstract void expensiveUpdate();

    private static final TrackedData<Integer> upgradebody$COOLDOWN = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "xp_cooldown"), TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> upgradebody$LEVEL = BionicDataTracker.registerData(new Identifier(UpgradeBody.MOD_ID, "xp_level"), TrackedDataHandlerRegistry.BOOLEAN);

    public ExperienceOrbEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(upgradebody$COOLDOWN, 0);
        getDataTracker().startTracking(upgradebody$LEVEL, false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient && getDataTracker().get(upgradebody$COOLDOWN) > 0) {
            upgradebody$setCooldown(getDataTracker().get(upgradebody$COOLDOWN) - 1);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("levelOrb", getDataTracker().get(upgradebody$LEVEL));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        getDataTracker().set(upgradebody$LEVEL, nbt.getBoolean("levelOrb"));
    }

    @Inject(method = "expensiveUpdate", at = @At("HEAD"), cancellable = true)
    public void expensiveUpdate(CallbackInfo info) {
        if (getDataTracker().get(upgradebody$COOLDOWN) > 0) {
            info.cancel();
        }
    }

    @Inject(method = "isMergeable(Lnet/minecraft/entity/ExperienceOrbEntity;)Z", at = @At("HEAD"), cancellable = true)
    public void isMergeable(ExperienceOrbEntity other, CallbackInfoReturnable<Boolean> info) {
        if (getDataTracker().get(upgradebody$LEVEL)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    public void onPlayerCollision(PlayerEntity player, CallbackInfo info) {
        if (this.getWorld().isClient) {
            return;
        }

        if (getDataTracker().get(upgradebody$LEVEL)) {
            if (player.experiencePickUpDelay == 0) {
                player.experiencePickUpDelay = 2;
                player.sendPickup(this, 1);
                int i = this.repairPlayerGears(player, this.amount);
                if (i > 0) {
                    player.addExperienceLevels(i);
                }
                --this.pickingCount;
                if (this.pickingCount == 0) {
                    this.discard();
                }
            }

            info.cancel();
        }
    }

    @Inject(method = "getOrbSize", at = @At("HEAD"), cancellable = true)
    public void getOrbSize(CallbackInfoReturnable<Integer> info) {
        if (getDataTracker().get(upgradebody$LEVEL)) {
            info.setReturnValue(getExperienceAmount());
        }
    }

    public void upgradebody$setLevelOrb(boolean levelOrb) {
        getDataTracker().set(upgradebody$LEVEL, levelOrb);
    }

    public void upgradebody$setCooldown(int cooldown) {
        getDataTracker().set(upgradebody$COOLDOWN, cooldown);
    }
}