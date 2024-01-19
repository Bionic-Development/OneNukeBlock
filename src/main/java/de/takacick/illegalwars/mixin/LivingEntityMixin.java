package de.takacick.illegalwars.mixin;

import de.takacick.illegalwars.IllegalWars;
import de.takacick.illegalwars.access.LivingProperties;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "illegalwars$")})
public abstract class LivingEntityMixin extends Entity {

    private static final TrackedData<Boolean> illegalwars$PIE = BionicDataTracker.registerData(new Identifier(IllegalWars.MOD_ID, "pie"), TrackedDataHandlerRegistry.BOOLEAN);
    private int illegalwars$pieTicks = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(illegalwars$PIE, false);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (this.illegalwars$pieTicks > 0) {
                illegalwars$setPieTicks(this.illegalwars$pieTicks - 1);

                if (!illegalwars$hasPie() && isPlayer()) {
                    BionicUtils.sendEntityStatus(getWorld(), this, IllegalWars.IDENTIFIER, 7);
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (this.illegalwars$pieTicks > 0) {
            nbt.putInt("illegalwars$pieTicks", this.illegalwars$pieTicks);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        this.illegalwars$pieTicks = nbt.getInt("illegalwars$pieTicks");
    }

    public void illegalwars$setPieTicks(int pieTicks) {
        this.illegalwars$pieTicks = pieTicks;
        getDataTracker().set(illegalwars$PIE, pieTicks > 0);
    }

    public boolean illegalwars$hasPie() {
        return getDataTracker().get(illegalwars$PIE);
    }
}