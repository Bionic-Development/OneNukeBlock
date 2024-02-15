package de.takacick.secretgirlbase.mixin.leadcuffs;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import de.takacick.secretgirlbase.access.LeadCuffProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onTrackedDataSet", at = @At(value = "HEAD"))
    private void onTrackedDataSet(TrackedData<?> data, CallbackInfo info) {
        if (this instanceof LeadCuffProperties leadCuffProperties) {
            leadCuffProperties.trackedDataSet(data);
        }
    }

    @ModifyReturnValue(method = "getStepHeight", at = @At(value = "RETURN"))
    private float getStepHeight(float step) {
        if (this instanceof LeadCuffProperties leadCuffProperties
                && leadCuffProperties.isLeadCuffed()) {
            step += 1.5f;
        }

        return step;
    }
}