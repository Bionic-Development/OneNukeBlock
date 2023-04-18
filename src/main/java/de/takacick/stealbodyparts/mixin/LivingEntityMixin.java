package de.takacick.stealbodyparts.mixin;

import de.takacick.stealbodyparts.StealBodyParts;
import de.takacick.utils.data.BionicTrackedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.Identifier;
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

    @Inject(method = "onTrackedDataSet", at = @At("TAIL"))
    public void onTrackedDataSet(TrackedData<?> data, CallbackInfo info) {
        if(data instanceof BionicTrackedData<?> bionicTrackedData) {
            if(bionicTrackedData.getIdentifier().equals(new Identifier(StealBodyParts.MOD_ID, "parts"))) {
                this.setBoundingBox(this.calculateBoundingBox());
            }
        }
    }
}