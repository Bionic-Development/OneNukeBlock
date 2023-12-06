package de.takacick.emeraldmoney.mixin;

import de.takacick.emeraldmoney.access.VillagerProperties;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerResemblingModel.class)
public abstract class VillagerResemblingModelMixin<T extends Entity>
        extends SinglePartEntityModel<T>
        implements ModelWithHead,
        ModelWithHat {

    @Shadow
    @Final
    protected ModelPart nose;

    @Inject(method = "setAngles", at = @At("TAIL"))
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof VillagerProperties villagerProperties) {
            this.nose.visible = villagerProperties.hasNose();
        }
    }
}
