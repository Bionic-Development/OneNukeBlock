package de.takacick.onegirlfriendblock.mixin;

import de.takacick.onegirlfriendblock.OneGirlfriendBlock;
import de.takacick.onegirlfriendblock.access.LivingProperties;
import de.takacick.utils.BionicUtils;
import de.takacick.utils.data.BionicDataTracker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Implements({@Interface(iface = LivingProperties.class, prefix = "onegirlfriendblock$")})
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract Random getRandom();

    @Shadow
    public abstract float getBodyYaw();

    @Unique
    private static final TrackedData<Float> onegirlfriendblock$LIPSTICK = BionicDataTracker.registerData(new Identifier(OneGirlfriendBlock.MOD_ID, "lipstick"), TrackedDataHandlerRegistry.FLOAT);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void initDataTracker(CallbackInfo info) {
        getDataTracker().startTracking(onegirlfriendblock$LIPSTICK, 0f);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (!getWorld().isClient) {
            if (isTouchingWaterOrRain() && onegirlfriendblock$getLipstickStrength() > 0f) {
                onegirlfriendblock$setLipstickStrength(0f);

                BionicUtils.sendEntityStatus(getWorld(), this, OneGirlfriendBlock.IDENTIFIER, 7);
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        if (onegirlfriendblock$getLipstickStrength() > 0) {
            nbt.putFloat("onegirlfriendblock$lipstick", onegirlfriendblock$getLipstickStrength());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("onegirlfriendblock$lipstick", NbtElement.FLOAT_TYPE)) {
            getDataTracker().set(onegirlfriendblock$LIPSTICK, nbt.getFloat("onegirlfriendblock$lipstick"));
        }
    }

    public void onegirlfriendblock$setLipstickStrength(float strength) {
        strength = MathHelper.clamp(strength, 0f, 1f);

        getDataTracker().set(onegirlfriendblock$LIPSTICK, strength);
    }

    public float onegirlfriendblock$getLipstickStrength() {
        return getDataTracker().get(onegirlfriendblock$LIPSTICK);
    }
}